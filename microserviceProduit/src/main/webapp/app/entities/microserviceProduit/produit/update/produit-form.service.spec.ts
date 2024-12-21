import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../produit.test-samples';

import { ProduitFormService } from './produit-form.service';

describe('Produit Form Service', () => {
  let service: ProduitFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ProduitFormService);
  });

  describe('Service methods', () => {
    describe('createProduitFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createProduitFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nomProduit: expect.any(Object),
            descriptionProduit: expect.any(Object),
            prixProduit: expect.any(Object),
            imageProduit: expect.any(Object),
            categorie: expect.any(Object),
          }),
        );
      });

      it('passing IProduit should create a new form with FormGroup', () => {
        const formGroup = service.createProduitFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nomProduit: expect.any(Object),
            descriptionProduit: expect.any(Object),
            prixProduit: expect.any(Object),
            imageProduit: expect.any(Object),
            categorie: expect.any(Object),
          }),
        );
      });
    });

    describe('getProduit', () => {
      it('should return NewProduit for default Produit initial value', () => {
        const formGroup = service.createProduitFormGroup(sampleWithNewData);

        const produit = service.getProduit(formGroup) as any;

        expect(produit).toMatchObject(sampleWithNewData);
      });

      it('should return NewProduit for empty Produit initial value', () => {
        const formGroup = service.createProduitFormGroup();

        const produit = service.getProduit(formGroup) as any;

        expect(produit).toMatchObject({});
      });

      it('should return IProduit', () => {
        const formGroup = service.createProduitFormGroup(sampleWithRequiredData);

        const produit = service.getProduit(formGroup) as any;

        expect(produit).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IProduit should not enable id FormControl', () => {
        const formGroup = service.createProduitFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewProduit should disable id FormControl', () => {
        const formGroup = service.createProduitFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
