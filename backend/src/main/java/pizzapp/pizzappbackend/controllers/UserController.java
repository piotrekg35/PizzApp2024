package pizzapp.pizzappbackend.controllers;

import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pizzapp.pizzappbackend.models.Token;
import pizzapp.pizzappbackend.models.User;
import pizzapp.pizzappbackend.models.UserCredentials;
import pizzapp.pizzappbackend.services.TokenService;
import pizzapp.pizzappbackend.services.UserService;

import java.util.ArrayList;
import java.util.Optional;

@RestController
public class UserController {
    private UserService userService;
    private TokenService tokenService;

    @Autowired
    public UserController(UserService userService, TokenService tokenService) {
        this.userService = userService;
        this.tokenService =tokenService;
    }

    @GetMapping("/api/users")
    public ResponseEntity<Object> getUsers(@RequestHeader("Authorization") String token){
        token=token.split(" ")[1];
        Optional<Token> t = this.tokenService.verifyToken(token);
        if(t.isPresent() && t.get().getRole().equals("ADMIN"))
            return ResponseEntity.ok().body(this.userService.getUsers());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @PostMapping("/api/register")
    public ResponseEntity<Object> register(@RequestBody UserCredentials credentials){
        Optional obj = this.userService.addUser(credentials.getEmail(), credentials.getPassword());
        if(!obj.isPresent()){
            return ResponseEntity.notFound().build();
        }
        else if(obj.get() instanceof String){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(obj.get());
        }
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("token", this.tokenService.getJWTToken((User)obj.get()));
        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(obj.get());
    }
    @PostMapping("/api/login")
    public ResponseEntity<Object> login(@RequestBody UserCredentials credentials){
        Optional obj = this.userService.getUser(credentials.getEmail(), credentials.getPassword());
        if(!obj.isPresent()){
            return ResponseEntity.notFound().build();
        }
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("token", this.tokenService.getJWTToken((User)obj.get()));
        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(obj.get());
    }
    @PutMapping("/api/update_user")
    public ResponseEntity<Object> updateUser(@RequestBody User user, @RequestHeader("Authorization") String token) {
        token=token.split(" ")[1];
        Optional<Token> t = this.tokenService.verifyToken(token);

        Optional obj = this.userService.updateUser(user);
        if(!obj.isPresent()){
            return ResponseEntity.notFound().build();
        }
        if(t.isPresent() && t.get().getRole().equals("ADMIN"))
            return ResponseEntity.ok().body(obj.get());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
}

