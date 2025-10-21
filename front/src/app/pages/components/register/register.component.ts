import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';

import { take } from 'rxjs';

import { User } from '@core/interfaces/user.interface';

import { RegisterRequest } from '@pages/interfaces/RegisterRequest.interface';
import { AuthService } from '@pages/services/auth.service';
import { AuthSuccess } from '@pages/interfaces/AuthSuccess.interface';
import { SessionService } from '@app/shared/services/session.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {

  registerForm!: FormGroup;
  invalidCredentials: boolean = false;

  readonly labelsForInterface = {
    title: 'Inscription',
    register: 's\'inscrire',
    name: 'Nom d\'utilisateur',
    email: 'E-mail',
    password: 'Mot de passe',
    invalidCredentials: 'Inscription impossible avec ces identifiants'
  }

  constructor(private formBuilder: FormBuilder, private authService: AuthService, private router: Router, private sessionService: SessionService) {}

  ngOnInit(): void {
    this.initRegisterForm();
  }

  //Methods
  onSubmitForm(): void {
    if (this.registerForm.valid) {
      const registerRequest = this.registerForm.value as RegisterRequest;
      this.authService.saveUser(registerRequest).pipe(take(1)).subscribe({
        next: (response: AuthSuccess) => {
          localStorage.setItem('token', response.token);
          this.authService.getUser().pipe(take(1)).subscribe({
            next: (user: User) => {
              this.sessionService.login(user);
              this.router.navigate(['/posts']);
            },
            error: (error) => {
              console.error('Error getting user:', error);
            }
          });
        },
        error: (error) => {
          console.error('Error during registration:', error);
          if (error.status === 401 || error.status === 403) {
            this.invalidCredentials = true;
          } 
        }
      });
    }
    this.registerForm.reset();
  }

  getFieldError(fieldName: 'name' | 'email' | 'password'): string | null {
    const field = this.registerForm.get(fieldName);
    if (!field || !field.touched) return null;

    if (field.hasError('required')) {
      switch (fieldName) {
        case 'name':
          return "Le nom d'utilisateur est requis";
        case 'email':
          return "L'email est requis";
        case 'password':
          return "Le mot de passe est requis";
      }
    }
    if (fieldName === 'name' && field.hasError('maxlength')) {
      return "Le nom d'utilisateur ne doit pas dépasser 80 caractères";
    }
    if (fieldName === 'email') {
      if (field.hasError('email')) return "L'email n'est pas valide";
      if (field.hasError('maxlength')) return "L'email ne doit pas dépasser 255 caractères";
    }
    if (fieldName === 'password') {
      if (field.hasError('minlength')) return "Le mot de passe doit contenir au moins 8 caractères";
      if (field.hasError('pattern')) return "Le mot de passe doit contenir au moins une majuscule, une minuscule, un chiffre et un caractère spécial";
    }
    return null;
  }

  private initRegisterForm(): void {
    this.registerForm = this.formBuilder.group({
      name: ['', [Validators.required, Validators.maxLength(80)]],
      email: ['', [Validators.required, Validators.email, Validators.maxLength(255)]],
      password: ['', [
        Validators.required,
        Validators.minLength(8),
        Validators.pattern(/^(?=.*[A-Z])(?=.*[a-z])(?=.*\d)(?=.*[;,:/'(){}<>§*µ£€@#$%^&+=!]).{8,}$/)
      ]]
    });
  }
}
