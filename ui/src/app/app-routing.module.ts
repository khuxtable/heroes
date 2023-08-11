import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DashboardComponent } from '@appComponents/dashboard/dashboard.component';
import { HeroesComponent } from '@appComponents/heroes/heroes.component';
import { HeroDetailComponent } from '@appComponents/hero-detail/hero-detail.component';
import { LoginComponent } from '@appComponents/login/login.component';
import { AuthGuard } from '@appGuards/auth.guard';

const routes: Routes = [
	{ path: '', redirectTo: '/dashboard', pathMatch: 'full' },
	{ path: 'dashboard', component: DashboardComponent, canActivate: [AuthGuard] },
	{ path: 'heroes', component: HeroesComponent, canActivate: [AuthGuard] },
	{ path: 'detail/new', component: HeroDetailComponent, canActivate: [AuthGuard] },
	{ path: 'detail/:id', component: HeroDetailComponent, canActivate: [AuthGuard] },
	{ path: 'login', component: LoginComponent },
];

@NgModule({
	imports: [RouterModule.forRoot(routes)],
	exports: [RouterModule]
})
export class AppRoutingModule { }