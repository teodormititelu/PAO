package PAO.Entities.Services;

import PAO.Entities.Addresses.Address;
import PAO.Entities.Addresses.AddressRepository;
import PAO.Entities.Customers.Customer;
import PAO.Entities.Customers.CustomerRepository;
import PAO.Entities.Employees.Deliverer;
import PAO.Entities.Employees.Employee;
import PAO.Entities.Employees.EmployeeRepository;
import PAO.Entities.Employees.Waiter;
import PAO.Entities.Orders.OnlineOrder;
import PAO.Entities.Orders.Order;
import PAO.Entities.Orders.OrderRepository;
import PAO.Entities.Orders.PhysicalOrder;
import PAO.Entities.Products.Product;
import PAO.Entities.Products.ProductRepository;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Stream;


public class WriteService{

    private static WriteService singletonInstance;

    private Map < Integer, Customer > customers;
    private List < Employee > employees;
    private List < Order > orders;
    private Map < Integer, Product>  products;
    private Map < Integer, Address > addresses;


    private WriteService()
    {
        customers = ReadService.getInstance().getCustomers();
        employees = ReadService.getInstance().getEmployees();
        orders = ReadService.getInstance().getOrders();
        products = ReadService.getInstance().getProducts();
        addresses = ReadService.getInstance().getAddresses();
    }

    public static WriteService getInstance(){
        if ( singletonInstance == null)
            singletonInstance = new WriteService();
        return singletonInstance;
    }

    public void writeAddresses(){
        try{
            Stream < Address > addressStream = addresses.values().stream();
            var writer = new FileWriter("Data/Addresses.csv");
            Consumer < Address > consumer = address -> {
                try {
                    writer.write( address.toCSV());
                    writer.write("\n");

                } catch (IOException e) {
                    System.out.println(e.toString());
                }
            };
            addressStream.forEach(consumer);
            writer.close();
        }catch (IOException e){
            System.out.println(e.toString());
        }
    }

    public void writeCustomers(){
        try{
            Stream < Customer > customersStream = customers.values().stream();
            var writer = new FileWriter("Data/Customers.csv");
            Consumer < Customer > consumer = customer -> {
                try {
                    writer.write( customer.toCSV());
                    writer.write("\n");

                } catch (IOException e) {
                    System.out.println(e.toString());
                }
            };
            customersStream.forEach(consumer);
            writer.close();
        }catch (IOException e){
            System.out.println(e.toString());
        }
    }

    public void writeProducts(){
        try{
            Stream < Product > productStream = products.values().stream();
            var writer = new FileWriter("Data/Products.csv");
            Consumer < Product > consumer = product -> {
                try {
                    writer.write( product.toCSV());
                    writer.write("\n");

                } catch (IOException e) {
                    System.out.println(e.toString());
                }
            };
            productStream.forEach(consumer);
            writer.close();
        }catch (IOException e){
            System.out.println(e.toString());
        }
    }

    public void writeEmployees(){
        try{
            Stream < Employee > employeeStream = employees.stream();
            var writer = new FileWriter("Data/employees.csv");
            Consumer < Employee > consumer = employee -> {
                try {
                    writer.write( employee.toCSV());
                    writer.write("\n");

                } catch (IOException e) {
                    System.out.println(e.toString());
                }
            };
            employeeStream.forEach(consumer);
            writer.close();
        }catch (IOException e){
            System.out.println(e.toString());
        }
    }

    public void writeOrders(){
        try{
            Stream < Order > orderStream = orders.stream();
            var writer = new FileWriter("Data/orders.csv");
            Consumer < Order > consumer = order -> {
                try {
                    writer.write( order.toCSV());
                    writer.write("\n");

                } catch (IOException e) {
                    System.out.println(e.toString());
                }
            };
            orderStream.forEach(consumer);
            writer.close();
        }catch (IOException e){
            System.out.println(e.toString());
        }
    }

    public < T > void set( List< T > list ) {
        if(!list.isEmpty() && list.get(0) instanceof Employee) {
            this.employees = (List < Employee >) list;
        }
        else if(!list.isEmpty() && list.get(0) instanceof Order) {
            this.orders = (List < Order >) list;
        }
    }

    public < T > void set( Map < Integer, T > map ){
        if( !map.isEmpty() && map.values().toArray()[0] instanceof Product ){
            this.products = (Map<Integer, Product>) map;
        }
        else if( !map.isEmpty() && map.values().toArray()[0] instanceof Address ){
            this.addresses = (Map<Integer, Address>) map;
        }
        else if( !map.isEmpty() && map.values().toArray()[0] instanceof Customer ){
            this.customers = (Map<Integer, Customer>) map;
        }
    }

    public < T > void add( T entity){
        String fileName = null;
        if(entity instanceof Employee)
        {
            fileName = "Data/Employee.csv";
            employees.add((Employee) entity);
        }
        else if(entity instanceof Address)
        {
            fileName = "Data/Addresses.csv";
            Address address = (Address) entity;
            addresses.put(address.getAddressId(), address);
        }
        else if(entity instanceof  Customer)
        {
            fileName = "Data/Customers.csv";
            Customer customer = (Customer) entity;
            customers.put(customer.getCustomerId(), customer);
        }
        else if(entity instanceof  Product)
        {
            fileName = "Data/Products.csv";
            Product product = (Product) entity;
            products.put(product.getProductId(), product);
        }
        else if(entity instanceof Order)
        {
            fileName = "Data/Orders.csv";
            orders.add( (Order) entity);
        }

        try( var writer = new FileWriter(fileName, true) ) {
            if(entity instanceof Employee)
                writer.write(((Employee) entity).toCSV());
            else if(entity instanceof Address) {
                writer.write(((Address) entity).toCSV());
            }
            else if(entity instanceof Customer)
                writer.write(((Customer) entity).toCSV());
            else if(entity instanceof Product)
                writer.write(((Product) entity).toCSV());
            else if(entity instanceof Order)
                writer.write(((Order) entity).toCSV());
            writer.write("\n");

        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    public < T > void addJDBC( T entity){
        String fileName = null;
        if(entity instanceof Employee)
        {
            EmployeeRepository employeeRepo = EmployeeRepository.getInstance();
            employeeRepo.addEmployee( (Employee) entity);
            employees.add( (Employee) entity );
        }
        else if(entity instanceof Address)
        {
            AddressRepository addressRepo = AddressRepository.getInstance();
            addressRepo.addAddress((Address) entity);
            Address address = (Address) entity;
            addresses.put( address.getAddressId(), address );
        }
        else if(entity instanceof  Customer)
        {
            CustomerRepository customerRepo = CustomerRepository.getInstance();
            customerRepo.addCustomer((Customer) entity);
            Customer customer = (Customer) entity;
            customers.put( customer.getCustomerId(), customer );
        }
        else if(entity instanceof  Product)
        {
            ProductRepository productRepo = ProductRepository.getInstance();
            productRepo.addProduct((Product) entity);
            Product product = (Product) entity;
            products.put( product.getProductId(), product );
        }
        else if(entity instanceof Order)
        {
            OrderRepository orderRepo =  OrderRepository.getInstance();
            orderRepo.addOrder( (Order) entity );
            orders.add( (Order) entity );
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WriteService that = (WriteService) o;
        return Objects.equals(customers, that.customers) && Objects.equals(employees, that.employees) && Objects.equals(orders, that.orders) && Objects.equals(products, that.products) && Objects.equals(addresses, that.addresses);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customers, employees, orders, products, addresses);
    }
}
