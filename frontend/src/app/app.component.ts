import { Component } from '@angular/core';
import { RolesService } from './Services/roles.service';
import { Router } from '@angular/router';
import { CartService } from "./Services/cart.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  ordered:number=0;
  isLoggedIn:boolean=false;
  name:string="";
  admin:boolean=false;
  client:boolean=false;
  manager:boolean=false; 

  constructor(private cs:CartService, private rs:RolesService, private router:Router){
    this.rs.roleObservable.subscribe(r=>{
      switch (r){
        case("USER"):
          this.client=true;
          break;
        case("MANAGER"):
          this.manager=true;
          break;
        case("ADMIN"):
          this.admin=true;
          break;
        default:
          this.client=this.manager=this.admin=false;
      }
    });
    this.rs.loggedObservable.subscribe(v=>this.isLoggedIn=v);
    this.rs.emailObservable.subscribe(v=>this.name=v.split('@')[0]);
    this.cs.countObservable.subscribe(c=>this.ordered=c);
  }
  logout():void{
    this.rs.emitDefaultValues();
    this.cs.clearCart();
    this.router.navigate(['/']);
    localStorage.removeItem('jwtToken');
  }
  goToProfile():void{
    this.router.navigate(['/profil']);
  }
}
