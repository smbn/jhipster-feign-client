import { ICategorie, NewCategorie } from './categorie.model';

export const sampleWithRequiredData: ICategorie = {
  id: 20744,
  nomCategorie: 'aussitôt que',
};

export const sampleWithPartialData: ICategorie = {
  id: 23717,
  nomCategorie: 'jeune enfant glouglou',
};

export const sampleWithFullData: ICategorie = {
  id: 2729,
  nomCategorie: 'maigre hé miam',
};

export const sampleWithNewData: NewCategorie = {
  nomCategorie: 'sauvegarder',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
