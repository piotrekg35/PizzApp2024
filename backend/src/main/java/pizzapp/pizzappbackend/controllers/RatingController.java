package pizzapp.pizzappbackend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pizzapp.pizzappbackend.models.Rating;
import pizzapp.pizzappbackend.services.RatingService;

import java.util.ArrayList;
import java.util.Optional;

@RestController
public class RatingController {
    private RatingService ratingService;
    @Autowired
    public RatingController(RatingService ratingService){ this.ratingService=ratingService; }

    @GetMapping("/api/ratings")
    public ResponseEntity<Object>  getRatings(@RequestParam Integer id){
        return ResponseEntity.status(HttpStatus.OK).body(this.ratingService.getRatingsForDish(id));
    }

    @DeleteMapping("/api/del_rating")
    public ResponseEntity<Object>  delRating(@RequestBody Rating rating){
        Optional obj = this.ratingService.delRating(rating);
        if(!obj.isPresent()){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Something went wrong");
        }
        return ResponseEntity.status(HttpStatus.OK).body(obj);
    }

    @PostMapping("/api/add_rating")
    public ResponseEntity<Object> addRating(@RequestBody Rating rating){
        Optional obj = this.ratingService.addRating(rating);
        if(!obj.isPresent()){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Something went wrong");
        }
        else if(obj.get() instanceof String){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(obj.get());
        }
        return ResponseEntity.status(HttpStatus.OK).body(obj);
    }
}
