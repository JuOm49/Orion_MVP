import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

// consider a guard combined with canLoad / canActivate route option
// to manage unauthenticated user to access private routes
const routes: Routes = [
  { path: 'register', loadChildren: () => import('./pages/components/register/register.module').then(m => m.RegisterModule) },
  { path: 'login', loadChildren: () => import('./pages/components/login/login.module').then(m => m.LoginModule) },
  { path: 'posts/:id', loadChildren: () => import('./pages/components/post/post.module').then(m => m.PostModule) },
  { path: 'posts', loadChildren: () => import('./pages/components/posts/posts.module').then(m => m.PostsModule) },
  { path: '', loadChildren: () => import('./pages/components/home/home.module').then(m => m.HomeModule) }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
