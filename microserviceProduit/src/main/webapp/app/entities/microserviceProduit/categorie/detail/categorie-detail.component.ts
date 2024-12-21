import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { ICategorie } from '../categorie.model';

@Component({
  standalone: true,
  selector: 'gdil-categorie-detail',
  templateUrl: './categorie-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class CategorieDetailComponent {
  categorie = input<ICategorie | null>(null);

  previousState(): void {
    window.history.back();
  }
}
