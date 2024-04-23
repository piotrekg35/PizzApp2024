package pizzapp.pizzappbackend.services;

import org.springframework.stereotype.Service;
import pizzapp.pizzappbackend.models.Address;
import pizzapp.pizzappbackend.models.Rating;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Optional;

import static java.lang.System.out;

@Service
public class RatingService {
    public ArrayList<Rating> getRatingsForDish(Integer dish_id){
        ArrayList<Rating> arr = new ArrayList<>();
        try {
            // Class.forName("org.apache.derby.jdbc.ClientDriver");
            // url = "jdbc:postgresql://host:port/database"
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://cornelius.db.elephantsql.com:5432/povfpdpt";
            String username = "povfpdpt";
            String password = "LI3dlVj4nt3t7fqcrfFO1HApkOgW2Yvu";
            Connection conn = DriverManager.getConnection(url, username, password);
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM public.ratings WHERE dish_id=?");
            stmt.setInt(1, dish_id);
            ResultSet rs = stmt.executeQuery();

            while(rs.next())  {
                arr.add(new Rating(rs.getInt("user_id"), rs.getInt("dish_id"),
                        rs.getInt("rating"),OrderService.convertDateString(rs.getString("date")),
                        rs.getString("title"),rs.getString("description"), rs.getString("nick")));
            }

            rs.close();
            stmt.close();
            conn.close();
        }
        catch(Exception e)
        {  out.println (e) ; }
        return arr;
    }

    public Optional<Rating> addRating(Rating rating) {
        try
        {
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://cornelius.db.elephantsql.com:5432/povfpdpt";
            String username = "povfpdpt" ;
            String password = "LI3dlVj4nt3t7fqcrfFO1HApkOgW2Yvu" ;
            Connection conn = DriverManager.getConnection(url, username, password);
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO public.ratings " +
                    "(user_id,dish_id,rating,date,title,description,nick) VALUES (?,?,?,?,?,?,?)");
            stmt.setInt(1,rating.getUser_id());
            stmt.setInt(2,rating.getDish_id());
            stmt.setInt(3,rating.getRating());
            stmt.setDate(4,rating.getDate());
            stmt.setString(5,rating.getTitle());
            stmt.setString(6,rating.getDescription());
            stmt.setString(7,rating.getNick());
            stmt.executeUpdate();
            stmt.close();
            conn.close();

            return Optional.of(rating);
        }
        catch(Exception e) {
            out.println(e);
        }
        return Optional.empty();
    }

    public Optional<Rating> delRating(Rating rating) {
        try
        {
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://cornelius.db.elephantsql.com:5432/povfpdpt";
            String username = "povfpdpt" ;
            String password = "LI3dlVj4nt3t7fqcrfFO1HApkOgW2Yvu" ;
            Connection conn = DriverManager.getConnection(url, username, password);
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM public.ratings WHERE user_id=? AND dish_id=?");
            stmt.setInt(1,rating.getUser_id());
            stmt.setInt(2,rating.getDish_id());
            stmt.executeUpdate();
            stmt.close();
            conn.close();

            return Optional.of(rating);
        }
        catch(Exception e) {
            out.println(e);
        }
        return Optional.empty();
    }
}
