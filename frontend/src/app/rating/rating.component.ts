import {Component, EventEmitter, Input, OnChanges, Output} from '@angular/core';
import { RolesService } from '../Services/roles.service';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Router} from "@angular/router";

@Component({
  selector: 'app-rating',
  templateUrl: './rating.component.html',
  styleUrls: ['./rating.component.css']
})
export class RatingComponent implements OnChanges{
  new_rating:number=5;
  title:string="";
  description:string="";
  nick:string="";
  rating_list:Array<any>=[];
  @Input() id:number=0;
  @Input() old_rating:number=0;
  msg:string="";
  banned:boolean=false;
  client:boolean=false;
  manager:boolean=false;
  admin:boolean=false;
  reviewed:boolean=false;
  bought:boolean=false;
  userId:number=0;
  @Output() newItemEvent = new EventEmitter<null>();

  constructor(private rs:RolesService, private http: HttpClient){}

  ngOnChanges(){

    this.rs.bannedObservable.subscribe(a=>{
      this.banned=a;
      if(a)this.msg="Nie masz możliwości oceniania i zostawiania komentarzy!";
    });

    this.rs.userIdObservable.subscribe(usrid=>{

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

        this.getAllComments();
        this.userId=usrid;
        let userData =localStorage.getItem('jwtToken')
        if(!userData || this.admin || this.manager) return;

        const httpOptions: { headers: HttpHeaders; observe: any; } = {
          headers: new HttpHeaders({
            'Content-Type': 'application/json',
            'Accept': 'application/json',
            'Authorization': 'Bearer '+ userData.toString()
          }),
          observe: 'response'
        };

        let response = this.http.get('/api/orders', httpOptions);

        response.subscribe((data:any) => {
          data.body.forEach((order: any)=>{
            if(order)
              order.order_products.forEach((p: any)=>{
                if(p.dish_id==this.id){
                  this.bought=true;
                }
              })
          })
        }, (error: any) => {
          this.msg = "Coś poszło nie tak spróbuj ponownie!"
        });
      });
    });
  }

  getAllComments():void{

      const httpOptions: { headers: HttpHeaders; observe: any; } = {
        headers: new HttpHeaders({
          'Content-Type': 'application/json',
          'Accept': 'application/json'
        }),
        observe: 'response'
      };
      let response = this.http.get('/api/ratings?id=' + this.id.toString(), httpOptions);

      response.subscribe((data: any) => {
        this.rating_list = data.body;
        this.rating_list.forEach((a: any) => {
          if (a.user_id == this.userId)
            this.reviewed = true;
        });
      }, (error: any) => {
        this.msg = "Coś poszło nie tak spróbuj ponownie!"
      });
  }

  addRating():void{
    if(this.new_rating<1||this.new_rating>5){
      this.msg="Błędna ocena"
    }
    let userData =localStorage.getItem('jwtToken')
    if(!userData) return;

    const httpOptions: { headers: HttpHeaders; observe: any; } = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        'Accept': 'application/json',
        'Authorization': 'Bearer '+ userData.toString()
      }),
      observe: 'response'
    };
    const date: Date=new Date();
    let dateString = (date.getFullYear()-1900).toString()+'-'+date.getMonth().toString()+'-'+date.getDate().toString();

    let new_rating_body:any={
      user_id: this.userId,
      dish_id: this.id,
      rating:this.new_rating,
      date: dateString,
      title: this.title,
      description: this.description,
      nick: this.nick
    };

    let response = this.http.post("/api/ratings",new_rating_body,httpOptions);
    response.subscribe(()=> {
      this.getAllComments();
      this.reviewed=true;
      this.newItemEvent.emit();
      }
    ,(error)=>this.msg="Coś poszło nie tak");


  }
  updateAfterDelete(){
    this.getAllComments();
    this.reviewed=false;
    this.newItemEvent.emit();
  }
}
