import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";

import { SubjectsComponent } from "@pages/shared/components/subjects/subjects.component";
import { SharedPagesModule } from "@pages/shared/shared-pages.module";

@NgModule({
    declarations: [SubjectsComponent],
    imports: [CommonModule, SharedPagesModule],
    exports: [SubjectsComponent]
})
export class SubjectsSharedModule {}