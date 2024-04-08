package pizzapp.pizzappbackend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pizzapp.pizzappbackend.models.Dish;
import pizzapp.pizzappbackend.services.DishService;

import java.util.ArrayList;
import java.util.Optional;

@RestController
public class DishController {
    private DishService dishService;

    @Autowired
    public DishController(DishService dishService) {
        this.dishService = dishService;
    }

    @GetMapping("/api/dish")
    public Dish getDish(@RequestParam Integer id){
        Optional dish = this.dishService.getDish(id);
        if(dish.isPresent()){
            return (Dish) dish.get();
        }
        return null;
    }
    @GetMapping("/api/dishes")
    public ArrayList getDishes(){
        return this.dishService.getDishes();
    }

}
