import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";

import { SubjectsComponent } from "@pages/components/subjects/subjects.component";
import { SharedPagesModule } from "@pages/shared/shared-pages.module";

const routes: Routes = [
    {path: '', component: SubjectsComponent}
];

@NgModule({
    declarations: [SubjectsComponent],
    imports: [
        RouterModule.forChild(routes),
        SharedPagesModule
    ],
    exports: []
})
export class SubjectsModule {}