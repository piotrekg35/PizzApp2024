import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CartService {
  count:number=0;
  reserved:Array<DishGeneral>=[];
  countObservable = new BehaviorSubject<number>(this.count);
  reservedObservable = new BehaviorSubject<Array<DishGeneral>>(this.reserved);
  constructor() { }

  clearCart(){
    this.count=0;
    this.countObservable.next(0);
    this.reservedObservable.next([]);
    this.reserved.splice(0);
  }
}
export class DishGeneral{
  id:number=0;
  name:string="";
  price:number=0;
  size:string="";
  link_to_photos:Array<string>=[];
  ordered:number=0;

  constructor(id:number,n:string,o:number,s:string,p:number,l:Array<string>){
    this.id=id;
    this.name=n;
    this.size=s;
    this.price=p;
    this.link_to_photos=l;
    this.ordered=o;
  }
}