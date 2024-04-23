package pizzapp.pizzappbackend.models;

import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;

public class Token {
    private int id;
    private String email;
    private String role;
    private boolean banned;
    private String issuer;
    private Date issuedAt;
    private Date expiresAt;

    public Token(DecodedJWT token){
        this.id = Integer.parseInt(token.getClaim("id").toString().replaceAll("\"",""));
        this.email = token.getSubject();
        this.role = token.getClaim("role").toString().replaceAll("\"","");
        this.banned = Boolean.parseBoolean(token.getClaim("banned").toString().replaceAll("\"",""));
        this.issuer = token.getIssuer().replaceAll("\"","");
        this.issuedAt = token.getIssuedAt();
        this.expiresAt = token.getExpiresAt();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isBanned() {
        return banned;
    }

    public void setBanned(boolean banned) {
        this.banned = banned;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public Date getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(Date issuedAt) {
        this.issuedAt = issuedAt;
    }

    public Date getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Date expiresAt) {
        this.expiresAt = expiresAt;
    }
}
