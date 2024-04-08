import { Component } from '@angular/core';
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';

@Component({
  selector: 'app-dishes',
  templateUrl: './dishes.component.html',
  styleUrls: ['./dishes.component.css']
})

export class DishesComponent{
  dish_list:Array<Dish>=new Array<Dish>;
  ordered:number=0;
  daneRef: Observable<any>;

  constructor(private http: HttpClient){
    this.daneRef =http.get('/api/dishes');
    this.daneRef.subscribe((val)=>{
    this.dish_list.splice(0,this.dish_list.length);
      for(let i:number=0;i<val.length;i++){
        let links:Array<String>=val[i].link_to_photos.split(",");
        let new_dish:Dish=new Dish(
            val[i].id,val[i].name,val[i].ingridients,val[i].price,
            val[i].description,links,val[i].rating);
        this.dish_list.push(new_dish);
      }
    });
  }
  order():void{
    this.ordered++;
  }
  resign():void{
    this.ordered--;
  }
}
class Dish{
  id:number=0;
  name:String="";
  ingridients:String="";
  price:number=0;
  description:String="";
  link_to_photos:Array<String>=[];
  rating:number=0;
  constructor(id:number,n:String,i:String,p:number,d:String,l:Array<String>,r:number){
    this.id=id;
    this.name=n;
    this.ingridients=i;
    this.price=p;
    this.description=d;
    this.link_to_photos=l;
    this.rating=r;
  }
}
