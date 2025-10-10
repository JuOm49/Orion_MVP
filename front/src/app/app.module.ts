import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { HeaderModule } from '@core/components/header/hearder.module';

import { AppRoutingModule } from '@app/app-routing.module';
import { AppComponent } from '@app/app.component';

import { JwtInterceptor } from '@shared/interceptors/jwt.interceptor';

@NgModule({
  declarations: [AppComponent],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    HeaderModule,
    HttpClientModule
],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true }
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
