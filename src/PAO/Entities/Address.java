package PAO.Entities;

import java.util.Objects;

public class Address {
    private String street;
    private String city;
    private String country;
    private String postalCode;

    private static int Id;
    private final int addressId;

    public Address(String street, String city, String country, String postalCode) {
        Id += 1;
        addressId = Id;

        this.street = street;
        this.city = city;
        this.country = country;
        this.postalCode = postalCode;
    }

    public int getAddressId() {
        return addressId;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String toCSV(){
        /// st, city, country, postalCode
        return this.street + "," + this.city + "," + this.country + "," + this.postalCode;
    }
    @Override
    public String toString() {
        return "\nStreet = " + street +
                "\nCity = " + city +
                "\nCountry = " + country +
                "\nPostal Code = " + postalCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(getStreet(), address.getStreet()) && Objects.equals(getCity(), address.getCity()) && Objects.equals(getCountry(), address.getCountry()) && Objects.equals(getPostalCode(), address.getPostalCode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStreet(), getCity(), getCountry(), getPostalCode());
    }
}
