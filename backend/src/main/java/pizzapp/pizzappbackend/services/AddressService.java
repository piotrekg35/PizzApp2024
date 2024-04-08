package pizzapp.pizzappbackend.services;

import org.springframework.stereotype.Service;
import pizzapp.pizzappbackend.models.Address;

import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Optional;
import java.util.regex.Pattern;

import static java.lang.System.out;


@Service
public class AddressService {
    public Optional<Address> getUserAddress(Integer id) {
        Optional optional = Optional.empty();
        try
        {
            // Class.forName("org.apache.derby.jdbc.ClientDriver");
            // url = "jdbc:postgresql://host:port/database"
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://cornelius.db.elephantsql.com:5432/povfpdpt";
            String username = "povfpdpt" ;
            String password = "LI3dlVj4nt3t7fqcrfFO1HApkOgW2Yvu" ;
            Connection conn = DriverManager.getConnection(url, username, password);
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM public.users WHERE id = ?");
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if(rs.next())  {
                Address a = new Address(rs.getString("address"),
                        rs.getString("postcode"), rs.getString("city"));
                optional = optional.of(a);
            }
            rs.close();
            stmt.close();
            conn.close();


        }
        catch(Exception e)
        {  out.println (e) ; }
        return optional;
    }
    public ArrayList<Address> getLocals() {
        ArrayList<Address> arr = new ArrayList<>();
        try
        {
            // Class.forName("org.apache.derby.jdbc.ClientDriver");
            // url = "jdbc:postgresql://host:port/database"
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://cornelius.db.elephantsql.com:5432/povfpdpt";
            String username = "povfpdpt" ;
            String password = "LI3dlVj4nt3t7fqcrfFO1HApkOgW2Yvu" ;
            Connection conn = DriverManager.getConnection(url, username, password);
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM public.locals");
            ResultSet rs = stmt.executeQuery();

            while(rs.next())  {
                Address a = new Address(rs.getString("address"),
                        rs.getString("postcode"), rs.getString("city"));
                arr.add(a);
            }
            rs.close();
            stmt.close();
            conn.close();
        }
        catch(Exception e)
        {  out.println (e) ; }
        return arr;
    }
    private boolean patternMatches(String postcode, String regexPattern) {
        return Pattern.compile(regexPattern)
                .matcher(postcode)
                .matches();
    }
    public Optional<Object> updateAddress(Integer id,String address, String postcode, String city) {
        Optional optional = Optional.empty();
        try
        {
            if(!patternMatches(postcode,"^[0-9]{2}\\-[0-9]{3}$")){
                return Optional.of("Invalid postcode");
            }
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://cornelius.db.elephantsql.com:5432/povfpdpt";
            String username = "povfpdpt" ;
            String password = "LI3dlVj4nt3t7fqcrfFO1HApkOgW2Yvu" ;
            Connection conn = DriverManager.getConnection(url, username, password);
            PreparedStatement stmt = conn.prepareStatement("UPDATE public.users SET address=?, postcode=?, city=? " +
                    "WHERE id=?");
            stmt.setString(1,address);
            stmt.setString(2,postcode);
            stmt.setString(3,city);
            stmt.setInt(4,id);
            stmt.executeUpdate();
            stmt.close();
            conn.close();

            optional=Optional.of(new Address(address,postcode,city));
        }
        catch(Exception e) {
            out.println(e);
            optional = Optional.of(e.toString());
        }
        return optional;
    }
}
