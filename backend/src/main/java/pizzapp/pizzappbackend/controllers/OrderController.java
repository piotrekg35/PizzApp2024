package pizzapp.pizzappbackend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pizzapp.pizzappbackend.models.Dish;
import pizzapp.pizzappbackend.models.Order;
import pizzapp.pizzappbackend.models.Token;
import pizzapp.pizzappbackend.models.UserCredentials;
import pizzapp.pizzappbackend.services.OrderService;
import pizzapp.pizzappbackend.services.TokenService;
import pizzapp.pizzappbackend.services.UserService;

import java.util.ArrayList;
import java.util.Optional;

@RestController
public class OrderController {
    private OrderService orderService;
    private TokenService tokenService;

    @Autowired
    public OrderController(OrderService orderService, TokenService tokenService) {
        this.orderService = orderService;
        this.tokenService = tokenService;
    }

    @PostMapping("/api/orders")
    public ResponseEntity<Object> processOrder(@RequestBody Order order, @RequestHeader("Authorization") String token){
        token=token.split(" ")[1];
        Optional<Token> t = this.tokenService.verifyToken(token);
        if(t.isEmpty() || !t.get().getRole().equals("USER"))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        Optional obj = this.orderService.processOrder(order);
        if(!obj.isPresent()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(obj.get());
    }
    @GetMapping("/api/orders")
    public ResponseEntity<Object> getUsersOrders(@RequestHeader("Authorization") String token){
        token=token.split(" ")[1];
        Optional<Token> t = this.tokenService.verifyToken(token);
        if(t.isEmpty() || !t.get().getRole().equals("USER"))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        int id = t.get().getId();
        return ResponseEntity.ok().body(this.orderService.getOrders(id));
    }
}
