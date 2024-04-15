import { Component } from '@angular/core';
import { ActivatedRoute, Router} from '@angular/router';
import { faChevronLeft, faChevronRight} from '@fortawesome/free-solid-svg-icons';
import { CartService } from '../Services/cart.service';
import { RolesService } from '../Services/roles.service';
import {HttpClient, HttpHeaders} from "@angular/common/http";

@Component({
  selector: 'app-dish-details',
  templateUrl: './dish-details.component.html',
  styleUrls: ['./dish-details.component.css']
})
export class DishDetailsComponent {
  faChevronLeft=faChevronLeft;
  faChevronRight=faChevronRight;
  id:number=0;
  name:string="";
  ingredients:string="";
  price:number=0;
  description:string="";
  link_to_photos:Array<string>=[];
  photoIndex:number=0;
  photoLink:string="";
  rating:number=0;
  client:boolean=false;
  manager:boolean=false;

  constructor(private route: ActivatedRoute,private router:Router,private cs:CartService,private rs:RolesService,
              private http: HttpClient) {
    rs.roleObservable.subscribe(r=>{
      switch (r){
        case("USER"):
          this.client=true;
          break;
        case("MANAGER"):
          this.manager=true;
          break;
        default:
          this.client=this.manager=false;
      }
    });
  }

  ngOnInit() {
    this.route.params.subscribe(params => {
      this.id = Number(params['id']);

      this.getDishInfo();

    });

  }


  getDishInfo(){
    const httpOptions: { headers: HttpHeaders; observe: any; } = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        'Accept': 'application/json'
      }),
      observe: 'response'
    };

    let response = this.http.get("/api/dish?id="+this.id.toString(),httpOptions);
    response.subscribe((val:any)=>{
      this.name=val.body.name;
      this.ingredients=val.body.ingredients;
      this.price=val.body.price;
      this.description=val.body.description;
      this.rating=val.body.rating;
      this.link_to_photos=val.body.link_to_photos.split(",");
      this.photoLink=this.link_to_photos[this.photoIndex];
    });
  }
  nextImg():void{
    if(this.photoIndex<this.link_to_photos.length-1)
      this.photoIndex++;
    else this.photoIndex=0;
    this.photoLink=this.link_to_photos[this.photoIndex];
  }
  prevImg():void{
    if(this.photoIndex>0)
      this.photoIndex--;
    else this.photoIndex=this.link_to_photos.length-1;
    this.photoLink=this.link_to_photos[this.photoIndex];
  }
  goBack():void{
    this.router.navigate(['/menu']);
  }
  goToEdit(){
    this.router.navigate(['/edytuj', this.id]);
  }

  protected readonly Number = Number;
  protected readonly Math = Math;
}
