package PAO.Entities.Services;

import PAO.Entities.Address;
import PAO.Entities.Customers.Customer;
import PAO.Entities.Employees.Deliverer;
import PAO.Entities.Employees.Employee;
import PAO.Entities.Employees.Waiter;
import PAO.Entities.Orders.OnlineOrder;
import PAO.Entities.Orders.Order;
import PAO.Entities.Orders.PhysicalOrder;
import PAO.Entities.Products.Product;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;


public class ReadService{

    private static ReadService singletonInstance;

    private Map < Integer, Customer > customers;
    private List < Employee > employees;
    private List < Order > orders;
    private Map < Integer, Product>  products;
    private Map < Integer, Address > addresses;

    private ReadService()
    {
        customers = new HashMap<>();
        employees = new ArrayList<>();
        orders = new ArrayList<>();
        products = new HashMap<>();
        addresses = new HashMap<>();
    }

    public static ReadService getInstance(){
        if ( singletonInstance == null)
            singletonInstance = new ReadService();
        return singletonInstance;
    }

    public Map<Integer, Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(Map<Integer, Customer> customers) {
        this.customers = customers;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public Map<Integer, Product> getProducts() {
        return products;
    }

    public void setProducts(Map<Integer, Product> products) {
        this.products = products;
    }

    public Map<Integer, Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(Map<Integer, Address> addresses) {
        this.addresses = addresses;
    }

    private static List < String[] > getCSVStrings(String fileName ){
        List < String[] > readData = new ArrayList<>();

        try(var in = new BufferedReader(new FileReader(fileName))){
            String line;
            while( (line = in.readLine()) != null ) {
                String[] data = line.split(",");
                readData.add(data);
            }

        }catch (IOException e) {
            System.out.println("There is no data saved in " + fileName + "!");
            return readData;
        }
        return readData;
    }

    public void parseAddresses(){
        /// st, city, country, postalCode
        var readData = ReadService.getCSVStrings("Data/Addresses.csv");
        for (var objectData : readData) {
            if( objectData != null) {
                Address address = new Address(
                    objectData[0],
                    objectData[1],
                    objectData[2],
                    objectData[3]
            );
                addresses.put( address.getAddressId(), address);
            }
        }
    }

    public void parseCustomers( Map < Integer, Address > addresses ){
        // fname, lname, date, phone, password, addressId
        var readData = ReadService.getCSVStrings("Data/Customers.csv");
        try{
            for (var objectData : readData) {
                if(objectData != null) {
                    int addressId = Integer.parseInt( objectData[5] );
                    Address address = addresses.get( addressId );

                    Customer customer = new Customer( objectData[0]
                            ,objectData[1]
                            ,LocalDate.parse( objectData[2] )
                            ,objectData[3]
                            ,objectData[4]
                            ,address);

                    customers.put( customer.getCustomerId(), customer );
                }
            }
        }catch( DateTimeParseException e ){
            System.out.println( e.toString() );
        }catch( NumberFormatException e ){
            System.out.println( e.toString() );
        }
    }

    public void parseEmployees(){
        var readData = ReadService.getCSVStrings("Data/Employees.csv");

        try{
            for (var objectData : readData) {
                if( objectData != null) {
                    String employeeType = objectData[3];
                    Employee employee;

                    if( employeeType == "Deliverer")
                        employee = new Deliverer(
                                objectData[0],
                                objectData[1],
                                LocalDate.parse( objectData[2] )
                        );
                    else employee = new Waiter(
                            objectData[0],
                            objectData[1],
                            LocalDate.parse( objectData[2] )
                    );

                    employees.add( employee );
                }
            }
        }catch( DateTimeParseException e ){
            System.out.println( e.toString() );
        }

    }

    public void parseProducts (){
        /// name, price, restricted
        var readData = ReadService.getCSVStrings("Data/Products.csv");

        try{
            for (var objectData : readData) {
                if( objectData != null) {
                    Product product = new Product(
                            objectData[0],
                            Integer.parseInt( objectData[1] ),
                            Boolean.parseBoolean( objectData[2] )
                    );
                    products.put( product.getProductId(), product);
                }
            }
        }catch( NumberFormatException e ){
            System.out.println( e.toString() );
        }
    }

    public void parseOrders( Map < Integer, Customer > customers, Map < Integer, Product > products ){
        // customerId, ordertype, date, product list
        var readData = ReadService.getCSVStrings("Data/Orders.csv");
        try {
            for (var objectData : readData) {
                if( objectData != null) {
                    Map < Product, Integer > productList = new HashMap<>();
                    String orderType = objectData[2];
                    Order order;

                    for( int i = 3; i < objectData.length; i += 2 ){
                        productList.put( products.get( Integer.parseInt(objectData[i]) ), Integer.parseInt(objectData[i+1] ) );
                    }

                    if( orderType == "Delivery" )
                        order = new OnlineOrder(
                                productList,
                                customers.get( Integer.parseInt( objectData[0] ) ),
                                LocalDate.parse( objectData[1] )
                        );
                    else order = new PhysicalOrder(
                            productList,
                            customers.get( Integer.parseInt( objectData[0] ) ),
                            LocalDate.parse( objectData[1] )
                    );

                    orders.add( order );
                }
            }
        }catch( DateTimeParseException e ){
            System.out.println( e.toString() );
        } catch (NumberFormatException  e){
            System.out.println(e.toString());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReadService that = (ReadService) o;
        return Objects.equals(getCustomers(), that.getCustomers()) && Objects.equals(getEmployees(), that.getEmployees()) && Objects.equals(getOrders(), that.getOrders()) && Objects.equals(getProducts(), that.getProducts()) && Objects.equals(getAddresses(), that.getAddresses());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCustomers(), getEmployees(), getOrders(), getProducts(), getAddresses());
    }
}

