import {Component, Input} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Location} from '@angular/common';

import {Hero} from '@appModel/hero';
import {HeroService} from '@appServices/hero.service';
import {AuthService} from '@appServices/auth.service';

@Component({
    selector: 'app-hero-detail',
    templateUrl: './hero-detail.component.html',
    styleUrls: ['./hero-detail.component.css']
})
export class HeroDetailComponent {

    hero: Hero | undefined;
    editable: boolean = false;

    powers = [
        'Really Smart',
        'Super Flexible',
        'Super Hot',
        'Weather Changer',
    ];

    constructor(
        private route: ActivatedRoute,
        private heroService: HeroService,
        private authService: AuthService,
        private location: Location
    ) {
    }

    ngOnInit(): void {
        this.getHero();
        this.getUser();
    }

    getHero(): void {
        let idParam = this.route.snapshot.paramMap.get('id');
        if (!idParam) {
            this.hero = { id: 0, name: '', power: '', rating: 0};
        } else {
            const id = Number(idParam);
            this.heroService.getHero(id)
                .subscribe(hero => this.hero = hero);
        }
    }

    getUser(): void {
        if (this.authService.userValue) {
            this.editable = this.authService.userValue.privileges.includes('ADMIN');
        } else {
            this.editable = false;
        }
    }

    save(): void {
        if (this.hero) {
            if (this.hero.id == 0) {
                this.heroService.addHero(this.hero)
                    .subscribe(() => this.goBack());
            } else {
                this.heroService.updateHero(this.hero)
                    .subscribe(() => this.goBack());
            }
        }
    }

    goBack(): void {
        this.location.back();
    }
}
