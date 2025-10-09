import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '@app/pages/services/auth.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {

  mainForm!: FormGroup;

  constructor(private formBuilder: FormBuilder, private authService: AuthService) {}

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
      this.authService.saveNewUser(this.mainForm.value).subscribe((response) => {
        console.log(response);
      });
    }
  }

}
