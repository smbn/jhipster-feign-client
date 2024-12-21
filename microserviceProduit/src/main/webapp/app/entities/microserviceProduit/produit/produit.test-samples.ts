import { IProduit, NewProduit } from './produit.model';

export const sampleWithRequiredData: IProduit = {
  id: 3269,
  nomProduit: 'proche de',
  prixProduit: 11644.2,
};

export const sampleWithPartialData: IProduit = {
  id: 5010,
  nomProduit: 'clac',
  descriptionProduit: 'miam zzzz comment',
  prixProduit: 19713.98,
};

export const sampleWithFullData: IProduit = {
  id: 21996,
  nomProduit: 'vlan',
  descriptionProduit: 'de manière à ce que',
  prixProduit: 32371.48,
  imageProduit: 'avant que',
};

export const sampleWithNewData: NewProduit = {
  nomProduit: 'rose loin si',
  prixProduit: 10726.07,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
