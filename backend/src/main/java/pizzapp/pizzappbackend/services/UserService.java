package pizzapp.pizzappbackend.services;

import com.google.common.hash.Hashing;
import org.springframework.stereotype.Service;
import pizzapp.pizzappbackend.models.User;

import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Optional;
import java.util.regex.Pattern;

import static java.lang.System.out;

@Service
public class UserService{

    public ArrayList getUsers() {
        ArrayList<User> arr = new ArrayList<>();
        try
        {
            // Class.forName("org.apache.derby.jdbc.ClientDriver");
            // url = "jdbc:postgresql://host:port/database"
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://cornelius.db.elephantsql.com:5432/povfpdpt";
            String username = "povfpdpt" ;
            String password = "LI3dlVj4nt3t7fqcrfFO1HApkOgW2Yvu" ;
            Connection conn = DriverManager.getConnection(url, username, password);
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM public.users");
            ResultSet rs = stmt.executeQuery();


            while(rs.next())  {
                User u = new User(rs.getInt("id"),rs.getString("email"),
                        rs.getString("password"), rs.getString("role"),
                        rs.getBoolean("banned"));
                arr.add(u);
            }
            rs.close();
            stmt.close();
            conn.close();
        }
        catch(Exception e)
        {  out.println (e) ; }
        return arr;
    }
    private boolean patternMatches(String emailAddress, String regexPattern) {
        return Pattern.compile(regexPattern)
                .matcher(emailAddress)
                .matches();
    }
    public Optional<Object> addUser(String email, String pwd) {
        Optional optional = Optional.empty();
        try
        {
            if(!patternMatches(email,"^(.+)@(\\S+)$")){
                return Optional.of("Not an Email");
            }
            else if(pwd.length()<6){
                return Optional.of("Password too short");
            }
            String sha256hex = Hashing.sha256()
                    .hashString(pwd, StandardCharsets.UTF_8)
                    .toString();
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://cornelius.db.elephantsql.com:5432/povfpdpt";
            String username = "povfpdpt" ;
            String password = "LI3dlVj4nt3t7fqcrfFO1HApkOgW2Yvu" ;
            Connection conn = DriverManager.getConnection(url, username, password);
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO public.users (email, password, role, banned) " +
                    "VALUES (?,?,'USER', false)");
            stmt.setString(1,email);
            stmt.setString(2,sha256hex);
            stmt.executeUpdate();
            stmt.close();
            conn.close();
            return getUser(email,pwd);
        }
        catch(Exception e)
        {
            out.println (e) ;
            optional=Optional.of(e.toString());
        }
        return optional;
    }

    public Optional<Object> getUser(String email, String pwd) {
        Optional optional = Optional.empty();
        try
        {
            String sha256hex = Hashing.sha256()
                    .hashString(pwd, StandardCharsets.UTF_8)
                    .toString();
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://cornelius.db.elephantsql.com:5432/povfpdpt";
            String username = "povfpdpt" ;
            String password = "LI3dlVj4nt3t7fqcrfFO1HApkOgW2Yvu" ;
            Connection conn = DriverManager.getConnection(url, username, password);
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM public.users WHERE email=? and password=?");
            stmt.setString(1,email);
            stmt.setString(2,sha256hex);
            ResultSet rs = stmt.executeQuery();

            if(rs.next())  {
                User u = new User(rs.getInt("id"),rs.getString("email"),
                        rs.getString("password"), rs.getString("role"),
                        rs.getBoolean("banned"));
                optional = optional.of(u);
            }
            rs.close();
            stmt.close();
            conn.close();
        }
        catch(Exception e)
        {
            out.println (e) ;
            optional=Optional.of(e.toString());
        }
        return optional;
    }
}
