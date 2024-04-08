package pizzapp.pizzappbackend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pizzapp.pizzappbackend.models.Dish;
import pizzapp.pizzappbackend.models.Order;
import pizzapp.pizzappbackend.models.UserCredentials;
import pizzapp.pizzappbackend.services.OrderService;
import pizzapp.pizzappbackend.services.UserService;

import java.util.ArrayList;
import java.util.Optional;

@RestController
public class OrderController {
    private OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/api/zamowienie")
    public ResponseEntity<Object> processOrder(@RequestBody Order order){
        Optional obj = this.orderService.processOrder(order);
        if(!obj.isPresent()){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Something went wrong");
        }
        else if(obj.get() instanceof String){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(obj.get());
        }
        return ResponseEntity.status(HttpStatus.OK).body(obj);
    }
    @GetMapping("/api/zamowienia")
    public ArrayList<Order> getUsersOrders(@RequestParam Integer id){
        return this.orderService.getOrders(id);
    }
}
