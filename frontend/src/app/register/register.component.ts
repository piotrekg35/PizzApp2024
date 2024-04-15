import { Component } from '@angular/core';
import { Router } from '@angular/router';

import { RolesService } from '../Services/roles.service';
import {HttpClient} from '@angular/common/http';
import { HttpHeaders } from '@angular/common/http';
import {Observable} from "rxjs";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  email_input:string="";
  pwd_input:string="";
  msg:string="";
  
  constructor(private router:Router, private rs:RolesService, private http: HttpClient) {}
  
  register(){

    const httpOptions: { headers: HttpHeaders; observe: any; } = {
      headers: new HttpHeaders({
        'Content-Type':  'application/json',
        'Accept': 'application/json'
      }),
      observe: 'response'
    };

    let response = this.http.post('/api/zarejestruj',{ email: this.email_input,
          password:this.pwd_input }, httpOptions);
    response.subscribe((data:any) => {
      this.rs.emitValues(data.body.id, this.email_input, data.body.role, data.body.banned);
          localStorage.setItem('userData', JSON.stringify({id: data.body.id, email: this.email_input, role: data.body.role, banned: data.body.banned}));
      this.router.navigate(['/']);
      return;}
        , (error:any)=>{
      let err:String = String(error.error)
      if(err.includes("Email already taken")){
        this.msg="Konto z tym emailem już istnieje!"
      }
      else if(err.includes("too short")){
        this.msg="Hasło musi posiadać co najmniej 6 znaków!"
      }
      else if(err.includes("Not an Email")){
        this.msg="Email jest niepoprawny!"
      }
      else {
        this.msg="Coś poszło nie tak spróbuj ponownie!"
      }
    });
  }
  goToLoginPage():void{
    this.router.navigate(['/zaloguj']);
  }
  
}
