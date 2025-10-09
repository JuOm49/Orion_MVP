import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";

import { SharedPagesModule } from "@pages/shared-pages.module";
import { HomeComponent } from "@pages/components/home/home.component";

const routes: Routes = [
    { path: '', component: HomeComponent }
];

@NgModule({
    declarations: [HomeComponent],
    imports: [
        RouterModule.forChild(routes),
        SharedPagesModule
    ],
    exports: [],
    providers: [],
})
export class HomeModule {}