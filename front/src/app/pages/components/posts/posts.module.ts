import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";

import { PostsComponent } from "@pages/components/posts/posts.component";
import { SharedPagesModule } from "@pages/shared/shared-pages.module";

const routes: Routes = [
    {path: '', component: PostsComponent}
];

@NgModule({
    declarations: [PostsComponent],
    imports: [
        RouterModule.forChild(routes),
        SharedPagesModule
    ],
    exports: []
})
export class PostsModule {}