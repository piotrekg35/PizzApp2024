import { Component } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {RolesService} from "../Services/roles.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-user-management',
  templateUrl: './user-management.component.html',
  styleUrls: ['./user-management.component.css']
})
export class UserManagementComponent {
  users:Array<User>=[];


  constructor(private http:HttpClient, private rs:RolesService, private router: Router){
    let userData =localStorage.getItem('jwtToken')
    if(!userData) return;
    const httpOptions: { headers: HttpHeaders; } = {
      headers: new HttpHeaders({
        'Authorization': 'Bearer '+ userData.toString()
      })
    };

    this.http.get("/api/users", httpOptions)
        .subscribe((users:any)=>{
          users.forEach((user:any)=>this.users.push(new User(user.id,user.email,user.role,user.banned)));
          this.users.sort((u1,u2)=>{
            if(u1.id<u2.id)return 0;
            return 1;
          })
        },(error)=>{
          if(error.status == 403){
            localStorage.removeItem('jwtToken');
            rs.emitDefaultValues();
            router.navigate(['/zaloguj']);
          }
        });
  }

  save(id: number) {
    let userData =localStorage.getItem('jwtToken')
    if(!userData) return;

    let usr = this.users.find(u=>{return u.id==id});

    const httpOptions: { headers: HttpHeaders } = {
      headers: new HttpHeaders({
        'Content-Type':  'application/json',
        'Accept': 'application/json',
        'Authorization': 'Bearer '+ userData.toString()
      })
    };
    if(usr){
      this.http.put("/api/update_user",JSON.stringify(usr),httpOptions)
          .subscribe(()=>{},(error)=>{
            if(error.status == 403){
              localStorage.removeItem('jwtToken');
              this.rs.emitDefaultValues();
              this.router.navigate(['/zaloguj']);
            }})
    }
  }

  role_change(id: number, role: HTMLSelectElement) {
    let usr = this.users.find(u=>{return u.id==id});
    if(usr){
      usr.role=role.value;
    }
  }

  banned_change(id: number, banned: HTMLInputElement) {
    let usr = this.users.find(u=>{return u.id==id});
    if(usr){
      usr.banned=banned.checked;
    }
  }
}

class User{
  id:number;
  email:string;
  role:string;
  banned:boolean;

  constructor(id: number, email: string, role: string, banned: boolean) {
    this.id = id;
    this.email = email;
    this.role = role;
    this.banned = banned;
  }
}