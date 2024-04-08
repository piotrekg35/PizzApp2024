import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { map, Observable } from 'rxjs';
import { RolesService } from '../Services/roles.service';

@Injectable({
  providedIn: 'root'
})
export class CartGuard implements CanActivate {
  client:boolean=false;
  constructor( private rs: RolesService, public router: Router){
      rs.roleObservable.subscribe(a=>this.client=(a=="USER"));
  }
  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
        if(!this.client){
          this.router.navigate(['/']) ; 
          return false;
        }
        return true;    
    }
}