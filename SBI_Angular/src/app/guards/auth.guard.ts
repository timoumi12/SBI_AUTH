import {
  ActivatedRouteSnapshot,
  CanActivateFn,
  Router,
  RouterStateSnapshot,
  Route,
} from '@angular/router';
import { User } from '../models/user.model';
import { Injectable, inject } from '@angular/core';
import { AuthenticationService } from '../services/authentication.service';
import { Role } from '../models/role.enum';

@Injectable({
  providedIn: 'root',
})
class PermissionsService {
  private currentUser: User = new User();
  constructor(
    private authenticationservice: AuthenticationService,
    private router: Router
  ) {
    this.authenticationservice.currentUser.subscribe((data) => {
      this.currentUser = data;
    });
  }

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): boolean {
    // if (this.currentUser) {
    //   if (route.data['roles'].indexOf(this.currentUser.role) === -1) {
    //     this.router.navigate(['/401']);
    //     return false;
    //   }
    //   return true;
    // }
    this.router.navigate(['/login']);
    return true;
  }
}

export const AuthGuard: CanActivateFn = (
  route: ActivatedRouteSnapshot,
  state: RouterStateSnapshot
): boolean => {
  return inject(PermissionsService).canActivate(route, state);
};
