import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICategorie } from 'app/entities/microserviceProduit/categorie/categorie.model';
import { CategorieService } from 'app/entities/microserviceProduit/categorie/service/categorie.service';
import { IProduit } from '../produit.model';
import { ProduitService } from '../service/produit.service';
import { ProduitFormGroup, ProduitFormService } from './produit-form.service';

@Component({
  standalone: true,
  selector: 'gdil-produit-update',
  templateUrl: './produit-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ProduitUpdateComponent implements OnInit {
  isSaving = false;
  produit: IProduit | null = null;

  categoriesSharedCollection: ICategorie[] = [];

  protected produitService = inject(ProduitService);
  protected produitFormService = inject(ProduitFormService);
  protected categorieService = inject(CategorieService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ProduitFormGroup = this.produitFormService.createProduitFormGroup();

  compareCategorie = (o1: ICategorie | null, o2: ICategorie | null): boolean => this.categorieService.compareCategorie(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ produit }) => {
      this.produit = produit;
      if (produit) {
        this.updateForm(produit);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const produit = this.produitFormService.getProduit(this.editForm);
    if (produit.id !== null) {
      this.subscribeToSaveResponse(this.produitService.update(produit));
    } else {
      this.subscribeToSaveResponse(this.produitService.create(produit));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProduit>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(produit: IProduit): void {
    this.produit = produit;
    this.produitFormService.resetForm(this.editForm, produit);

    this.categoriesSharedCollection = this.categorieService.addCategorieToCollectionIfMissing<ICategorie>(
      this.categoriesSharedCollection,
      produit.categorie,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.categorieService
      .query()
      .pipe(map((res: HttpResponse<ICategorie[]>) => res.body ?? []))
      .pipe(
        map((categories: ICategorie[]) =>
          this.categorieService.addCategorieToCollectionIfMissing<ICategorie>(categories, this.produit?.categorie),
        ),
      )
      .subscribe((categories: ICategorie[]) => (this.categoriesSharedCollection = categories));
  }
}
