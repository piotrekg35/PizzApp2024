package pizzapp.pizzappbackend.models;

public class Address {
    private String address;
    private String postCode;
    private String city;

    public Address(String address, String postCode, String city) {
        this.address = address;
        this.postCode = postCode;
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
