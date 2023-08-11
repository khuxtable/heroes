import { Component, ViewChild } from '@angular/core';
import { Table } from 'primeng/table';
import { TableLazyLoadEvent } from 'primeng/table';
import { Router } from '@angular/router';

import { Hero } from '@appModel/hero';
import { UIFilter, UIFilterSort, UIFilterMetadata, UIFilterResult } from '@appModel/ui.filter';
import { HeroService } from '@appServices/hero.service';
import { AuthService } from '@appServices/auth.service';

@Component({
	selector: 'app-heroes',
	templateUrl: './heroes.component.html',
	styleUrls: ['./heroes.component.css']
})
export class HeroesComponent {

	heroes: Hero[] = [];
	selectedHeroes: Hero[] = [];
	canAdd: boolean = false;
	loading: boolean = true;
	totalRecords: number = 0;

	@ViewChild('dt')
	dt: Table | undefined;

	constructor(private router: Router,
				private heroService: HeroService,
				private authService: AuthService) { }

	ngOnInit(): void {
		this.getUser();
		this.loading = true;
	}

	applyFilterGlobal($event: any, stringVal: any) {
		this.dt!.filterGlobal(($event.target as HTMLInputElement).value, stringVal);
	}

	loadHeroes(event: TableLazyLoadEvent) {
		this.loading = true;

		setTimeout(() => {
			this.heroService.getHeroesLazy(new UIFilter(event)).subscribe(res => {
				this.heroes = res.heroes;
				this.totalRecords = res.totalRecords;
				this.loading = false;
			})
		}, 1000);
	}

	getUser(): void {
		if (this.authService.userValue) {
			this.canAdd = this.authService.userValue.privileges.includes('ADMIN');
		} else {
			this.canAdd = false;
		}
	}

	add(): void {
		this.router.navigate(['/detail/new']);
	}

	delete(hero: Hero): void {
		this.heroes = this.heroes.filter(h => h !== hero);
		this.heroService.deleteHero(hero.id).subscribe();
	}
}
