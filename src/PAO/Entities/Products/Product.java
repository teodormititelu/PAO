package PAO.Entities.Products;

import java.util.Objects;

public class Product {

    private String name;
    private int price;
    private boolean ageRestricted = false;

    private static int Id = 0;
    private final int productId;

    public Product(String name, int price) {
        Id += 1;
        this.productId = Id;
        this.name = name;
        this.price = price;
    }

    public Product(String name, int price, boolean ageRestricted ) {
        Id += 1;
        this.productId = Id;
        this.name = name;
        this.price = price;
        this.ageRestricted = ageRestricted;
    }

    public int getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public boolean isAgeRestricted(){
        return ageRestricted;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public String toString() {
        String out =  "ProductId: " + productId +
                ", Name: " + name +
                ", Price: " + price + " ron";

        if( ageRestricted == false ) out += ", This product is not age restricted.";
        else out += ", This product is age restricted.";

        return out;
    }

    public String toCSV(){
        /// name, price, restricted
        return this.name + "," + this.price + "," + this.ageRestricted;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return getPrice() == product.getPrice() && isAgeRestricted() == product.isAgeRestricted() && getProductId() == product.getProductId() && Objects.equals(getName(), product.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getPrice(), isAgeRestricted(), getProductId());
    }
}
