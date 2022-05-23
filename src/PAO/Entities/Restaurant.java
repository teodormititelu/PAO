package PAO.Entities;

import PAO.Entities.Customers.Customer;
import PAO.Entities.Employees.Employee;
import PAO.Entities.Orders.Order;
import PAO.Entities.Products.Product;
import PAO.Entities.Addresses.Address;

import java.util.*;

public class Restaurant {

    private Map< Integer, Employee> Employees = new HashMap<>();
    private Map < Integer, Customer> Customers = new HashMap<>();
    private Map < Integer, Product> Products = new HashMap<>();

    private List <Order> OrderHistory = new ArrayList<>();

    private String restaurantName;
    private String contact;
    private Address address;
    private String promotions = "We are sorry!!\nThere are no active promotions at the moment.\n";

    public Restaurant(Map<Integer, Employee> employees, Map < Integer, Product> products, String restaurantName, String contact, Address address) {
        Employees = employees;
        Products = products;
        this.restaurantName = restaurantName;
        this.contact = contact;
        this.address = address;
    }

    public Restaurant(Map<Integer, Employee> employees, Map < Integer, Product> products, String restaurantName, String contact, Address address, String promotions) {
        this.Employees = employees;
        this.Products = products;
        this.restaurantName = restaurantName;
        this.contact = contact;
        this.address = address;
        this.promotions = promotions;
    }

    public String getPromotions() {
        return promotions;
    }

    public void setPromotions(String promotions) {
        this.promotions = promotions;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public Map<Integer, Employee> getEmployees() {
        return Employees;
    }

    public void setEmployees(Map<Integer, Employee> employees) {
        Employees = employees;
    }

    public Map<Integer, Customer> getCustomers() {
        return Customers;
    }

    public void setCustomers(Map<Integer, Customer> customers) {
        Customers = customers;
    }

    public Map < Integer, Product>  getProducts() {
        return Products;
    }

    public void setProducts(Map < Integer, Product> products) {
        Products = products;
    }

    public List<Order> getOrderHistory() {
        return OrderHistory;
    }

    public void setOrderHistory(List<Order> orderHistory) {
        OrderHistory = orderHistory;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "RestaurantName = " + restaurantName + "\n"+
                "Contact = " + contact + "\n" +
                "Address:\n" + address.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Restaurant that = (Restaurant) o;
        return Objects.equals(getEmployees(), that.getEmployees()) && Objects.equals(getCustomers(), that.getCustomers()) && Objects.equals(getProducts(), that.getProducts()) && Objects.equals(getOrderHistory(), that.getOrderHistory()) && Objects.equals(getRestaurantName(), that.getRestaurantName()) && Objects.equals(getContact(), that.getContact()) && Objects.equals(getAddress(), that.getAddress()) && Objects.equals(getPromotions(), that.getPromotions());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEmployees(), getCustomers(), getProducts(), getOrderHistory(), getRestaurantName(), getContact(), getAddress(), getPromotions());
    }
}
