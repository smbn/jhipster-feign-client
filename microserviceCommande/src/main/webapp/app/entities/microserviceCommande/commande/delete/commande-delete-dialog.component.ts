import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ICommande } from '../commande.model';
import { CommandeService } from '../service/commande.service';

@Component({
  standalone: true,
  templateUrl: './commande-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class CommandeDeleteDialogComponent {
  commande?: ICommande;

  protected commandeService = inject(CommandeService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.commandeService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
