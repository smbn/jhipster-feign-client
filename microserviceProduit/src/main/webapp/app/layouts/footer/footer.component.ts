import { Component } from '@angular/core';
import { TranslateDirective } from 'app/shared/language';

@Component({
  standalone: true,
  selector: 'gdil-footer',
  templateUrl: './footer.component.html',
  imports: [TranslateDirective],
})
export default class FooterComponent {}
