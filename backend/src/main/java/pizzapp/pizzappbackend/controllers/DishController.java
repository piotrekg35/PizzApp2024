package pizzapp.pizzappbackend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pizzapp.pizzappbackend.models.Dish;
import pizzapp.pizzappbackend.models.Rating;
import pizzapp.pizzappbackend.models.Token;
import pizzapp.pizzappbackend.services.DishService;
import pizzapp.pizzappbackend.services.TokenService;

import java.util.ArrayList;
import java.util.Optional;

@RestController
public class DishController {
    private DishService dishService;
    private TokenService tokenService;

    @Autowired
    public DishController(DishService dishService, TokenService tokenService) {
        this.dishService = dishService;
        this.tokenService = tokenService;
    }

    @GetMapping("/api/dish")
    public ResponseEntity<Object> getDish(@RequestParam Integer id){
        Optional obj = this.dishService.getDish(id);
        if(!obj.isPresent()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(obj.get());
    }
    @GetMapping("/api/dishes")
    public ResponseEntity<Object> getDishes(){
        return ResponseEntity.ok().body(this.dishService.getDishes());
    }

    @DeleteMapping("/api/dish")
    public ResponseEntity<Object>  delDish(@RequestParam Integer id, @RequestHeader("Authorization") String token){
        token=token.split(" ")[1];
        Optional<Token> t = this.tokenService.verifyToken(token);
        if(t.isEmpty() || !t.get().getRole().equals("MANAGER"))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        Optional obj = this.dishService.delDsih(id);
        if(!obj.isPresent()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(obj.get());
    }

    @PutMapping("/api/dish")
    public ResponseEntity<Object>  updateDish(@RequestBody Dish dish, @RequestHeader("Authorization") String token){
        token=token.split(" ")[1];
        Optional<Token> t = this.tokenService.verifyToken(token);
        if(t.isEmpty() || !t.get().getRole().equals("MANAGER"))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        Optional obj = this.dishService.updateDish(dish);
        if(!obj.isPresent()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(obj.get());
    }

    @PostMapping("/api/dish")
    public ResponseEntity<Object>  addDish(@RequestBody Dish dish, @RequestHeader("Authorization") String token){
        token=token.split(" ")[1];
        Optional<Token> t = this.tokenService.verifyToken(token);
        if(t.isEmpty() || !t.get().getRole().equals("MANAGER"))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        Optional obj = this.dishService.addDish(dish);
        if(!obj.isPresent()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(obj.get());
    }

}
