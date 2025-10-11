import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { ReactiveFormsModule } from "@angular/forms";
import { RouterModule } from "@angular/router";

import { AuthHeaderComponent } from '@pages/shared/components/auth-header/auth-header.component';

import { SharedMaterialModule } from "@shared/material.module";

@NgModule({
    declarations: [
    AuthHeaderComponent
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
        AuthHeaderComponent
    ]
})
export class SharedPagesModule {}