import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";

import { SharedPagesModule } from "@pages/shared/shared-pages.module";
import { ProfileComponent } from "@pages/components/profile/profile.component";
import { SubjectsSharedModule } from "@pages/shared/components/subjects/subjects-shared.module";

const routes: Routes = [
    {path: '', component: ProfileComponent}
];

@NgModule({
    declarations: [ProfileComponent],
    imports: [
        RouterModule.forChild(routes),
        SharedPagesModule,
        SubjectsSharedModule
    ],
    exports: []
})
export class ProfileModule {}