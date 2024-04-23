import { Injectable } from '@angular/core';
import { ReplaySubject } from 'rxjs';
import { jwtDecode } from 'jwt-decode';
import {HttpClient, HttpHeaders} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class RolesService {
  userIdObservable = new ReplaySubject<number>();
  roleObservable = new ReplaySubject<string>();
  bannedObservable = new ReplaySubject<boolean>();
  loggedObservable = new ReplaySubject<boolean>();
  emailObservable = new ReplaySubject<string>();

  constructor(private http:HttpClient) {
    let userData =localStorage.getItem('jwtToken')
    if(userData){
      let userDataJSON:any = jwtDecode(userData);
      const httpOptions: { observe: any; } = {
        observe: 'response'
      };
      http.get("/api/verify_token?token="+userData.toString(),httpOptions)
          .subscribe((a)=>{
            this.emitValues(userDataJSON.id, userDataJSON.sub, userDataJSON.role, userDataJSON.banned);
            },
                  e=>localStorage.removeItem('jwtToken'));
    }
    else
      this.emitDefaultValues();
  }
  emitValues(id:number, email:string, role:string, banned: boolean){
    this.loggedObservable.next(true);
    this.userIdObservable.next(id);
    this.emailObservable.next(email);
    this.roleObservable.next(role);
    this.bannedObservable.next(banned);
  }
  emitDefaultValues(){
    this.userIdObservable.next(0);
    this.loggedObservable.next(false);
    this.emailObservable.next("");
    this.roleObservable.next("");
    this.bannedObservable.next(false);
  }
}
