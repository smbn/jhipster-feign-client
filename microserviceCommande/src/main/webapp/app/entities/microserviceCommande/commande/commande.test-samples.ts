import dayjs from 'dayjs/esm';

import { ICommande, NewCommande } from './commande.model';

export const sampleWithRequiredData: ICommande = {
  id: 5992,
  clientId: 29240,
  produitId: 14381,
  dateCommande: dayjs('2024-12-19T17:37'),
};

export const sampleWithPartialData: ICommande = {
  id: 2891,
  clientId: 17194,
  produitId: 4416,
  dateCommande: dayjs('2024-12-19T10:47'),
};

export const sampleWithFullData: ICommande = {
  id: 11501,
  clientId: 21851,
  produitId: 4868,
  dateCommande: dayjs('2024-12-19T18:10'),
};

export const sampleWithNewData: NewCommande = {
  clientId: 28708,
  produitId: 12555,
  dateCommande: dayjs('2024-12-19T05:05'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
