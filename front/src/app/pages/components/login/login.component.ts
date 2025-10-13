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
  invalidCredentials: boolean = false;

  readonly labelsForInterface = {
    login: 'se connecter',
    identifier: 'E-mail ou nom d\'utilisateur',
    password: 'Mot de passe',
    invalidCredentials: 'Identifiant ou mot de passe incorrect'
  }
  
  constructor(private formBuilder: FormBuilder, private authService: AuthService, private router: Router, private sessionService: SessionService) {}

  ngOnInit(): void {
    this.initFormControls();
  }

  //Methods
  onSubmitForm(): void {
      if (this.mainForm.valid) {
        const loginRequest = this.mainForm.value as LoginRequest;
        this.authService.loginUser(loginRequest).pipe(take(1)).subscribe({
          next: (response: AuthSuccess) => {
          localStorage.setItem('token', response.token);
          this.authService.getUser().pipe(take(1)).subscribe({
            next: (user: User) => {
            this.sessionService.login(user);
            this.router.navigate(['/posts']);
          },
            error: (error) => {
              console.error('Error getting userLogin:', error);
            }
        });
        },
        error: (error) => {
          console.error('Error during login:', error);
          if (error.status === 401 || error.status === 403) {
            this.invalidCredentials = true;
          }
        }
      });
      }
    }

    getFieldError(fieldName: 'identifier' | 'password'): string | null {
      const field = this.mainForm.get(fieldName);
      if (field && field.errors && (field.touched)) {
        const errors = field.errors;
        
        if (errors['required']) {
          return fieldName === 'identifier' ? 
            'L\'identifiant est requis' : 
            'Le mot de passe est requis';
        }

        if(errors['maxlength']) {
          return 'L\'identifiant ne doit pas dépasser 255 caractères';
        }
      }
      return null;
    }

    private initFormControls(): void {    
      this.mainForm = this.formBuilder.group({
        identifier: ['', [
          Validators.required,
          Validators.maxLength(255)
        ]],
        password:['', [
          Validators.required
        ]]
      });
    }
}
