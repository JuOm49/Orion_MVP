import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";

import { SubjectsComponent } from "@pages/shared/components/subjects/subjects.component";
import { SubjectsSharedModule } from "./subjects-shared.module";

const routes: Routes = [
    {path: '', component: SubjectsComponent}
];

@NgModule({
    imports: [
        RouterModule.forChild(routes),
        SubjectsSharedModule
    ]
})
export class SubjectsModule {}