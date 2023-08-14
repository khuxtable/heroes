/*
 * Copyright 2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

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