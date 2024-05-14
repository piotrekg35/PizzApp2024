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
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import pizzapp.pizzappbackend.controllers.UserController;
import pizzapp.pizzappbackend.models.Token;
import pizzapp.pizzappbackend.models.User;
import pizzapp.pizzappbackend.models.UserCredentials;
import pizzapp.pizzappbackend.services.TokenService;
import pizzapp.pizzappbackend.services.UserService;

import java.util.Date;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerIntegrationTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;
    @MockBean
    private TokenService tokenService;

    @Test
    public void loginWithoutCredentaialsReturnsBadRequest()
            throws Exception {

        mvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));
    }
    @Test
    public void registerWithoutCredentaialsReturnsBadRequest()
            throws Exception {

        mvc.perform(post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));
    }
    @Test
    public void loginWithCredentaialsReturnsOkWithTokenInResponseHeader()
            throws Exception {

        String credentials = "{\"email\":\"ala@ala.pl\", \"password\":\"alaMAkota\"}";
        User u = new User(1,"ala@ala.pl","hash","USER",false);
        given(this.userService.getUser("ala@ala.pl","alaMAkota")).willReturn(Optional.of(u));
        given(tokenService.getJWTToken(u)).willReturn("validToken");

        mvc.perform(post("/api/login")
                        .content(credentials)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().exists("token"))
                .andExpect(status().is(200));

    }
    @Test
    public void registerWithCredentaialsReturnsOkWithTokenInResponseHeader()
            throws Exception {

        String credentials = "{\"email\":\"ala@ala.pl\", \"password\":\"alaMAkota\"}";
        User u = new User(1,"ala@ala.pl","hash","USER",false);
        given(this.userService.addUser("ala@ala.pl","alaMAkota")).willReturn(Optional.of(u));
        given(tokenService.getJWTToken(u)).willReturn("validToken");

        mvc.perform(post("/api/register")
                        .content(credentials)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().exists("token"))
                .andExpect(status().is(200));

    }
    @Test
    public void getUsersWithoutTokenReturnsBadRequest()
            throws Exception {

        mvc.perform(get("/api/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));
    }

    @Test
    public void getUsersWithoutPermissionReturnsForbidden()
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
        mvc.perform(get("/api/users")
                        .header("Authorization","Bearer "+userToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(403));

    }
    @Test
    public void getUsersWithPermissionReturnsOk()
            throws Exception {
        String userToken = JWT.create()
                .withSubject("test@mock.pl")
                .withIssuer("PizzApp")
                .withClaim("id", "123")
                .withClaim("role", "ADMIN")
                .withClaim("banned", false)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 10000))
                .sign(Algorithm.HMAC256("PizzApp2024JWTsecret"));

        JWTVerifier verifier = JWT.require(Algorithm.HMAC256("PizzApp2024JWTsecret"))
                .withIssuer("PizzApp")
                .build();
        DecodedJWT decodedJWT = verifier.verify(userToken);

        given(tokenService.verifyToken(userToken)).willReturn(Optional.of(new Token(decodedJWT)));
        mvc.perform(get("/api/users")
                        .header("Authorization","Bearer "+userToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200));

    }

    @Test
    public void updateUserWithoutTokenReturnsBadRequest()
            throws Exception {

        User u = new User(1,"ala@ala.pl","hash","USER",false);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String body=ow.writeValueAsString(u);


        mvc.perform(put("/api/update_user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().is(400));
    }

    @Test
    public void updateUserWithoutPermissionReturnsForbidden()
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

        User u = new User(1,"ala@ala.pl","hash","USER",false);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String body=ow.writeValueAsString(u);


        given(tokenService.verifyToken(userToken)).willReturn(Optional.of(new Token(decodedJWT)));
        given(userService.updateUser(any(User.class))).willReturn(Optional.of(u));

        mvc.perform(put("/api/update_user")
                        .header("Authorization","Bearer "+userToken)
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(403));

    }

    @Test
    public void updateUserWithPermissionReturnsOk()
            throws Exception {
        String userToken = JWT.create()
                .withSubject("test@mock.pl")
                .withIssuer("PizzApp")
                .withClaim("id", "123")
                .withClaim("role", "ADMIN")
                .withClaim("banned", false)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 10000))
                .sign(Algorithm.HMAC256("PizzApp2024JWTsecret"));

        JWTVerifier verifier = JWT.require(Algorithm.HMAC256("PizzApp2024JWTsecret"))
                .withIssuer("PizzApp")
                .build();
        DecodedJWT decodedJWT = verifier.verify(userToken);

        User u = new User(1,"ala@ala.pl","hash","USER",false);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String body=ow.writeValueAsString(u);


        given(tokenService.verifyToken(userToken)).willReturn(Optional.of(new Token(decodedJWT)));
        given(userService.updateUser(any(User.class))).willReturn(Optional.of(u));

        mvc.perform(put("/api/update_user")
                        .header("Authorization","Bearer "+userToken)
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200));

    }

}
