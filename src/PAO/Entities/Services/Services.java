package PAO.Entities.Services;

import PAO.Entities.Customers.Customer;
import PAO.Entities.Employees.*;
import PAO.Entities.Orders.*;
import PAO.Entities.Products.Product;
import PAO.Entities.Addresses.Address;
import PAO.Entities.Restaurant;

import java.time.LocalDate;
import java.time.Period;
import java.util.*;

public class Services {

    public void addCustomer( Customer customer, Restaurant restaurant){
        Map < Integer, Customer> Customers = restaurant.getCustomers();
        Customers.put( customer.getCustomerId(), customer );
        restaurant.setCustomers( Customers );
    }

    public Customer createCustomer(String firstName, String lastName, LocalDate birthDate, String phoneNumber, String password, String street, String city, String country, String postalCode, Restaurant restaurant) {
        Address address = new Address( street, city, country, postalCode );
        Customer customer = new Customer( firstName, lastName, birthDate, phoneNumber, password, address );

        addCustomer( customer, restaurant );

        return customer;
    }

    public void addEmployee( Employee employee, Restaurant restaurant){
        Map < Integer, Employee> Employees = restaurant.getEmployees();
        Employees.put( employee.getEmployeeId(), employee );
        restaurant.setEmployees( Employees );
    }

    public void removeEmployee( Employee employee, Restaurant restaurant){
        Map < Integer, Employee> Employees = restaurant.getEmployees();
        Employees.remove( employee.getEmployeeId() );
        restaurant.setEmployees( Employees );
    }

    public Employee createEmployee( String firstName, String lastName, LocalDate birthDate , String emplpyeeType, Restaurant restaurant ){
        Employee employee;

        if( emplpyeeType == "Deliverer" )
            employee = new Deliverer( firstName, lastName, birthDate );
        else employee = new Waiter(firstName, lastName, birthDate);

        addEmployee( employee, restaurant );

        return employee;
    }

    public void addOrder( Order order, Customer customer, Restaurant restaurant ){
        List < Order > restaurantOrders = restaurant.getOrderHistory();
        restaurantOrders.add( order );

        restaurant.setOrderHistory( restaurantOrders );
    }

    public boolean checkOrder( Map < Product, Integer > products, Customer customer, Restaurant restaurant ){
        int customerAge = Period.between(customer.getBirthDate(), LocalDate.now()).getYears();

        for( var product : products.keySet() ){
            if( product.isAgeRestricted() == true && customerAge < 18 ){
                System.out.println( "Comanda nu se poate efectua deoarece clientul nu are varsta legala necesara " +
                        "pentru a comanda anumite produse din lista." );
                return false;
            }
        }

        return true;
    }

    public Order createOrder( Map < Product, Integer > products, String orderType, LocalDate date, Customer customer, Restaurant restaurant ){
        Order order;
        System.out.println( orderType );

        if( orderType.equals( "Delivery" ) )
            order = new OnlineOrder( products, customer, date );
        else order = new PhysicalOrder( products, customer, date );

        addOrder( order, customer, restaurant );

        System.out.println( "Comanda a fost adaugata cu succes." );

        return order;
    }

    public void addProduct( Product product, Restaurant restaurant ){
        Map < Integer, Product>  productList = restaurant.getProducts();
        productList.put( product.getProductId(), product );
        restaurant.setProducts( productList );
    }

    public void removeProduct( Product product, Restaurant restaurant ){
        Map < Integer, Product>  productList = restaurant.getProducts();
        productList.remove( product.getProductId() );
        restaurant.setProducts( productList );
    }

    public Product createProduct( String name, int price, boolean ageRestricted, Restaurant restaurant){
        Product product = new Product( name, price, ageRestricted );
        addProduct( product, restaurant);
        return product;
    }

    public Employee getEmployeeById ( int Id, Restaurant restaurant ){
        Map < Integer, Employee> Employees = restaurant.getEmployees();
        return Employees.get( Id );
    }

    public Customer getCustomerById ( int Id, Restaurant restaurant ){
        Map < Integer, Customer> Customers = restaurant.getCustomers();
        return Customers.get( Id );
    }

    public List< Order > getCustomerOrderHistory ( Customer customer, Restaurant restaurant ){
        List < Order > CustomerOrderHsitory = new ArrayList<>();
        List < Order > RestaurantOrderHistory = restaurant.getOrderHistory();

        for( var order : RestaurantOrderHistory ){
            if( order.getCustomer().equals(customer) )
                CustomerOrderHsitory.add( order );
        }

        return CustomerOrderHsitory;
    }

    public void printCustomerOrderHistory( Customer customer, Restaurant restaurant ){
        List < Order > CustomerOrderHistory = getCustomerOrderHistory(customer, restaurant);
        System.out.println( CustomerOrderHistory );
    }

    public void printCustomerOrderHistory( int Id, Restaurant restaurant ){
        Customer customer = getCustomerById( Id, restaurant );
        printCustomerOrderHistory( customer, restaurant );
    }

    public int getOrderPrice( Order order ){
        Map < Product, Integer > products = order.getProducts();
        int price = 0;
        for( var product : products.keySet() )
            price += product.getPrice() * products.get( product );
        return price;
    }

    public List < Order > printOrderHistory( Customer customer, Restaurant restaurant ){
        List < Order > orderHistory = restaurant.getOrderHistory();
        List < Order > customerOrderHistory = new ArrayList<>();

        for( var order : orderHistory ){
            if( order.getCustomer().equals( customer ) )
                customerOrderHistory.add( order );
        }

        if( customerOrderHistory.size() == 0 )
            System.out.println("Your order history is empty.");

        for( var order : customerOrderHistory ) {
            System.out.println( order.toString() );
            int price = getOrderPrice( order );
            System.out.println("Total price: " + price );
        }

        return customerOrderHistory;
    }

    public void printRestaurantDetails( Restaurant restaurant ){
        System.out.println( restaurant.toString() );
    }

    public void printRestaurantPromotions(Restaurant restaurant) {
        System.out.println( restaurant.getPromotions() );
    }

    public Product getProductById( int id, Restaurant restaurant ){
        Map < Integer, Product>  ProductList = restaurant.getProducts();
        return ProductList.get( id );
    }

    public Map < Integer, Product> printProductList( Restaurant restaurant ){
        Map < Integer, Product>  ProductList = restaurant.getProducts();

        for( var product : ProductList.values() ){
            System.out.println( product.toString() );
        }
        return ProductList;
    }

    public Customer logIntoCustomerAccount(String phoneNumber, String password, Customer client, Restaurant restaurant) {
        Map< Integer, Customer > Customers = restaurant.getCustomers();


        for( var customer : Customers.values() ){

            if( customer.getPhoneNumber().equals(phoneNumber) && customer.getPassword().equals( password ) )
                return customer;
        }

        return client;
    }

    public void printCustomerDetails(Customer customer) {
        System.out.println( customer.toString() );
    }

    public void mapEmployees( List < Employee > employees, Restaurant restaurant ){
        for( var employee : employees )
            addEmployee( employee, restaurant );
    }

    public void mapCustomers(Map<Integer, Customer> customers, Restaurant restaurant) {
        restaurant.setCustomers( customers );
    }

    public void mapProducts(Map<Integer, Product> products, Restaurant restaurant) {
        restaurant.setProducts( products );
    }

    public void mapOrders(List<Order> orders, Restaurant restaurant) {
        restaurant.setOrderHistory( orders );
    }
}
