package pizzapp.pizzappbackend;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import pizzapp.pizzappbackend.controllers.AddressController;
import pizzapp.pizzappbackend.models.Address;
import pizzapp.pizzappbackend.models.Token;
import pizzapp.pizzappbackend.services.AddressService;
import pizzapp.pizzappbackend.services.TokenService;

import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@RunWith(SpringRunner.class)
@WebMvcTest(AddressController.class)
public class AddressControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AddressService addressService;
    @MockBean
    private TokenService tokenService;

    private String invalidToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhQGEucGwiLCJyb2xlIjoiVVNFUiIsImlzcyI6IlBpenpBcHAiLCJpZCI6MywiYmFubmVkIjpmYWxzZSwiZXhwIjoxNzEzNTkzOTQ0LCJpYXQiOjE3MTM1OTM3NjR9.YugMJplQoSyEkQK2IAWHFnrlxAP290HUz2CPJ39uuC4";
    @Test
    public void localsReturnArray()
            throws Exception {
        Address a1 = new Address("Wrocławska 12","31-111","Kraków");
        Address a2 = new Address("Aleja Kijowska 32","31-131","Kraków");
        ArrayList<Address> addresses = new ArrayList<>();
        addresses.add(a1);
        addresses.add(a2);

        given(addressService.getLocals()).willReturn(addresses);

        mvc.perform(get("/api/locals")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].address")
                        .value(a1.getAddress()))
                .andExpect(jsonPath("$[0].postCode")
                        .value(a1.getPostCode()))
                .andExpect(jsonPath("$[0].city")
                        .value(a1.getCity()));

    }
    @Test
    public void getAddressWithoutTokenReturnsBadRequest()
            throws Exception {

        mvc.perform(get("/api/address")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));

    }

    @Test
    public void putAddressWithoutTokenReturnsBadRequest()
            throws Exception {
        Address a1 = new Address("Wrocławska 12","31-111","Kraków");
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(a1);
        mvc.perform(put("/api/address")
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));

    }

    @Test
    public void getAddressWithoutValidTokenReturnsForbidden()
            throws Exception {

        given(tokenService.verifyToken(invalidToken)).willReturn(Optional.empty());

        mvc.perform(get("/api/address")
                        .header("Authorization","Bearer "+invalidToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(403));

    }

    @Test
    public void putAddressWithoutValidTokenReturnsForbidden()
            throws Exception {
        Address a1 = new Address("Wrocławska 12","31-111","Kraków");
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(a1);
        given(tokenService.verifyToken(invalidToken)).willReturn(Optional.empty());
        mvc.perform(put("/api/address")
                        .header("Authorization","Bearer "+invalidToken)
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(403));

    }

    @Test
    public void getAddressWithValidTokenReturnsOk()
            throws Exception {

        String validToken = JWT.create()
                .withSubject("test@mock.pl")
                .withIssuer("PizzApp")
                .withClaim("id", "123")
                .withClaim("role", "USER")
                .withClaim("banned", false)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 10000))
                .sign(Algorithm.HMAC256("PizzApp2024JWTsecret"));

        JWTVerifier verifier = JWT.require(Algorithm.HMAC256("PizzApp2024JWTsecret"))
                .withIssuer("PizzApp")
                .build();
        DecodedJWT decodedJWT = verifier.verify(validToken);

        given(tokenService.verifyToken(validToken)).willReturn(Optional.of(new Token(decodedJWT)));

        Address a1 = new Address("Wrocławska 12","31-111","Kraków");
        given(addressService.getUserAddress(123)).willReturn(Optional.of(a1));

        mvc.perform(get("/api/address")
                        .header("Authorization","Bearer "+validToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    public void putAddressWithoutValidTokenReturnsOk()
            throws Exception {
        String validToken = JWT.create()
                .withSubject("test@mock.pl")
                .withIssuer("PizzApp")
                .withClaim("id", "123")
                .withClaim("role", "USER")
                .withClaim("banned", false)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 10000))
                .sign(Algorithm.HMAC256("PizzApp2024JWTsecret"));

        JWTVerifier verifier = JWT.require(Algorithm.HMAC256("PizzApp2024JWTsecret"))
                .withIssuer("PizzApp")
                .build();
        DecodedJWT decodedJWT = verifier.verify(validToken);

        given(tokenService.verifyToken(validToken)).willReturn(Optional.of(new Token(decodedJWT)));

        Address a1 = new Address("Wrocławska 12","31-111","Kraków");
        given(addressService.updateAddress(123,"Wrocławska 12","31-111","Kraków")).willReturn(Optional.of(a1));

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(a1);

        mvc.perform(put("/api/address")
                        .header("Authorization","Bearer "+validToken)
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }
}
