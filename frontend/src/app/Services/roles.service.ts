import { Injectable } from '@angular/core';
import { ReplaySubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class RolesService {
  userIdObservable = new ReplaySubject<number>();
  roleObservable = new ReplaySubject<string>();
  bannedObservable = new ReplaySubject<boolean>();
  loggedObservable = new ReplaySubject<boolean>();
  emailObservable = new ReplaySubject<string>();

  constructor() {
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
