import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICommande } from '../commande.model';
import { CommandeService } from '../service/commande.service';

const commandeResolve = (route: ActivatedRouteSnapshot): Observable<null | ICommande> => {
  const id = route.params.id;
  if (id) {
    return inject(CommandeService)
      .find(id)
      .pipe(
        mergeMap((commande: HttpResponse<ICommande>) => {
          if (commande.body) {
            return of(commande.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default commandeResolve;
