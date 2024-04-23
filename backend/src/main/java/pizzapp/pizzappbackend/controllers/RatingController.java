package pizzapp.pizzappbackend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pizzapp.pizzappbackend.models.Rating;
import pizzapp.pizzappbackend.models.Token;
import pizzapp.pizzappbackend.services.RatingService;
import pizzapp.pizzappbackend.services.TokenService;

import java.util.ArrayList;
import java.util.Optional;

@RestController
public class RatingController {
    private RatingService ratingService;
    private TokenService tokenService;
    @Autowired
    public RatingController(RatingService ratingService, TokenService tokenService){
        this.ratingService=ratingService;
        this.tokenService = tokenService;
    }

    @GetMapping("/api/ratings")
    public ResponseEntity<Object>  getRatings(@RequestParam Integer id){
        return ResponseEntity.ok().body(this.ratingService.getRatingsForDish(id));
    }

    @DeleteMapping("/api/ratings")
    public ResponseEntity<Object>  delRating(@RequestBody Rating rating, @RequestHeader("Authorization") String token){
        token=token.split(" ")[1];
        Optional<Token> t = this.tokenService.verifyToken(token);
        if(t.isEmpty() || t.get().getRole().equals("MANAGER"))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        Optional obj = this.ratingService.delRating(rating);
        if(!obj.isPresent()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(obj.get());
    }

    @PostMapping("/api/ratings")
    public ResponseEntity<Object> addRating(@RequestBody Rating rating, @RequestHeader("Authorization") String token){
        token=token.split(" ")[1];
        Optional<Token> t = this.tokenService.verifyToken(token);
        if(t.isEmpty() || !t.get().getRole().equals("USER"))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        Optional obj = this.ratingService.addRating(rating);
        if(!obj.isPresent()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(obj.get());
    }
}
