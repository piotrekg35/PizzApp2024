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

  constructor(private http: HttpClient){
    this.http.get("/api/dishes").subscribe((val:any)=>{
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

  updateDishList(id:number) {
    let dish2del=this.dish_list.findIndex((d)=>{return d.id==id;});
    if(dish2del!=-1)
      this.dish_list.splice(dish2del,1);
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
