import { Component, OnInit } from '@angular/core';
import { SubjectsService } from '@app/pages/services/subjects.service';
import { Subject as SubjectInterface } from '@app/pages/interfaces/Subject.interface';

@Component({
  selector: 'app-subjects',
  templateUrl: './subjects.component.html',
  styleUrls: ['./subjects.component.scss']
})
export class SubjectsComponent implements OnInit {

  public subjects: SubjectInterface[] = [];

  constructor(private subjectsService: SubjectsService) { }

  ngOnInit(): void {
    this.subjectsService.getAll().subscribe((subjects: SubjectInterface[]) => {
      console.log(subjects);
      this.subjects.push(...subjects);
    });
  }

}
