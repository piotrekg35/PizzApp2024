import { Component } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";

@Component({
  selector: 'app-user-management',
  templateUrl: './user-management.component.html',
  styleUrls: ['./user-management.component.css']
})
export class UserManagementComponent {
  users:Array<User>=[];


  constructor(private http:HttpClient){
    this.http.get("/api/users")
        .subscribe((users:any)=>{
          users.forEach((user:any)=>this.users.push(new User(user.id,user.email,user.role,user.banned)));
        });
  }

  save(id: number) {
    let usr = this.users.find(u=>{return u.id==id});

    const httpOptions: { headers: HttpHeaders } = {
      headers: new HttpHeaders({
        'Content-Type':  'application/json',
        'Accept': 'application/json'
      })
    };
    if(usr){
      this.http.put("/api/update_user",JSON.stringify(usr),httpOptions).subscribe();
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