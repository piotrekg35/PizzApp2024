import {Component, Input, OnChanges} from '@angular/core';
import {faMinusCircle, faPlusCircle} from "@fortawesome/free-solid-svg-icons";
import {CartService, DishGeneral} from "../Services/cart.service";
import {RolesService} from "../Services/roles.service";

@Component({
  selector: 'app-order-scheme',
  templateUrl: './order-scheme.component.html',
  styleUrls: ['./order-scheme.component.css']
})
export class OrderSchemeComponent implements OnChanges{

    protected readonly faPlusCircle = faPlusCircle;
    protected readonly faMinusCircle = faMinusCircle;
    max_amount:number=0;
    ordered:Array<number>=[0,0,0];
    prices:Array<number>=[0,0,0];
    @Input() id:number=0;
    @Input() price:number=0;
    @Input() name:string="";
    @Input() link_to_photos:Array<string>=[];
    mapping:Array<string>=["MAŁA", "ŚREDNIA", "DUŻA"];


    constructor(private cs:CartService) {
    }
    ngOnChanges(){
        if(this.price==0)return;
        this.max_amount=10;
        this.prices=[this.price,Math.ceil(this.price*1.3)-0.01,Math.ceil(this.price*1.5)-0.01];
        this.cs.reservedObservable.subscribe(r=>{
            this.max_amount=10-r.reduce((acc, curr) => acc + curr.ordered, 0)
            for(let s of this.mapping){
                let d=r.filter(a=>(a.id===this.id && a.size==s));
                let idx:number=this.mapping.indexOf(s);
                if(d.length===0)this.ordered[idx]=0;
                else{
                    let idxD:number=r.indexOf(d[0]);
                    this.ordered[idx]=r[idxD].ordered;
                }
            }
        });
    }

    order(size:string):void{
        let idx:number=this.mapping.indexOf(size);
        this.max_amount--;
        this.ordered[idx]++;
        this.cs.countObservable.next(++this.cs.count);
        let d=this.cs.reserved.filter(a=>(a.id===this.id && a.size===size));
        if(d.length===0)this.cs.reserved.push(new DishGeneral(this.id,this.name,this.ordered[idx],size,this.prices[idx],
            this.link_to_photos));
        else{
            let idxD:number=this.cs.reserved.indexOf(d[0]);
            this.cs.reserved[idxD].ordered=this.ordered[idx];
        }
        this.cs.reservedObservable.next(this.cs.reserved);
    }
    resign(size:string):void{
        let idx:number=this.mapping.indexOf(size);
        if(this.ordered[idx]>0)
        {
            this.max_amount++;
            this.ordered[idx]--;
            this.cs.countObservable.next(--this.cs.count);

            let d=this.cs.reserved.filter(a=>(a.id===this.id && a.size===size));
            let idxD:number=this.cs.reserved.indexOf(d[0]);
            this.cs.reserved[idxD].ordered=this.ordered[idx];
            if(this.ordered[idx]===0)this.cs.reserved.splice(idxD,1);
            this.cs.reservedObservable.next(this.cs.reserved);
        }
    }

    protected readonly Math = Math;
}
