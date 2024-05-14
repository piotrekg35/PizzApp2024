package pizzapp.pizzappbackend;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
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
import pizzapp.pizzappbackend.controllers.OrderController;
import pizzapp.pizzappbackend.models.Dish;
import pizzapp.pizzappbackend.models.Order;
import pizzapp.pizzappbackend.models.OrderProduct;
import pizzapp.pizzappbackend.models.Token;
import pizzapp.pizzappbackend.services.OrderService;
import pizzapp.pizzappbackend.services.TokenService;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(OrderController.class)
public class OrderControllerIntegrationTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private OrderService orderService;
    @MockBean
    private TokenService tokenService;

    private String sampleOrder="{\"user_id\":3,\"cost\":33.99,\"address\":\"Królowej Jadwigi 184, 30-212, Kraków\",\"date\":\"124-4-4\",\"order_products\":[{\"dish_id\":1,\"size\":\"ŚREDNIA\",\"quantity\":1}]}";
    private String invalidToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhQGEucGwiLCJyb2xlIjoiVVNFUiIsImlzcyI6IlBpenpBcHAiLCJpZCI6MywiYmFubmVkIjpmYWxzZSwiZXhwIjoxNzEzNTkzOTQ0LCJpYXQiOjE3MTM1OTM3NjR9.YugMJplQoSyEkQK2IAWHFnrlxAP290HUz2CPJ39uuC4";

    @Test
    public void getOrderWithoutTokenReturnsBadRequest()
            throws Exception {

        mvc.perform(get("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));

    }

    @Test
    public void postOrderWithoutTokenReturnsBadRequest()
            throws Exception {

        mvc.perform(post("/api/orders")
                        .content(sampleOrder)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));

    }

    @Test
    public void getOrderWithInvalidTokenReturnsForbidden()
            throws Exception {

        given(tokenService.verifyToken(invalidToken)).willReturn(Optional.empty());

        mvc.perform(get("/api/orders")
                        .header("Authorization","Bearer "+invalidToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(403));

    }

    @Test
    public void postOrderWithInvalidTokenReturnsForbidden()
            throws Exception {

        given(tokenService.verifyToken(invalidToken)).willReturn(Optional.empty());

        mvc.perform(post("/api/orders")
                        .content(sampleOrder)
                        .header("Authorization","Bearer "+invalidToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(403));

    }

    @Test
    public void getOrderWithValidTokenReturnsOk()
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
        given(orderService.getOrders(123)).willReturn(new ArrayList<Order>());

        mvc.perform(get("/api/orders")
                        .header("Authorization","Bearer "+validToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    public void postOrderWithValidTokenReturnsOk()
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

        given(orderService.processOrder(any(Order.class)))
                .willReturn(Optional.of(new Order(123,11.89,"address",new ArrayList<>(),"120-4-4")));

        mvc.perform(post("/api/orders")
                        .content(sampleOrder)
                        .header("Authorization","Bearer "+validToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }
}
