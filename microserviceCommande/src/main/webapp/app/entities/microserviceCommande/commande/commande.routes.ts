import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import CommandeResolve from './route/commande-routing-resolve.service';

const commandeRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/commande.component').then(m => m.CommandeComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/commande-detail.component').then(m => m.CommandeDetailComponent),
    resolve: {
      commande: CommandeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/commande-update.component').then(m => m.CommandeUpdateComponent),
    resolve: {
      commande: CommandeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/commande-update.component').then(m => m.CommandeUpdateComponent),
    resolve: {
      commande: CommandeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default commandeRoute;
