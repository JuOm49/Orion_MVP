import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-auth-header',
  templateUrl: './auth-header.component.html'
})
export class AuthHeaderComponent {

  @Input() title!: string;

}
