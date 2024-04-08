package pizzapp.pizzappbackend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pizzapp.pizzappbackend.models.Address;
import pizzapp.pizzappbackend.models.UserCredentials;
import pizzapp.pizzappbackend.services.AddressService;

import java.util.ArrayList;
import java.util.Optional;

@RestController
public class AddressController {
    private AddressService addressService;

    @Autowired
    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @GetMapping("/api/address")
    public Address getUserAddress(@RequestParam Integer id){
        Optional address = this.addressService.getUserAddress(id);
        if(address.isPresent()){
            return (Address) address.get();
        }
        return null;
    }
    @GetMapping("/api/locals")
    public ArrayList getLocals(){
        return this.addressService.getLocals();
    }

    @PutMapping("/api/update_address")
    public ResponseEntity<Object> update(@RequestParam Integer id, @RequestBody Address a){
        Optional obj = this.addressService.updateAddress(id,a.getAddress(),a.getPostCode(),a.getCity());
        if(!obj.isPresent()){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Something went wrong");
        }
        else if(obj.get() instanceof String){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(obj.get());
        }
        return ResponseEntity.status(HttpStatus.OK).body(obj.get());
    }
}
