import dayjs from 'dayjs/esm';

export interface ICommande {
  id: number;
  clientId?: number | null;
  produitId?: number | null;
  dateCommande?: dayjs.Dayjs | null;
}

export type NewCommande = Omit<ICommande, 'id'> & { id: null };
