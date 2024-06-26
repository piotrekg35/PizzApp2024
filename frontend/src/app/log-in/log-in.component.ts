import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { RolesService } from '../Services/roles.service';
import {HttpClient, HttpHeaders} from "@angular/common/http";

@Component({
  selector: 'app-log-in',
  templateUrl: './log-in.component.html',
  styleUrls: ['./log-in.component.css']
})
export class LogInComponent {

  email_input: string = "";
  pwd_input: string = "";
  msg: string = "";

  constructor(private router: Router, private rs: RolesService, private http: HttpClient) {
  }

  login() {
    const httpOptions: { headers: HttpHeaders; observe: any; } = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        'Accept': 'application/json'
      }),
      observe: 'response'
    };

    let response = this.http.post('/api/login', {
      email: this.email_input,
      password: this.pwd_input
    }, httpOptions);

    response.subscribe((data:any) => {
      this.rs.emitValues(data.body.id, this.email_input, data.body.role, data.body.banned);
      localStorage.setItem('jwtToken', data.headers.get('token'));
      this.router.navigate(['/']);
    }, (error: any) => {
      if (error.status == 404) {
        this.msg = "Błędne dane!"
      }  else {
        this.msg = "Coś poszło nie tak spróbuj ponownie!"
      }
    });
  }
}
