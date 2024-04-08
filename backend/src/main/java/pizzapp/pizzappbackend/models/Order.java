package pizzapp.pizzappbackend.models;

import java.sql.Date;
import java.util.ArrayList;

public class Order {
    private int user_id;
    private double cost;
    private String address;
    private ArrayList<OrderProduct> order_products;
    private Date date;


    public Order(int user_id, double cost, String address, ArrayList<OrderProduct> order_products, String date) {
        this.user_id = user_id;
        this.cost = cost;
        this.address = address;
        this.order_products = order_products;
        String[] dateParts=date.split("-");
        this.date = new Date(Integer.parseInt(dateParts[0]),Integer.parseInt(dateParts[1]),Integer.parseInt(dateParts[2]));
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public ArrayList<OrderProduct> getOrder_products() {
        return order_products;
    }

    public void setOrder_products(ArrayList<OrderProduct> order_products) {
        this.order_products = order_products;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
