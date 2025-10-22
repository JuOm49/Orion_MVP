import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';

import { take } from 'rxjs';

import { User } from '@core/interfaces/user.interface';

import { AuthSuccess } from '@pages/interfaces/AuthSuccess.interface';
import { RegisterRequest } from '@pages/interfaces/RegisterRequest.interface';
import { AuthService } from '@pages/services/auth.service';

import { SessionService } from '@shared/services/session.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html'
})
export class ProfileComponent implements OnInit {

  profileForm!: FormGroup;
  invalidCredentials: boolean = false;

  readonly labelsForInterface = {
    profile: 'Profil utilisateur',
    subscribes: 'Abonnements',
    save: 'Sauvegarder',
    unsubscribe: 'Se désabonner',
    ph_name: 'Nom d\'utilisateur',
    ph_email: 'E-mail',
    ph_password: 'Mot de passe',
    invalidCredentials: 'La modification a échouée'
  }

  constructor(private formBuilder: FormBuilder, private authService: AuthService, private router: Router, private sessionService: SessionService) { }

  ngOnInit(): void {
    this.authService.getUser().pipe(take(1)).subscribe({
      next: (user: User) => {
        this.sessionService.login(user);
        this.profileForm.patchValue({
          name: user.name,
          email: user.email
        });
      },
      error: (error) => {
        console.error('Error getting user:', error);
      }
    });
    this.initProfileForm();
  }

  //Methods
    onSubmitForm(): void {
      if (this.profileForm.valid) {
        const registerRequest = this.profileForm.value as RegisterRequest;
        this.authService.updateUser(registerRequest).pipe(take(1)).subscribe({
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
      this.profileForm.reset();
    }

    getFieldError(fieldName: 'name' | 'email' | 'password'): string | null {
    const field = this.profileForm.get(fieldName);
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

    private initProfileForm(): void {
      this.profileForm = this.formBuilder.group({
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
