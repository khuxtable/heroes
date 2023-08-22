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

import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { AppRoutingModule } from '@app/app-routing.module';
import { AppComponent } from '@app/app.component';
import { ProfileComponent } from '@appComponents//profile/profile.component';
import { DashboardComponent } from '@appComponents/dashboard/dashboard.component';
import { HeroDetailComponent } from '@appComponents/hero-detail/hero-detail.component';
import { HeroSearchComponent } from '@appComponents/hero-search/hero-search.component';
import { HeroesComponent } from '@appComponents/heroes/heroes.component';
import { LoginComponent } from '@appComponents/login/login.component';
import { MessagesComponent } from '@appComponents/messages/messages.component';
import { AvatarModule } from "primeng/avatar";
import { ButtonModule } from 'primeng/button';
import { DataViewModule } from 'primeng/dataview';
import { DropdownModule } from 'primeng/dropdown';
import { InputTextModule } from 'primeng/inputtext';
import { ListboxModule } from 'primeng/listbox';
import { OverlayPanelModule } from "primeng/overlaypanel";
import { PasswordModule } from 'primeng/password';
import { RatingModule } from 'primeng/rating';
import { SelectButtonModule } from 'primeng/selectbutton';
import { TableModule } from 'primeng/table';
import { ToolbarModule } from 'primeng/toolbar';

@NgModule({
	declarations: [
		AppComponent,
		HeroesComponent,
		HeroDetailComponent,
		MessagesComponent,
		DashboardComponent,
		HeroSearchComponent,
		LoginComponent,
		ProfileComponent,
	],
	imports: [
		BrowserAnimationsModule,
		BrowserModule,
		CommonModule,
		DropdownModule,
		FormsModule,
		AppRoutingModule,
		HttpClientModule,
		ToolbarModule,
		ButtonModule,
		DataViewModule,
		TableModule,
		InputTextModule,
		PasswordModule,
		ListboxModule,
		RatingModule,
		SelectButtonModule,
		AvatarModule,
		OverlayPanelModule,
	],
	providers: [
//		[
//			{ provide: HTTP_INTERCEPTORS, useClass: XhrHeaderInterceptor, multi:true }
//		]
	],
	bootstrap: [AppComponent]
})
export class AppModule {
}
