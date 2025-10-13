import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { ReactiveFormsModule } from "@angular/forms";
import { RouterModule } from "@angular/router";

import { AuthHeaderComponent } from '@pages/shared/components/auth-header/auth-header.component';
import { InvalidCredentialsComponent } from "@pages/shared/components/invalid-credentials/invalid-credentials.component";


import { SharedMaterialModule } from "@shared/material.module";

@NgModule({
    declarations: [
    AuthHeaderComponent,
    InvalidCredentialsComponent
  ],
    imports: [
        CommonModule,
        ReactiveFormsModule,
        RouterModule,
        SharedMaterialModule
    ],
    exports: [
        CommonModule,
        ReactiveFormsModule,
        RouterModule,
        SharedMaterialModule,
        AuthHeaderComponent,
        InvalidCredentialsComponent
    ]
})
export class SharedPagesModule {}