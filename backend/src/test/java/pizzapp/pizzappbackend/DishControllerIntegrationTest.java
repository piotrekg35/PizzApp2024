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
import pizzapp.pizzappbackend.controllers.DishController;
import pizzapp.pizzappbackend.models.Dish;
import pizzapp.pizzappbackend.models.Token;
import pizzapp.pizzappbackend.services.DishService;
import pizzapp.pizzappbackend.services.TokenService;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(DishController.class)
public class DishControllerIntegrationTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private DishService dishService;
    @MockBean
    private TokenService tokenService;

    @Test
    public void getDishesReturnArray() throws Exception {
        Dish d1 = new Dish(1,"vegan pizza","vegetables","link1,link2","VEGE Pizza",28.99,2);
        Dish d2 = new Dish(2,"pizza from Bolognia","vegetables, prosciutto","link1,link2","Pizza Bolognese",21.99,4);
        ArrayList<Dish> d = new ArrayList<>();
        d.add(d1);
        d.add(d2);

        given(dishService.getDishes()).willReturn(d);

        mvc.perform(get("/api/dishes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].description")
                        .value(d1.getDescription()))
                .andExpect(jsonPath("$[0].ingredients")
                        .value(d1.getIngredients()))
                .andExpect(jsonPath("$[0].name")
                        .value(d1.getName()));
    }

    @Test
    public void getDishReturnArray() throws Exception {
        Dish d1 = new Dish(1,"vegan pizza","vegetables","link1,link2","VEGE Pizza",28.99,2);

        given(dishService.getDish(1)).willReturn(Optional.of(d1));

        mvc.perform(get("/api/dish")
                        .param("id","1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id")
                        .value(d1.getId()));
    }

    @Test
    public void deleteDishWithoutTokenReturnsBadRequest()
            throws Exception {

        mvc.perform(delete("/api/dish")
                        .param("id","1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));

    }

    @Test
    public void putDishWithoutTokenReturnsBadRequest()
            throws Exception {

        Dish d = new Dish(1,"vegan pizza","vegetables","link1,link2","VEGE Pizza",28.99,2);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(d);

        mvc.perform(put("/api/dish")
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));

    }

    @Test
    public void postDishWithoutTokenReturnsBadRequest()
            throws Exception {

        Dish d = new Dish(1,"vegan pizza","vegetables","link1,link2","VEGE Pizza",28.99,2);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(d);

        mvc.perform(post("/api/dish")
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));

    }

    @Test
    public void deleteDishWithoutPermissionReturnsForbidden()
            throws Exception {
        String userToken = JWT.create()
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
        DecodedJWT decodedJWT = verifier.verify(userToken);

        given(tokenService.verifyToken(userToken)).willReturn(Optional.of(new Token(decodedJWT)));
        mvc.perform(delete("/api/dish")
                        .param("id","1")
                        .header("Authorization","Bearer "+userToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(403));

    }

    @Test
    public void putDishWithoutPermissionReturnsForbidden()
            throws Exception {
        String userToken = JWT.create()
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
        DecodedJWT decodedJWT = verifier.verify(userToken);

        Dish d = new Dish(1,"vegan pizza","vegetables","link1,link2","VEGE Pizza",28.99,2);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(d);


        given(tokenService.verifyToken(userToken)).willReturn(Optional.of(new Token(decodedJWT)));
        mvc.perform(put("/api/dish")
                        .content(requestJson)
                        .header("Authorization","Bearer "+userToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(403));

    }

    @Test
    public void postDishWithoutPermissionReturnsForbidden()
            throws Exception {
        String userToken = JWT.create()
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
        DecodedJWT decodedJWT = verifier.verify(userToken);

        Dish d = new Dish(1,"vegan pizza","vegetables","link1,link2","VEGE Pizza",28.99,2);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(d);


        given(tokenService.verifyToken(userToken)).willReturn(Optional.of(new Token(decodedJWT)));
        mvc.perform(post("/api/dish")
                        .content(requestJson)
                        .header("Authorization","Bearer "+userToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(403));

    }

    @Test
    public void deleteDishWithPermissionReturnsOk()
            throws Exception {
        String managerToken = JWT.create()
                .withSubject("test@mock.pl")
                .withIssuer("PizzApp")
                .withClaim("id", "123")
                .withClaim("role", "MANAGER")
                .withClaim("banned", false)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 10000))
                .sign(Algorithm.HMAC256("PizzApp2024JWTsecret"));

        JWTVerifier verifier = JWT.require(Algorithm.HMAC256("PizzApp2024JWTsecret"))
                .withIssuer("PizzApp")
                .build();
        DecodedJWT decodedJWT = verifier.verify(managerToken);

        Dish d = new Dish(1,"vegan pizza","vegetables","link1,link2","VEGE Pizza",28.99,2);

        given(tokenService.verifyToken(managerToken)).willReturn(Optional.of(new Token(decodedJWT)));
        given(dishService.delDsih(1)).willReturn(Optional.of(d));
        mvc.perform(delete("/api/dish")
                        .param("id","1")
                        .header("Authorization","Bearer "+managerToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    public void putDishWithPermissionReturnsOk()
            throws Exception {
        String managerToken = JWT.create()
                .withSubject("test@mock.pl")
                .withIssuer("PizzApp")
                .withClaim("id", "123")
                .withClaim("role", "MANAGER")
                .withClaim("banned", false)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 10000))
                .sign(Algorithm.HMAC256("PizzApp2024JWTsecret"));

        JWTVerifier verifier = JWT.require(Algorithm.HMAC256("PizzApp2024JWTsecret"))
                .withIssuer("PizzApp")
                .build();
        DecodedJWT decodedJWT = verifier.verify(managerToken);

        Dish d = new Dish(1,"vegan pizza","vegetables","link1,link2","VEGE Pizza",28.99,2);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(d);


        given(tokenService.verifyToken(managerToken)).willReturn(Optional.of(new Token(decodedJWT)));
        given(dishService.updateDish(any(Dish.class))).willReturn(Optional.of(d));

        mvc.perform(put("/api/dish")
                        .content(requestJson)
                        .header("Authorization","Bearer "+managerToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    public void postDishWithPermissionReturnsOk()
            throws Exception {
        String managerToken = JWT.create()
                .withSubject("test@mock.pl")
                .withIssuer("PizzApp")
                .withClaim("id", "123")
                .withClaim("role", "MANAGER")
                .withClaim("banned", false)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 10000))
                .sign(Algorithm.HMAC256("PizzApp2024JWTsecret"));

        JWTVerifier verifier = JWT.require(Algorithm.HMAC256("PizzApp2024JWTsecret"))
                .withIssuer("PizzApp")
                .build();
        DecodedJWT decodedJWT = verifier.verify(managerToken);

        Dish d = new Dish(1,"vegan pizza","vegetables","link1,link2","VEGE Pizza",28.99,2);

        given(tokenService.verifyToken(managerToken)).willReturn(Optional.of(new Token(decodedJWT)));
        given(dishService.addDish(any(Dish.class))).willReturn(Optional.of(d));

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(d);

        mvc.perform(post("/api/dish")
                        .header("Authorization","Bearer "+managerToken)
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

}
