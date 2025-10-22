import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-invalid-credentials',
  templateUrl: './invalid-credentials.component.html'
})
export class InvalidCredentialsComponent {
  @Input() invalidCredentialsMessage!: string;
}
