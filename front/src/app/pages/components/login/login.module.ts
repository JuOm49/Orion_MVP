import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Routes, RouterModule } from '@angular/router';

import { LoginComponent } from '@pages/components/login/login.component';
import { SharedPagesModule } from '@pages/shared/shared-pages.module';

const routes: Routes = [
  { path: '', component: LoginComponent }
]

@NgModule({
  declarations: [LoginComponent],
  imports: [
    RouterModule.forChild(routes),
    CommonModule,
    SharedPagesModule
  ]
})
export class LoginModule { }
