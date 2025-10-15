import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { SessionService } from '@app/shared/services/session.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {

  readonly labelsForInterface = {
    logout: 'Se déconnecter',
    posts: 'Articles',
    subjects: 'Thèmes'
  }

  constructor(private router: Router, private sessionService: SessionService) { }

  ngOnInit(): void {
  }

  get isLoginOrRegisterPage(): boolean {
    return this.router.url === '/register' || this.router.url === '/login';
  }

  logout(): void {
    this.sessionService.logout();
    this.router.navigate(['/login']);
  }
}
