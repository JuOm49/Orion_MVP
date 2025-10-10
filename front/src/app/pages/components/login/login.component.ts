import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { User } from '@app/core/interfaces/user.interface';
import { AuthSuccess } from '@app/pages/interfaces/AuthSuccess.interface';
import { LoginRequest } from '@app/pages/interfaces/LoginRequest.interface';
import { RegisterRequest } from '@app/pages/interfaces/RegisterRequest.interface';
import { AuthService } from '@app/pages/services/auth.service';
import { SessionService } from '@app/shared/services/session.service';
import { take } from 'rxjs';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  mainForm!: FormGroup;
  
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
