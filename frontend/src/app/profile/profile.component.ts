import {Component, OnChanges} from '@angular/core';
import {RolesService} from "../Services/roles.service";
import {HttpClient, HttpHeaders} from "@angular/common/http";

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnChanges{
  email:string="";
  msg:string="";
  zamowienia:any[] = [];
  address:string="";
  postcode:string="";
  city:string="";
  id:number=0;
  dishNames: any=null;

  constructor(private rs:RolesService, private http: HttpClient){}

  ngOnChanges(){
    this.rs.userIdObservable.subscribe(id=>{
      this.id=id;
      if(id==0)return;
      let response = this.http.get("/api/address?id="+id.toString());
      response.subscribe((data:any)=>{
        if(data){
          this.address = data.address;
          this.postcode = data.postCode;
          this.city = data.city;
        }
      });
      response = this.http.get("/api/zamowienia?id="+id.toString());
      response.subscribe((data:any)=>{if(data)this.zamowienia=data});
    });

    this.dishNames=new Map();
    let response = this.http.get("/api/dishes");
    response.subscribe((data:any)=>{
      if(data)
        data.forEach((d:any)=>{if(d)this.dishNames.set(d.id, d.name)})
    });
  }
  save():void{
    if(this.address==""||this.postcode==""|| this.city==""){
      this.msg="Uzupełnij wszystkie pola!"
      return;
    }

    const httpOptions: { headers: HttpHeaders; observe: any; } = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        'Accept': 'application/json'
      }),
      observe: 'response'
    };

    let response = this.http.put('/api/update_address?id='+this.id.toString(), {
      address: this.address,
      postCode: this.postcode,
      city: this.city
    }, httpOptions);

    response.subscribe((a)=>{this.msg="Zaaktualizowano dane"},
        (error: any) => {
          let err: String = String(error.error)
          if (err.includes("Invalid")) {
            this.msg = "Błędny kod pocztowy"
          }  else {
            this.msg = "Coś poszło nie tak spróbuj ponownie!"
          }});
  }
}



