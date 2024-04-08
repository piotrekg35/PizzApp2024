import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { RolesService } from '../Services/roles.service';

@Injectable({
  providedIn: 'root'
})
export class AdminGuard implements CanActivate {
  admin:boolean=false;
  constructor( private rs: RolesService, public router: Router){
      rs.roleObservable.subscribe(a=>this.admin=(a=="ADMIN"));
  }
  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
      if(!this.admin){
        this.router.navigate(['/']) ; 
        return false;
      }
      return true;    
    }
}
