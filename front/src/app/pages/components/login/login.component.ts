import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';

import { take } from 'rxjs';

import { User } from '@core/interfaces/user.interface';

import { AuthSuccess } from '@pages/interfaces/AuthSuccess.interface';
import { LoginRequest } from '@pages/interfaces/LoginRequest.interface';
import { AuthService } from '@pages/services/auth.service';

import { SessionService } from '@shared/services/session.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  mainForm!: FormGroup;
  readonly labelsForInterface = {
    login: 'se connecter',
    identifier: 'E-mail ou nom d\'utilisateur',
    password: 'Mot de passe'
  }
  
  constructor(private formBuilder: FormBuilder, private authService: AuthService, private router: Router, private sessionService: SessionService) {}

  ngOnInit(): void {
    this.initFormControls();
  }

  //Methods
    private initFormControls(): void {    
      this.mainForm = this.formBuilder.group({
        identifier: ['', Validators.required],
        password:['', [
          Validators.required,
          Validators.minLength(8),
          Validators.pattern(/^(?=.*[A-Z])(?=.*[a-z])(?=.*\d)(?=.*[;,:/'(){}<>§*µ£€@#$%^&+=!]).{8,}$/)
        ]]
      });
    }
  
    onSubmitForm(): void {
      if (this.mainForm.valid) {
        const loginRequest = this.mainForm.value as LoginRequest;
        this.authService.loginUser(loginRequest).pipe(take(1)).subscribe((response: AuthSuccess) => {
          localStorage.setItem('token', response.token);
          this.authService.getUser().pipe(take(1)).subscribe((user: User) => {
            this.sessionService.login(user);
            this.router.navigate(['/posts']);
          });
        },
        (error) => {
          console.error('Error during login:', error);
        });
      }
    }
}
