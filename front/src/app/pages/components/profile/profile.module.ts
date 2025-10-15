import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";

import { SharedPagesModule } from "@pages/shared/shared-pages.module";
import { ProfileComponent } from "@pages/components/profile/profile.component";

const routes: Routes = [
    {path: '', component: ProfileComponent}
];

@NgModule({
    declarations: [ProfileComponent],
    imports: [
        RouterModule.forChild(routes),
        SharedPagesModule
    ],
    exports: []
})
export class ProfileModule {}