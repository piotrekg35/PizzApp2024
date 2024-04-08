package pizzapp.pizzappbackend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pizzapp.pizzappbackend.models.User;
import pizzapp.pizzappbackend.models.UserCredentials;
import pizzapp.pizzappbackend.services.UserService;

import java.util.ArrayList;
import java.util.Optional;

@RestController
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/api/users")
    public ArrayList getUsers(){
        return this.userService.getUsers();
    }

    @PostMapping("/api/zarejestruj")
    public ResponseEntity<Object> register(@RequestBody UserCredentials credentials){
        Optional obj = this.userService.addUser(credentials.getEmail(), credentials.getPassword());
        if(!obj.isPresent()){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Something went wrong");
        }
        else if(obj.get() instanceof String){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(obj.get());
        }
        return ResponseEntity.status(HttpStatus.OK).body(obj);
    }
    @PostMapping("/api/zaloguj")
    public ResponseEntity<Object> login(@RequestBody UserCredentials credentials){
        Optional obj = this.userService.getUser(credentials.getEmail(), credentials.getPassword());
        if(!obj.isPresent()){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User does not exist!");
        }
        else if(obj.get() instanceof String){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(obj.get());
        }
        return ResponseEntity.status(HttpStatus.OK).body(obj.get());
    }
}

