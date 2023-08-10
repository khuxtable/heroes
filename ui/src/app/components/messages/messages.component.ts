import { Component } from '@angular/core';
import { ButtonModule } from 'primeng/button';

import { MessageService } from '@appServices/message.service';

@Component({
	selector: 'app-messages',
	templateUrl: './messages.component.html',
	styleUrls: ['./messages.component.css']
})
export class MessagesComponent {

	constructor(public messageService: MessageService) { }
}
