package pizzapp.pizzappbackend.models;

public class OrderProduct {
    private int dish_id;
    private String size;
    private int quantity;

    public OrderProduct(int dish_id, String size, int quantity) {
        this.dish_id = dish_id;
        this.size = size;
        this.quantity = quantity;
    }

    public int getDish_id() {
        return dish_id;
    }

    public void setDish_id(int dish_id) {
        this.dish_id = dish_id;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
