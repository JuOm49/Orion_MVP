import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";
import { AuthService } from "@app/pages/services/auth.service";

import { RegisterComponent } from "@pages/components/register/register.component";
import { SharedPagesModule } from "@pages/shared/shared-pages.module";

const routes: Routes = [
    { path: '', component: RegisterComponent }
];

@NgModule({
    declarations: [RegisterComponent],
    imports: [
        RouterModule.forChild(routes),
        SharedPagesModule
    ],
    providers: [
        AuthService
    ],
})
export class RegisterModule {}