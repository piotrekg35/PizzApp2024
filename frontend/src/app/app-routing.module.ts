import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DishesComponent } from './dishes/dishes.component';
import { HomeComponent } from './home/home.component';
import { CartComponent } from './cart/cart.component';
import { DishDetailsComponent } from './dish-details/dish-details.component';
import { RegisterComponent } from './register/register.component';
import { LogInComponent } from './log-in/log-in.component';
import { CartGuard } from './guard/cart.guard';
import { ProfileComponent } from './profile/profile.component';

const routes: Routes = [ 
  { path: 'menu', component: DishesComponent }, 
  { path: 'profil', component: ProfileComponent, canActivate: [CartGuard] },
  { path: 'koszyk', component: CartComponent, canActivate: [CartGuard]},
  { path: 'produkt/:id', component: DishDetailsComponent },
  { path: 'zarejestruj', component: RegisterComponent},
  { path: 'zaloguj', component: LogInComponent},
  { path: '', component: HomeComponent } 
]

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
