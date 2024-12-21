import { IClient, NewClient } from './client.model';

export const sampleWithRequiredData: IClient = {
  id: 28452,
  nom: 'ouah responsable',
  prenom: 'capter diplomate',
  telephone: '0696912196',
  email: 'Marine.Renard18@yahoo.fr',
};

export const sampleWithPartialData: IClient = {
  id: 24547,
  nom: 'de manière à ce que errer à moins de',
  prenom: 'ouille collègue trop peu',
  telephone: '+33 424742592',
  email: 'Amaliane.Charpentier@hotmail.fr',
};

export const sampleWithFullData: IClient = {
  id: 31033,
  nom: 'athlète par rapport à',
  prenom: 'vraiment',
  adresse: 'naguère secours responsable',
  telephone: '0307502848',
  email: 'Brunehaut.Charpentier@yahoo.fr',
};

export const sampleWithNewData: NewClient = {
  nom: "hisser multiple d'entre",
  prenom: 'passablement',
  telephone: '0156560739',
  email: 'Armand.Garcia86@gmail.com',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
