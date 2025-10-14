import { Component, OnInit } from '@angular/core';

import { map, Observable, switchMap, take } from 'rxjs';

import { User } from '@core/interfaces/user.interface';

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
export class SubjectsComponent implements OnInit {

  public subjects$!: Observable<SubjectInterface[]>;
  public subscriptions$!: Observable<Subscription[]>;
  readonly labelsForInterface = {
    subscribe: "S'abonner",
    unsubscribe: "Déjà abonné"
  }

  constructor(private subjectsService: SubjectsService, private subscriptionService: SubscriptionService, private authService: AuthService) { }

  ngOnInit(): void {
    this.loadSubjects();
  }

  subscribeToSubject(subjectId: number): void {
    this.subscriptionService.subscribeToSubject(subjectId).subscribe({
      next: (response) => {
        console.log('Subscribed successfully:', response);
        this.loadSubjects();
      }, error: (error) => {
        console.error('Error subscribing to subject:', error);
      }
    });
  }

  unsubscribeFromSubject(subjectId: number): void {
    console.log('Unsubscribe from subject with ID:', subjectId);
    //
  }

  isSubscribed(subject: SubjectInterface): boolean {
    return !!(subject.subscriptionByUser);
  }

  private loadSubjects(): void {
    this.subjects$ = this.subscriptionService.getAllSubscribedSubjectsForUser().pipe(
      take(1),
      switchMap((subscriptions: Subscription[]) => {
        return this.subjectsService.getAll().pipe(
          map((subjects: SubjectInterface[]) => {
            subjects.forEach(subject => {
              subject.subscriptionByUser = subscriptions.find(sub => sub.subjectId === subject.id) || null;
            });
            return subjects;
          })
        );
      })
    );
  }
}
