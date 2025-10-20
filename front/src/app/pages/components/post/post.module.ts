import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";
import { FormsModule } from "@angular/forms";

import { PostComponent } from "@pages/components/post/post.component";
import { SharedPagesModule } from "@pages/shared/shared-pages.module";

const routes: Routes = [
    { path: '', component: PostComponent}
];

@NgModule({
    declarations: [PostComponent],
    imports: [
        RouterModule.forChild(routes),
        SharedPagesModule,
        FormsModule
    ],
    exports: []
})
export class PostModule {}