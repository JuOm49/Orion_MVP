import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";

import { PostComponent } from "@pages/components/post/post.component";
import { SharedPagesModule } from "@pages/shared/shared-pages.module";

const routes: Routes = [
    { path: '', component: PostComponent}
];

NgModule({
    declarations: [PostComponent],
    imports: [
        RouterModule.forChild(routes),
        SharedPagesModule
    ],
    exports: []
})
export class PostModule {}