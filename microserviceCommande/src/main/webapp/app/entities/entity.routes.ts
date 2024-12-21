import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'commande',
    data: { pageTitle: 'microserviceCommandeApp.microserviceCommandeCommande.home.title' },
    loadChildren: () => import('./microserviceCommande/commande/commande.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
