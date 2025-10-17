import { Component, Input, OnDestroy, OnInit } from '@angular/core';

import { BehaviorSubject, map, Observable, Subject, switchMap, take, takeUntil } from 'rxjs';

import { SubjectsService } from '@pages/services/subjects.service';
import { Subject as SubjectInterface } from '@pages/interfaces/Subject.interface';
import { SubscriptionService } from '@pages/services/subscription.service';
import { AuthService } from '@pages/services/auth.service';
import { Subscription } from '@pages/interfaces/Subscription.interface';

@Component({
  selector: 'app-subjects',
  templateUrl: './subjects.component.html',
  styleUrls: ['./subjects.component.scss']
})
export class SubjectsComponent implements OnInit, OnDestroy {

  private subjectsBehaviorSubject = new BehaviorSubject<SubjectInterface[]>([]);
  public subjects$ = this.subjectsBehaviorSubject.asObservable();
  public subscriptions$!: Observable<Subscription[]>;
  private destroy$ = new Subject<void>();

  readonly labelsForInterface = {
    subscribe: "S'abonner",
    alreadySubscribed: "Déjà abonné",
    unsubscribe: "Se désabonner"
  }

  @Input() isProfileView: boolean = false;

  constructor(private subjectsService: SubjectsService, private subscriptionService: SubscriptionService, private authService: AuthService) { }

  ngOnInit(): void {
    this.loadSubjects();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
    this.subjectsBehaviorSubject.complete();
  }

  subscribeToSubject(subjectId: number): void {
    // Optimistic update for UI
    this.updateOptimisticSubjectSubscription(subjectId, true);
    
    this.subscriptionService.subscribeToSubject(subjectId)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (response) => {
          console.log('Subscribed successfully:', response);
        }, error: (error) => {
          console.error('Error subscribing to subject:', error);
          // In case of error, revert the state
          this.updateOptimisticSubjectSubscription(subjectId, false);
        }
      });
  }

  unsubscribeFromSubject(subjectId: number): void {
    // Optimistic update for UI
    this.updateOptimisticSubjectSubscription(subjectId, false);
    
    this.subscriptionService.unsubscribeFromSubject(subjectId)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: () => {
          console.log('Unsubscribed successfully');
        }, error: (error) => {
          console.error('Error unsubscribing from subject:', error);
          // In case of error, revert the state
          this.updateOptimisticSubjectSubscription(subjectId, true);
        }
      });
  }

  isSubscribed(subject: SubjectInterface): boolean {
    return !!(subject.subscriptionByUser);
  }

  private loadSubjects(): void {
    this.subscriptionService.getAllSubscribedSubjectsForUser().pipe(
      take(1),
      switchMap((subscriptions: Subscription[]) => {
        const safeSubscriptions = (subscriptions && subscriptions.length > 0) ? subscriptions : [];
        return this.subjectsService.getAll().pipe(
          take(1),
          map((subjects: SubjectInterface[]) => {
            if(this.isProfileView) {
              subjects = subjects.filter(subject => safeSubscriptions.some(subscription => subscription.subjectId === subject.id));
            }
            else {
              subjects.forEach(subject => {
                subject.subscriptionByUser = safeSubscriptions.find(subscription => subscription.subjectId === subject.id) || null;
              });
            } 
            return subjects;
          })
        );
      })
    ).subscribe(subjects => {
      this.subjectsBehaviorSubject.next(subjects);
    });
  }

  /** 
   * Optimistic Method
   * subscriptionByUser: isSubscribed ? { id: 0, subjectId, userId: 0 } : null <= It's a substitute for a real subscription object, 
   * used while waiting for the API response that will return the correct data.
   * This allows transitioning from the "subscribed" state to the "unsubscribed" state or vice versa, without any flash issues.
   * */ 
  private updateOptimisticSubjectSubscription(subjectId: number, isSubscribed: boolean): void {
    const currentSubjects = this.subjectsBehaviorSubject.value;
    const updatedSubjects = currentSubjects.map(subject => {
      if (subject.id === subjectId) {
        if(this.isProfileView) {
          return null; // Remove the subject from the list in profile view
        }

        return {
          ...subject,
          subscriptionByUser: isSubscribed ? { id: 0, subjectId, userId: 0 } : null
        };
      }
      return subject;
    });
    this.subjectsBehaviorSubject.next(updatedSubjects.filter(subject => subject !== null) as SubjectInterface[]);
  }
}
