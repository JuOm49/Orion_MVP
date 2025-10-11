import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";

import { HomeComponent } from "@pages/components/home/home.component";
import { SharedPagesModule } from "@pages/shared/shared-pages.module";

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