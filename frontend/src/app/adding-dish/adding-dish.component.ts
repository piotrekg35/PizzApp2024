import { Component } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Router} from "@angular/router";
import {faChevronLeft} from "@fortawesome/free-solid-svg-icons";

@Component({
  selector: 'app-adding-dish',
  templateUrl: './adding-dish.component.html',
  styleUrls: ['./adding-dish.component.css']
})
export class AddingDishComponent {
  message:string="";
  name:string="";
  ingredients:string="";
  price:string="";
  description:string="";
  link_to_photos:string="";

  constructor(private http: HttpClient, private router:Router){}

  clean():void{
    this.name=this.ingredients=this.price=this.description=this.link_to_photos="";
  }
  addDish():void{
    if(this.name.trim()==="" ||this.price.trim()===""|| Number.isNaN(Number(this.price)) || Number(this.price)<0 ||
        this.description.trim()===""||this.link_to_photos.split(",").length<2){
      this.message="Błędne dane!";
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
      id: 0,
      description: this.description,
      ingredients: this.ingredients,
      name: this.name,
      price: this.price,
      link_to_photos: this.link_to_photos,
      rating: 0
    }
    this.http.post("/api/dish",body,httpOptions)
        .subscribe(()=>{
          this.message="Sukces!";
          this.clean();
        });
  }

  goBack():void{
    this.router.navigate(['/menu']);
  }

  protected readonly faChevronLeft = faChevronLeft;
}