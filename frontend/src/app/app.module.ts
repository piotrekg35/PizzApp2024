import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { DishesComponent } from './dishes/dishes.component';
import { DishComponent } from './dish/dish.component';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { FormsModule } from '@angular/forms';
import { RatingComponent } from './rating/rating.component';
import { HomeComponent } from './home/home.component';
import { CartComponent } from './cart/cart.component';
import { DishDetailsComponent } from './dish-details/dish-details.component';
import { CommentComponent } from './comment/comment.component';
import { RegisterComponent } from './register/register.component';
import { LogInComponent } from './log-in/log-in.component';
import { MatTabsModule } from '@angular/material/tabs';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ProfileComponent } from './profile/profile.component';
import { OrderSchemeComponent } from './order-scheme/order-scheme.component';
import { DishManagementComponent } from './dish-management/dish-management.component';
import { AddingDishComponent } from './adding-dish/adding-dish.component';
import { UserManagementComponent } from './user-management/user-management.component';


@NgModule({
  declarations: [
    AppComponent,
    DishesComponent,
    DishComponent,
    RatingComponent,
    HomeComponent,
    CartComponent,
    DishDetailsComponent,
    CommentComponent,
    RegisterComponent,
    LogInComponent,
    ProfileComponent,
    OrderSchemeComponent,
    DishManagementComponent,
    AddingDishComponent,
    UserManagementComponent
  ],
  imports: [
    HttpClientModule,
    MatTabsModule,
    BrowserModule,
    AppRoutingModule,
    FontAwesomeModule,
    FormsModule,
    BrowserAnimationsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
