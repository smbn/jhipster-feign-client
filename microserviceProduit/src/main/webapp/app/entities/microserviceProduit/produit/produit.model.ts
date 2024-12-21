import { ICategorie } from 'app/entities/microserviceProduit/categorie/categorie.model';

export interface IProduit {
  id: number;
  nomProduit?: string | null;
  descriptionProduit?: string | null;
  prixProduit?: number | null;
  imageProduit?: string | null;
  categorie?: Pick<ICategorie, 'id'> | null;
}

export type NewProduit = Omit<IProduit, 'id'> & { id: null };
