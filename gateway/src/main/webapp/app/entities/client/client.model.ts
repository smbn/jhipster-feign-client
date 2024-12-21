import { IUser } from 'app/entities/user/user.model';

export interface IClient {
  id: number;
  nom?: string | null;
  prenom?: string | null;
  adresse?: string | null;
  telephone?: string | null;
  email?: string | null;
  user?: Pick<IUser, 'id'> | null;
}

export type NewClient = Omit<IClient, 'id'> & { id: null };
