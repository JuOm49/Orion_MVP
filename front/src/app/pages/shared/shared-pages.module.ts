import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { ReactiveFormsModule } from "@angular/forms";
import { RouterModule } from "@angular/router";

import { TitleComponent } from '@pages/shared/components/title/title.component';
import { InvalidCredentialsComponent } from "@pages/shared/components/invalid-credentials/invalid-credentials.component";


import { SharedMaterialModule } from "@shared/material.module";

@NgModule({
    declarations: [
    TitleComponent,
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
        TitleComponent,
        InvalidCredentialsComponent
    ]
})
export class SharedPagesModule {}