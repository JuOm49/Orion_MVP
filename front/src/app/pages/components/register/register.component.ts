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

  mainForm!: FormGroup;

  constructor(private formBuilder: FormBuilder, private authService: AuthService, private router: Router, private sessionService: SessionService) {}

  ngOnInit(): void {
    this.initFormControls();
  }

  //Methods
  private initFormControls(): void {    
    this.mainForm = this.formBuilder.group({
      name: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password:['', [
        Validators.required,
        Validators.minLength(8),
        Validators.pattern(/^(?=.*[A-Z])(?=.*[a-z])(?=.*\d)(?=.*[;,:/'(){}<>§*µ£€@#$%^&+=!]).{8,}$/)
      ]]
    });
  }

  onSubmitForm(): void {
    if (this.mainForm.valid) {
      const registerRequest = this.mainForm.value as RegisterRequest;
      this.authService.saveNewUser(registerRequest).pipe(take(1)).subscribe((response: AuthSuccess) => {
        localStorage.setItem('token', response.token);
        this.authService.getUser().pipe(take(1)).subscribe((user: User) => {
          this.sessionService.login(user);
          this.router.navigate(['/posts']);
        });
      },
      (error) => {
        console.error('Error during registration:', error);
      });
    }
  }

}
