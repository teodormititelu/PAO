package PAO.Entities.Orders;

import PAO.Entities.Customers.Customer;
import PAO.Entities.Products.Product;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Date;

public class Order {

    protected Map < Product, Integer > products;
    protected Customer customer;
    protected String orderType = null;
    protected final LocalDate date;

    private static int Id = 0;
    private final int orderId;

    public Order(Map < Product, Integer > products, Customer customer, LocalDate date) {
        Id += 1;
        this.orderId = Id;
        this.products = products;
        this.customer = customer;
        this.date = date;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public int getOrderId() {
        return orderId;
    }

    public Map < Product, Integer >  getProducts() {
        return products;
    }

    public void setProducts(Map < Product, Integer > products) {
        this.products = products;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public String toString() {
        String out;
        out = "\nOrder Type: " + orderType + "\nDate: " + date;
        for( var product : products.keySet() )
            out += "\n" + product.getName() + ", quantity: " + products.get( product );

        return out;
    }

    public String toCSV(){
        // customerId, ordertype, date, product list
        String csv = this.customer.getCustomerId() + "," + this.orderType + "," + this.date;

        for( var product : this.products.keySet() ){
            csv += "," + product.getProductId() + "," + products.get( product );
        }

        return csv;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return getOrderId() == order.getOrderId() && Objects.equals(getProducts(), order.getProducts()) && Objects.equals(getCustomer(), order.getCustomer()) && Objects.equals(getOrderType(), order.getOrderType()) && Objects.equals(getDate(), order.getDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getProducts(), getCustomer(), getOrderType(), getDate(), getOrderId());
    }
}
