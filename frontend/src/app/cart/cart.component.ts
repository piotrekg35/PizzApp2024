import {Component, OnInit} from '@angular/core';
import { CartService, DishGeneral } from '../Services/cart.service';
import { RolesService } from '../Services/roles.service';
import { DatePipe } from '@angular/common';
import {HttpClient, HttpHeaders} from "@angular/common/http";

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css']
})
export class CartComponent implements OnInit{
  reserved:Array<DishGeneral>=[];
  date:string="";
  msg:string="";
  email:string="";
  locals:Array<string>=[];
  id:number=0;
  address:string="";
  postcode:string="";
  city:string="";
  odbiorRadio:any =null;
  dostawaRadio:any =null;
  cartForm:any=null;
  odbiorSelect:any=null;
  zaznacz:any=null;
  innerRadios: HTMLCollectionOf<any> | undefined;
  finalAddress:string="";
  finalPrice:number=0;
  constructor(private cs:CartService, private rs:RolesService, private http: HttpClient){

    this.rs.userIdObservable.subscribe(id=>this.id=id);

    let response = this.http.get("/api/address?id="+this.id.toString());
    response.subscribe((data:any)=>{
      this.address = data.address;
      this.postcode = data.postCode;
      this.city = data.city;
    });

    cs.reservedObservable.subscribe(r=>this.reserved=r);
    rs.emailObservable.subscribe(email => {
      this.email = email;
      });
  }
  ngOnInit() {
    this.odbiorRadio=document.getElementById("odbior");
    this.dostawaRadio=document.getElementById("dostawa");
    this.cartForm=document.getElementById("cartForm");
    this.odbiorSelect=document.getElementById("odbiorselect");
    this.zaznacz=document.getElementById("zaznacz");
    if(this.reserved.length==0)this.zaznacz.style.display="none";
    this.innerRadios = document.getElementsByClassName("innerOdbiorRadio");

    let response = this.http.get('/api/locals');
    response.subscribe((locals:any)=>{
      locals.forEach((l:any)=>this.locals.push(l.address+", "+l.postCode+", "+l.city));
    },null,()=>{this.finalAddress=this.locals[0]});

    this.finalPrice=Math.round(this.reserved.reduce(function(prev,curr)
      {return prev+curr.ordered*curr.price},0)*100)/100;
  }

  clikRadio(radio:any){
    if(!this.odbiorRadio || !this.dostawaRadio || !this.cartForm|| !this.odbiorSelect)return;

    if(radio==this.odbiorRadio){
      this.odbiorRadio.checked=true;
      this.dostawaRadio.checked=false;
      this.cartForm.style.display="none";
      this.odbiorSelect.style.display="block";
      this.finalPrice-=9.99;
    }
    else if(radio==this.dostawaRadio){
      this.odbiorRadio.checked=false;
      this.dostawaRadio.checked=true;
      this.cartForm.style.display="block";
      this.odbiorSelect.style.display="none";
      this.finalAddress=this.address+", "+this.postcode+", "+this.city;
      this.finalPrice+=9.99;
    }
  }
  clikInnerRadio(radio:any){
    if(!this.innerRadios)return;
    for(let i=0;i<this.innerRadios.length;i++){
      if(radio==this.innerRadios[i]){
        this.innerRadios[i].checked=true;
        this.finalAddress=this.innerRadios[i].id;
      }
      else this.innerRadios[i].checked=false;
    }
  }
  buy():void{
    if(this.dostawaRadio.checked && (this.address.length==0 || this.postcode.length==0|| this.city.length==0)){
      this.msg="Uzupełnij wszystkie pola";
      return;
    }

    const date: Date=new Date();
    let dateString = (date.getFullYear()-1900).toString()+'-'+date.getMonth().toString()+'-'+date.getDate().toString();

    var body ={
      user_id: this.id,
      cost: this.finalPrice,
      address: this.finalAddress,
      date: dateString,
      order_products:[]
    }

    this.cs.reserved.forEach((dish)=> {
      // @ts-ignore
      body.order_products.push({
        dish_id: dish.id,
        size: dish.size,
        quantity: dish.ordered
      })
    });

    console.log(dateString);
    const httpOptions: { headers: HttpHeaders; observe: any; } = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        'Accept': 'application/json'
      }),
      observe: 'response'
    };

    let response = this.http.post('/api/zamowienie', body, httpOptions);
    response.subscribe((data:any) => {
      this.msg="Dziękujemy za zakupy!";
      this.cs.clearCart();
      this.zaznacz.style.display="none";
      }, (error: any) => this.msg="Coś poszło nie tak. Spróbuj ponownie póżniej.");
  }
}
