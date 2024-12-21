import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import CategorieResolve from './route/categorie-routing-resolve.service';

const categorieRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/categorie.component').then(m => m.CategorieComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/categorie-detail.component').then(m => m.CategorieDetailComponent),
    resolve: {
      categorie: CategorieResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/categorie-update.component').then(m => m.CategorieUpdateComponent),
    resolve: {
      categorie: CategorieResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/categorie-update.component').then(m => m.CategorieUpdateComponent),
    resolve: {
      categorie: CategorieResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default categorieRoute;
