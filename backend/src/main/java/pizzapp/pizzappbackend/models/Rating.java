package pizzapp.pizzappbackend.models;

import java.sql.Date;

public class Rating {
    private int user_id;
    private int dish_id;
    private int rating;
    private Date date;
    private String title;
    private String description;
    private String nick;

    public Rating(int user_id, int dish_id, int rating, String date, String title, String description, String nick) {
        this.user_id = user_id;
        this.dish_id = dish_id;
        this.rating = rating;
        String[] dateParts=date.split("-");
        this.date = new Date(Integer.parseInt(dateParts[0]),Integer.parseInt(dateParts[1]),Integer.parseInt(dateParts[2]));
        this.title = title;
        this.description = description;
        this.nick=nick;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getDish_id() {
        return dish_id;
    }

    public void setDish_id(int dish_id) {
        this.dish_id = dish_id;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }
}
