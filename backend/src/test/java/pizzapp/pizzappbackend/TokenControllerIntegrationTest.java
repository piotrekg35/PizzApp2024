package pizzapp.pizzappbackend;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import pizzapp.pizzappbackend.controllers.TokenController;
import pizzapp.pizzappbackend.models.Token;
import pizzapp.pizzappbackend.services.TokenService;

import java.util.Date;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(TokenController.class)
public class TokenControllerIntegrationTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private TokenService tokenService;

    @Test
    public void verifyTokenWithoutTokenReturnsBadRequest()
            throws Exception {

        mvc.perform(get("/api/verify_token")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));
    }

    @Test
    public void verifyTokenWithInvalidTokenReturnsNotAcceptable()
            throws Exception {

        String invalidToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhQGEucGwiLCJyb2xlIjoiVVNFUiIsImlzcyI6IlBpenpBcHAiLCJpZCI6MywiYmFubmVkIjpmYWxzZSwiZXhwIjoxNzEzNTkzOTQ0LCJpYXQiOjE3MTM1OTM3NjR9.YugMJplQoSyEkQK2IAWHFnrlxAP290HUz2CPJ39uuC4";
        given(tokenService.verifyToken(invalidToken)).willReturn(Optional.empty());

        mvc.perform(get("/api/verify_token")
                        .param("token",invalidToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(406));
    }

    @Test
    public void verifyTokenWithValidTokenReturnsOk()
            throws Exception {

        String validToken = JWT.create()
                .withSubject("test@mock.pl")
                .withIssuer("PizzApp")
                .withClaim("id", "3")
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

        mvc.perform(get("/api/verify_token")
                        .param("token",validToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
