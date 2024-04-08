package pizzapp.pizzappbackend.services;

import org.springframework.stereotype.Service;
import pizzapp.pizzappbackend.models.Dish;

import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;

import static java.lang.System.out;

@Service
public class DishService {
    public Optional<Dish> getDish(Integer id) {
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
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM public.dishes WHERE id = ?");
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if(rs.next())  {
                Dish d = new Dish(rs.getInt("id"), rs.getString("description"),
                                rs.getString("ingridients"), rs.getString("link_to_photos"),
                                rs.getString("name"), rs.getDouble("price"), rs.getDouble("rating"));
                optional = optional.of(d);
            }
            rs.close();
            stmt.close();
            conn.close();


        }
        catch(Exception e)
        {  out.println (e) ; }
        return optional;
    }

    public ArrayList getDishes() {
        ArrayList<Dish> arr = new ArrayList<>();
        try
        {
            // Class.forName("org.apache.derby.jdbc.ClientDriver");
            // url = "jdbc:postgresql://host:port/database"
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://cornelius.db.elephantsql.com:5432/povfpdpt";
            String username = "povfpdpt" ;
            String password = "LI3dlVj4nt3t7fqcrfFO1HApkOgW2Yvu" ;
            Connection conn = DriverManager.getConnection(url, username, password);
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM public.dishes");
            ResultSet rs = stmt.executeQuery();


            while(rs.next())  {
                Dish d = new Dish(rs.getInt("id"), rs.getString("description"),
                        rs.getString("ingridients"), rs.getString("link_to_photos"),
                        rs.getString("name"), rs.getDouble("price"), rs.getDouble("rating"));
                arr.add(d);
            }
            rs.close();
            stmt.close();
            conn.close();
        }
        catch(Exception e)
        {  out.println (e) ; }
        return arr;
    }

}
