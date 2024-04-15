package pizzapp.pizzappbackend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pizzapp.pizzappbackend.models.Dish;
import pizzapp.pizzappbackend.models.Rating;
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
    public ResponseEntity<Object> getDish(@RequestParam Integer id){
        Optional obj = this.dishService.getDish(id);
        if(!obj.isPresent()){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Something went wrong");
        }
        return ResponseEntity.status(HttpStatus.OK).body(obj);
    }
    @GetMapping("/api/dishes")
    public ResponseEntity<Object> getDishes(){
        return ResponseEntity.status(HttpStatus.OK).body(this.dishService.getDishes());
    }

    @DeleteMapping("/api/del_dish")
    public ResponseEntity<Object>  delDish(@RequestParam Integer id){
        Optional obj = this.dishService.delDsih(id);
        if(!obj.isPresent()){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Something went wrong");
        }
        return ResponseEntity.status(HttpStatus.OK).body(obj);
    }

    @PutMapping("/api/update_dish")
    public ResponseEntity<Object>  updateDish(@RequestBody Dish dish){
        Optional obj = this.dishService.updateDish(dish);
        if(!obj.isPresent()){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Something went wrong");
        }
        return ResponseEntity.status(HttpStatus.OK).body(obj);
    }

    @PostMapping("/api/add_dish")
    public ResponseEntity<Object>  addDish(@RequestBody Dish dish){
        Optional obj = this.dishService.addDish(dish);
        if(!obj.isPresent()){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Something went wrong");
        }
        return ResponseEntity.status(HttpStatus.OK).body(obj);
    }

}
