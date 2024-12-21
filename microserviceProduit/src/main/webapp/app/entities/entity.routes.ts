import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'categorie',
    data: { pageTitle: 'microserviceProduitApp.microserviceProduitCategorie.home.title' },
    loadChildren: () => import('./microserviceProduit/categorie/categorie.routes'),
  },
  {
    path: 'produit',
    data: { pageTitle: 'microserviceProduitApp.microserviceProduitProduit.home.title' },
    loadChildren: () => import('./microserviceProduit/produit/produit.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
