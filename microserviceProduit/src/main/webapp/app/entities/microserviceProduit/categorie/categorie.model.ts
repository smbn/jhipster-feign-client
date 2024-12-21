export interface ICategorie {
  id: number;
  nomCategorie?: string | null;
}

export type NewCategorie = Omit<ICategorie, 'id'> & { id: null };
