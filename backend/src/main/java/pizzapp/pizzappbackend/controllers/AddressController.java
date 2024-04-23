package pizzapp.pizzappbackend.controllers;

import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pizzapp.pizzappbackend.models.Address;
import pizzapp.pizzappbackend.models.Token;
import pizzapp.pizzappbackend.models.UserCredentials;
import pizzapp.pizzappbackend.services.AddressService;
import pizzapp.pizzappbackend.services.TokenService;

import java.util.ArrayList;
import java.util.Optional;

@RestController
public class AddressController {
    private AddressService addressService;
    private TokenService tokenService;

    @Autowired
    public AddressController(AddressService addressService, TokenService tokenService) {
        this.addressService = addressService;
        this.tokenService = tokenService;
    }

    @GetMapping("/api/address")
    public ResponseEntity<Object> getUserAddress(@RequestHeader("Authorization") String token){
        token=token.split(" ")[1];
        Optional<Token> t = this.tokenService.verifyToken(token);
        if(t.isEmpty()) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        int id = t.get().getId();
        Optional obj = this.addressService.getUserAddress(id);
        if(!obj.isPresent()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(obj.get());
    }
    @GetMapping("/api/locals")
    public ResponseEntity<Object> getLocals(){
        return ResponseEntity.ok().body(this.addressService.getLocals());
    }

    @PutMapping("/api/address")
    public ResponseEntity<Object> update(@RequestHeader("Authorization") String token, @RequestBody Address a){
        token=token.split(" ")[1];
        Optional<Token> t = this.tokenService.verifyToken(token);
        if(t.isEmpty()) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        int id = t.get().getId();
        Optional obj = this.addressService.updateAddress(id,a.getAddress(),a.getPostCode(),a.getCity());
        if(!obj.isPresent()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(obj.get());
    }
}
