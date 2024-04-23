package pizzapp.pizzappbackend.services;

import org.springframework.stereotype.Service;
import pizzapp.pizzappbackend.models.Address;
import pizzapp.pizzappbackend.models.Order;
import pizzapp.pizzappbackend.models.OrderProduct;

import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;

import static java.lang.System.out;

@Service
public class OrderService {
    public Optional<Order> processOrder(Order order) {
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
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO public.orders (user_id, date, cost, address)" +
                    " VALUES(?,?,?,?) RETURNING order_id");
            stmt.setInt(1, order.getUser_id());
            stmt.setDate(2, order.getDate());
            stmt.setDouble(3, order.getCost());
            stmt.setString(4, order.getAddress());
            ResultSet rs = stmt.executeQuery();

            int order_id=-1;

            if(rs.next()) {
                order_id = rs.getInt("order_id");
            }

            rs.close();
            stmt.close();

            for(OrderProduct op : order.getOrder_products())
            {
                stmt = conn.prepareStatement("INSERT INTO public.order_products (order_id, dish_id, size, quantity) " +
                        "VALUES (?,?,?,?)");
                stmt.setInt(1, order_id);
                stmt.setInt(2, op.getDish_id());
                stmt.setString(3, op.getSize());
                stmt.setInt(4, op.getQuantity());
                stmt.executeUpdate();
                stmt.close();

            }

            conn.close();
            optional=Optional.of(order);

        }
        catch(Exception e)
        {
            out.println (e) ;
        }
        return optional;
    }
    public static String convertDateString(String date){
        String[] dateParts=date.split("-");
        int year = Integer.parseInt(dateParts[0])-1900;
        int month = Integer.parseInt(dateParts[1])-1;
        String day = dateParts[2];
        return year + "-"+month+ "-"+day;
    }
    public ArrayList<Order> getOrders(Integer id) {
        ArrayList<Order> arr = new ArrayList<>();
        try
        {
            // Class.forName("org.apache.derby.jdbc.ClientDriver");
            // url = "jdbc:postgresql://host:port/database"
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://cornelius.db.elephantsql.com:5432/povfpdpt";
            String username = "povfpdpt" ;
            String password = "LI3dlVj4nt3t7fqcrfFO1HApkOgW2Yvu" ;
            Connection conn = DriverManager.getConnection(url, username, password);
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM public.orders NATURAL JOIN public.order_products " +
                    "WHERE user_id=? ORDER BY order_id");
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            int prevOrderId=0;
            ArrayList<OrderProduct> ops = new ArrayList<>();
            Order order = null;

            while(rs.next())  {
                if(prevOrderId==0){
                    prevOrderId=rs.getInt("order_id");
                    OrderProduct op =new OrderProduct(rs.getInt("dish_id"), rs.getString("size"),
                            rs.getInt("quantity"));
                    ops.add(op);
                    order = new Order(id,rs.getDouble("cost"),rs.getString("address"),ops,
                            convertDateString(rs.getDate("date").toString()));
                } else if (prevOrderId==rs.getInt("order_id")) {
                    OrderProduct op =new OrderProduct(rs.getInt("dish_id"), rs.getString("size"),
                            rs.getInt("quantity"));
                    ops.add(op);
                } else {
                    arr.add(order);
                    ops=new ArrayList<>();
                    OrderProduct op =new OrderProduct(rs.getInt("dish_id"), rs.getString("size"),
                            rs.getInt("quantity"));
                    ops.add(op);
                    order = new Order(id,rs.getDouble("cost"),rs.getString("address"),ops,
                            convertDateString(rs.getDate("date").toString()));
                    prevOrderId=rs.getInt("order_id");
                }

            }
            arr.add(order);
            rs.close();
            stmt.close();
            conn.close();
        }
        catch(Exception e)
        {  out.println (e) ; }
        return arr;
    }
}
