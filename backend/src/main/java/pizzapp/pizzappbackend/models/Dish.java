package pizzapp.pizzappbackend.models;

public class Dish {
    private int id;
    private String description;
    private String ingredients;
    private String link_to_photos;
    private String name;
    private double price;
    private double rating;

    public Dish(int id, String description, String ingredients, String link_to_photos, String name, double price, double rating) {
        this.id = id;
        this.description = description;
        this.ingredients = ingredients;
        this.link_to_photos = link_to_photos;
        this.name = name;
        this.price = price;
        this.rating = rating;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getLink_to_photos() {
        return link_to_photos;
    }

    public void setLink_to_photos(String link_to_photos) {
        this.link_to_photos = link_to_photos;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
