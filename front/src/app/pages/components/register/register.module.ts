import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";

import { RegisterComponent } from "@pages/components/register/register.component";
import { SharedPagesModule } from "@pages/shared-pages.module";

const routes: Routes = [
    { path: '', component: RegisterComponent }
];

@NgModule({
    declarations: [RegisterComponent],
    imports: [
        RouterModule.forChild(routes),
        SharedPagesModule
    ],
    exports: [],
    providers: [],
})
export class RegisterModule {}