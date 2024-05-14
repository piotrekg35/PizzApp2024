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
import pizzapp.pizzappbackend.controllers.RatingController;
import pizzapp.pizzappbackend.models.Address;
import pizzapp.pizzappbackend.models.Rating;
import pizzapp.pizzappbackend.models.Token;
import pizzapp.pizzappbackend.services.OrderService;
import pizzapp.pizzappbackend.services.RatingService;
import pizzapp.pizzappbackend.services.TokenService;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(RatingController.class)
public class RatingControllerIntegrationTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private RatingService ratingService;
    @MockBean
    private TokenService tokenService;

    private String sampleRating = "{\"user_id\":3,\"dish_id\":2,\"rating\":3,\"date\":\"124-4-4\",\"title\":\"\",\"description\":\"\",\"nick\":\"\"}";
    private String invalidToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhQGEucGwiLCJyb2xlIjoiVVNFUiIsImlzcyI6IlBpenpBcHAiLCJpZCI6MywiYmFubmVkIjpmYWxzZSwiZXhwIjoxNzEzNTkzOTQ0LCJpYXQiOjE3MTM1OTM3NjR9.YugMJplQoSyEkQK2IAWHFnrlxAP290HUz2CPJ39uuC4";

    @Test
    public void ratingsReturnArray()
            throws Exception {
        Rating r1 = new Rating(1,1,5,"124-1-1","Mniam","Bardzo dobra","smoczyWojownik");
        Rating r2 = new Rating(2,1,2,"124-1-1",null,null,null);
        ArrayList<Rating> arr = new ArrayList<>();
        arr.add(r1);
        arr.add(r2);

        given(ratingService.getRatingsForDish(1)).willReturn(arr);

        mvc.perform(get("/api/ratings")
                        .param("id","1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].dish_id")
                        .value(1))
                .andExpect(jsonPath("$[1].dish_id")
                .value(1));

    }

    @Test
    public void postRatingWithoutTokenReturnsBadRequest()
            throws Exception {

        mvc.perform(post("/api/ratings")
                        .content(sampleRating)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));

    }

    @Test
    public void deleteRatingWithoutTokenReturnsBadRequest()
            throws Exception {


        mvc.perform(delete("/api/ratings")
                        .content(sampleRating)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));

    }

    @Test
    public void postRatingWithInvalidTokenReturnsForbidden()
            throws Exception {

        given(tokenService.verifyToken(invalidToken)).willReturn(Optional.empty());

        mvc.perform(post("/api/ratings")
                        .content(sampleRating)
                        .header("Authorization","Bearer "+invalidToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(403));

    }

    @Test
    public void deleteRatingWithInvalidTokenReturnsForbidden()
            throws Exception {

        given(tokenService.verifyToken(invalidToken)).willReturn(Optional.empty());

        mvc.perform(delete("/api/ratings")
                        .content(sampleRating)
                        .header("Authorization","Bearer "+invalidToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(403));

    }

    @Test
    public void postRatingWithValidTokenReturnsOk()
            throws Exception {
        Rating r = new Rating(3,2,3,"124-4-4",null,null,null);

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
        given(ratingService.addRating(any(Rating.class))).willReturn(Optional.of(r));

        mvc.perform(post("/api/ratings")
                        .content(sampleRating)
                        .header("Authorization","Bearer "+validToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    public void deleteRatingWithValidTokenReturnsOk()
            throws Exception {

        Rating r = new Rating(3,2,3,"124-4-4",null,null,null);

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
        given(ratingService.delRating(any(Rating.class))).willReturn(Optional.of(r));

        mvc.perform(delete("/api/ratings")
                        .content(sampleRating)
                        .header("Authorization","Bearer "+validToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }
}
