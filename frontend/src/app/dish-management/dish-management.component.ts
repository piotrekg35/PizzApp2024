import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { faChevronLeft } from '@fortawesome/free-solid-svg-icons';
import {HttpClient, HttpHeaders} from "@angular/common/http";


@Component({
  selector: 'app-dish-management',
  templateUrl: './dish-management.component.html',
  styleUrls: ['./dish-management.component.css']
})
export class DishManagementComponent {
  faChevronLeft=faChevronLeft;
  id:number=0;
  description:string="";
  ingredients:string="";
  link_to_photos:string="";
  name:string="";
  price:number=0;
  msg:string="";

  constructor(private route: ActivatedRoute, private router:Router, private http: HttpClient){
    this.route.params.subscribe(params => {
      this.id = Number(params['id']);
    });

    http.get('/api/dish?id='+this.id.toString())
        .subscribe((val:any)=>{
          this.description=val.description;
          this.ingredients=val.ingredients;
          this.name=val.name;
          this.price=val.price;
          this.link_to_photos=val.link_to_photos;
    });
  }

  goBack():void{
    this.router.navigate(['/produkt',this.id]);
  }
  save():void{
    if(this.description==""||this.ingredients==""|| this.link_to_photos==""||this.price<0||this.name==""){
      this.msg="Błędne dane!"
      return;
    }
    let userData =localStorage.getItem('jwtToken')
    if(!userData) return;

    const httpOptions: { headers: HttpHeaders } = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        'Accept': 'application/json',
        'Authorization': 'Bearer '+ userData.toString()
      })
    };
    let body={
      id: this.id,
      description: this.description,
      ingredients: this.ingredients,
      name: this.name,
      price: this.price,
      link_to_photos: this.link_to_photos,
      rating: 0
    }
    this.http.put("/api/dish",body,httpOptions).subscribe(()=>this.goBack());
  }
}
