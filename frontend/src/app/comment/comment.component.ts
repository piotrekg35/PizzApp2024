import {Component, EventEmitter, Input, Output} from '@angular/core';
import {RolesService} from "../Services/roles.service";
import {faTrash} from "@fortawesome/free-solid-svg-icons";
import {HttpClient, HttpHeaders} from "@angular/common/http";

@Component({
  selector: 'app-comment',
  templateUrl: './comment.component.html',
  styleUrls: ['./comment.component.css']
})
export class CommentComponent {
  @Input() new_rating:number=0;
  @Input() nick:string="";
  @Input() title:string="";
  @Input() description:string="";
  @Input() usrId:number=0;
  @Input() dish_id:number=0;
  loggedUsrId:number=0;
  admin:boolean=false;
  @Output() newItemEvent = new EventEmitter<null>();

  constructor(private rs:RolesService, private http: HttpClient) {
    rs.userIdObservable.subscribe(id=>this.loggedUsrId=id);
    rs.roleObservable.subscribe(r=>{if(r=="ADMIN")this.admin=true;})
  }

  protected readonly faTrash = faTrash;

  delComment() {
    const date: Date=new Date();
    let dateString = (date.getFullYear()-1900).toString()+'-'+date.getMonth().toString()+'-'+date.getDate().toString();

    const httpOptions: { headers: HttpHeaders, body: any} = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        'Accept': 'application/json'
      }),
      body: {
        user_id: this.usrId,
        dish_id:this.dish_id,
        rating:this.new_rating,
        date: dateString,
        title: this.title,
        description: this.description,
        nick: this.nick
      }
    };

    this.http.delete("/api/del_rating", httpOptions).subscribe(a=>this.newItemEvent.emit());
  }
}
