import { Component } from '@angular/core';
import { Router } from '@angular/router';

import { SessionService } from '@shared/services/session.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent {

  readonly labelsForInterface = {
    logout: 'Se déconnecter',
    posts: 'Articles',
    subjects: 'Thèmes'
  }

  constructor(private router: Router, private sessionService: SessionService) { }

  get isLoginOrRegisterPage(): boolean {
    return this.router.url === '/register' || this.router.url === '/login';
  }

  logout(): void {
    this.sessionService.logout();
    this.router.navigate(['/login']);
  }
}
