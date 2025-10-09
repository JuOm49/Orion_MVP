import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { ReactiveFormsModule } from "@angular/forms";

import { SharedMaterialModule } from "@app/shared/material.module";

@NgModule({
    declarations: [],
    imports: [
        CommonModule,
        ReactiveFormsModule,
        SharedMaterialModule
    ],
    exports: [
        CommonModule,
        ReactiveFormsModule,
        SharedMaterialModule
    ]
})
export class SharedPagesModule {}