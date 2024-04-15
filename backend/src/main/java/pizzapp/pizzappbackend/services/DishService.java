package pizzapp.pizzappbackend.services;

import org.springframework.stereotype.Service;
import pizzapp.pizzappbackend.models.Address;
import pizzapp.pizzappbackend.models.Dish;

import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;

import static java.lang.System.console;
import static java.lang.System.out;

@Service
public class DishService {
    public Optional<Object> getDish(Integer id) {
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

    public Optional delDsih(Integer id) {
        try
        {
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://cornelius.db.elephantsql.com:5432/povfpdpt";
            String username = "povfpdpt" ;
            String password = "LI3dlVj4nt3t7fqcrfFO1HApkOgW2Yvu" ;
            Connection conn = DriverManager.getConnection(url, username, password);
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM public.dishes WHERE id=?");
            stmt.setInt(1,id);
            stmt.executeUpdate();
            stmt.close();
            conn.close();

            return Optional.of(id);
        }
        catch(Exception e) {
            out.println(e);
        }
        return null;
    }

    public Optional updateDish(Dish dish){
        Optional optional = Optional.empty();
        try
        {
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://cornelius.db.elephantsql.com:5432/povfpdpt";
            String username = "povfpdpt" ;
            String password = "LI3dlVj4nt3t7fqcrfFO1HApkOgW2Yvu" ;
            Connection conn = DriverManager.getConnection(url, username, password);
            PreparedStatement stmt = conn.prepareStatement("UPDATE public.dishes SET description=?, ingridients=?, " +
                    "link_to_photos=?, name=?, price=? WHERE id=?");
            stmt.setString(1,dish.getDescription());
            stmt.setString(2,dish.getIngredients());
            stmt.setString(3,dish.getLink_to_photos());
            stmt.setString(4,dish.getName());
            stmt.setDouble(5,dish.getPrice());
            stmt.setInt(6,dish.getId());
            stmt.executeUpdate();
            stmt.close();
            conn.close();

            optional=Optional.of(dish);
        }
        catch(Exception e) {
            out.println(e);
        }
        return optional;
    }

    public Optional addDish(Dish dish) {
        Optional optional = Optional.empty();
        try
        {
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://cornelius.db.elephantsql.com:5432/povfpdpt";
            String username = "povfpdpt" ;
            String password = "LI3dlVj4nt3t7fqcrfFO1HApkOgW2Yvu" ;
            Connection conn = DriverManager.getConnection(url, username, password);
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO public.dishes " +
                    "(description, ingridients, link_to_photos, name, price, rating) VALUES (?,?,?,?,?,0) RETURNING id");
            stmt.setString(1,dish.getDescription());
            stmt.setString(2,dish.getIngredients());
            stmt.setString(3,dish.getLink_to_photos());
            stmt.setString(4,dish.getName());
            stmt.setDouble(5,dish.getPrice());

            ResultSet rs = stmt.executeQuery();

            int id=0;

            if(rs.next()) {
                id = rs.getInt("id");
                dish.setId(id);
            }
            stmt.close();
            conn.close();

            optional=Optional.of(dish);
        }
        catch(Exception e) {
            out.println(e);
        }
        return optional;
    }
}
