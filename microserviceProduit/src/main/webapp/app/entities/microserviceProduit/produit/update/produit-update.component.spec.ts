import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ICategorie } from 'app/entities/microserviceProduit/categorie/categorie.model';
import { CategorieService } from 'app/entities/microserviceProduit/categorie/service/categorie.service';
import { ProduitService } from '../service/produit.service';
import { IProduit } from '../produit.model';
import { ProduitFormService } from './produit-form.service';

import { ProduitUpdateComponent } from './produit-update.component';

describe('Produit Management Update Component', () => {
  let comp: ProduitUpdateComponent;
  let fixture: ComponentFixture<ProduitUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let produitFormService: ProduitFormService;
  let produitService: ProduitService;
  let categorieService: CategorieService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ProduitUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(ProduitUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProduitUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    produitFormService = TestBed.inject(ProduitFormService);
    produitService = TestBed.inject(ProduitService);
    categorieService = TestBed.inject(CategorieService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Categorie query and add missing value', () => {
      const produit: IProduit = { id: 456 };
      const categorie: ICategorie = { id: 27682 };
      produit.categorie = categorie;

      const categorieCollection: ICategorie[] = [{ id: 17310 }];
      jest.spyOn(categorieService, 'query').mockReturnValue(of(new HttpResponse({ body: categorieCollection })));
      const additionalCategories = [categorie];
      const expectedCollection: ICategorie[] = [...additionalCategories, ...categorieCollection];
      jest.spyOn(categorieService, 'addCategorieToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ produit });
      comp.ngOnInit();

      expect(categorieService.query).toHaveBeenCalled();
      expect(categorieService.addCategorieToCollectionIfMissing).toHaveBeenCalledWith(
        categorieCollection,
        ...additionalCategories.map(expect.objectContaining),
      );
      expect(comp.categoriesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const produit: IProduit = { id: 456 };
      const categorie: ICategorie = { id: 16345 };
      produit.categorie = categorie;

      activatedRoute.data = of({ produit });
      comp.ngOnInit();

      expect(comp.categoriesSharedCollection).toContain(categorie);
      expect(comp.produit).toEqual(produit);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProduit>>();
      const produit = { id: 123 };
      jest.spyOn(produitFormService, 'getProduit').mockReturnValue(produit);
      jest.spyOn(produitService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ produit });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: produit }));
      saveSubject.complete();

      // THEN
      expect(produitFormService.getProduit).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(produitService.update).toHaveBeenCalledWith(expect.objectContaining(produit));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProduit>>();
      const produit = { id: 123 };
      jest.spyOn(produitFormService, 'getProduit').mockReturnValue({ id: null });
      jest.spyOn(produitService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ produit: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: produit }));
      saveSubject.complete();

      // THEN
      expect(produitFormService.getProduit).toHaveBeenCalled();
      expect(produitService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProduit>>();
      const produit = { id: 123 };
      jest.spyOn(produitService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ produit });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(produitService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareCategorie', () => {
      it('Should forward to categorieService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(categorieService, 'compareCategorie');
        comp.compareCategorie(entity, entity2);
        expect(categorieService.compareCategorie).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
