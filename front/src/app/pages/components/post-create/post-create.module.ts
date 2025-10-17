import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";

import { PostCreateComponent } from "@pages/components/post-create/post-create.component";
import { SharedPagesModule } from "@pages/shared/shared-pages.module";

const routes: Routes = [
    {path: '', component: PostCreateComponent}
];

@NgModule({
    declarations: [PostCreateComponent],
    imports: [
        RouterModule.forChild(routes),
        SharedPagesModule
    ],
    exports: []
})
export class PostCreateModule {}