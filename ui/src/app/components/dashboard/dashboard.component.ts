import { Component } from '@angular/core';
import { Router } from '@angular/router';

import { Hero } from '@appModel/hero';
import { HeroService } from '@appServices/hero.service';
import { UIFilter } from '@appModel/ui.filter';

@Component({
	selector: 'app-dashboard',
	templateUrl: './dashboard.component.html',
	styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent {

	heroes: Hero[] = [];

	constructor(
        private router: Router,
        private heroService: HeroService) { }
	
	ngOnInit(): void {
		this.getHeroes();
	}

	getHeroes(): void {
		this.heroService.getTopHeroes()
			.subscribe(result => this.heroes = result);
	}
	
	routeMe(id : number) : void {
		this.router.navigate(['/detail/' + id]);
	}
}
