import { Component, Input } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { faChevronLeft, faChevronRight, faPlusCircle, faMinusCircle, faTrash} from '@fortawesome/free-solid-svg-icons';
import { CartService } from '../Services/cart.service';
import { RolesService } from '../Services/roles.service';

@Component({
  selector: 'app-dish',
  templateUrl: './dish.component.html',
  styleUrls: ['./dish.component.css']
})
export class DishComponent{
  faChevronLeft=faChevronLeft;
  faChevronRight=faChevronRight;
  faPlusCircle=faPlusCircle;
  faMinusCircle=faMinusCircle;
  faTrash=faTrash;
  @Input() id:number=0;
  @Input() name:String="";
  @Input() ingridients:String="";
  max_amount:number=5;
  @Input() price:number=0;
  @Input() description:String="";
  @Input() link_to_photos:Array<String>=[];
  @Input() rating:number=0;
  photoIndex:number=0;
  photoLink:String="";
  ordered:number=0;
  client:boolean=false;
  manager:boolean=false;

  constructor(private route:ActivatedRoute,private router:Router,private cs:CartService, private rs:RolesService){}
  
  ngOnInit():void{
    this.photoLink=this.link_to_photos[this.photoIndex];

    this.rs.roleObservable.subscribe(r=>{
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
  goToDetails(){
    this.router.navigate(['/produkt', this.id]);
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
  deleteDish():void{
    //TODO
  }
}
