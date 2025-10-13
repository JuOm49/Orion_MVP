import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { MatMenuModule } from "@angular/material/menu";
import { MatIconModule } from "@angular/material/icon";
import { RouterLink } from "@angular/router";
import { RouterLinkActive } from "@angular/router";

import { HeaderComponent } from "@core/components/header/header.component";

@NgModule({
    declarations: [HeaderComponent],
    imports: [CommonModule, MatMenuModule, MatIconModule, RouterLink, RouterLinkActive],
    exports: [HeaderComponent]
})
export class HeaderModule {}