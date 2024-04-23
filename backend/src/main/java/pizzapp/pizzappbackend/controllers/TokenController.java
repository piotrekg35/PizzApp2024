package pizzapp.pizzappbackend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pizzapp.pizzappbackend.services.TokenService;

@RestController
public class TokenController {
    private TokenService tokenService;

    public TokenController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @GetMapping("/api/verify_token")
    public ResponseEntity verifyToken(@RequestParam String token){
        if(this.tokenService.verifyToken(token).isPresent())
            return ResponseEntity.ok().build();
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }
}
