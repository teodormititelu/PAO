package PAO;

import PAO.Entities.Address;
import PAO.Entities.Customers.*;
import PAO.Entities.Employees.*;
import PAO.Entities.Orders.*;
import PAO.Entities.Products.Product;
import PAO.Entities.Restaurant;
import PAO.Entities.Services.*;

import java.time.LocalDate;
import java.util.*;

public class Main {
    public static void main( String[] args ){
        Services services = new Services();
        Restaurant SecuPub = new Restaurant( new HashMap<>(), new HashMap<>(), "SecuPub", "07********",
                new Address( "Splaiul Independentei nr.204", "Bucuresti", "Romania", "060024"));

        ReadService readService = ReadService.getInstance();
        WriteService writeService = WriteService.getInstance();

        Audit audit = Audit.getInstance();
        audit.parseActionHistory();

        readService.parseEmployees();
        List < Employee > employees = readService.getEmployees();
        readService.parseProducts();
        Map < Integer, Product > products = readService.getProducts();
        readService.parseAddresses();
        Map < Integer, Address > addresses = readService.getAddresses();
        readService.parseCustomers( addresses );
        Map < Integer, Customer > customers = readService.getCustomers();
        readService.parseOrders( customers, products);
        List < Order > orders = readService.getOrders();

        services.mapEmployees( employees, SecuPub );
        services.mapCustomers( customers, SecuPub );
        services.mapProducts( products, SecuPub );
        services.mapOrders( orders, SecuPub );

        Customer guest = services.getCustomerById( 1, SecuPub );
        Customer customer = guest;

        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while( !exit ) {

            System.out.println("\n\nMENU");
            System.out.println("-----");
            System.out.println("0.Exit");
            System.out.println("1.Show restaurant details");
            System.out.println("2.Show restaurant promotions");
            System.out.println("3.Show product list");
            System.out.println("4.Register as a customer");
            System.out.println("5.Log into your customer account");
            System.out.println("6.Show customer details");
            System.out.println("7.Create an order");
            System.out.println("8.Show order history\n\n");

            int action = scanner.nextInt();
            scanner.nextLine();

            switch (action) {
                case 0: {
                    exit = true;
                    break;
                }
                case 1: {
                    System.out.println("Restaurant details:\n");
                    services.printRestaurantDetails(SecuPub);

                    audit.addAction( "Show restaurant details" );
                    break;
                }
                case 2: {
                    System.out.println("Restaurant promotions:\n");
                    services.printRestaurantPromotions(SecuPub);

                    audit.addAction( "Show restaurant promotions" );
                    break;
                }
                case 3: {
                    System.out.println("Restaurant product list:\n");
                    services.printProductList(SecuPub);

                    audit.addAction( "Display product list" );
                    break;
                }
                case 4: {
                    System.out.println("Register as a customer!\n");

                    System.out.println("First name: ");
                    String firstName = scanner.nextLine();
                    System.out.println("Last name: ");
                    String lastName = scanner.nextLine();

                    System.out.println("Enter date of birth:");
                    System.out.println("Year: ");
                    int year = scanner.nextInt();
                    scanner.nextLine();
                    System.out.println("Month: ");
                    int month = scanner.nextInt();
                    scanner.nextLine();
                    System.out.println("Day: ");
                    int day = scanner.nextInt();
                    scanner.nextLine();
                    LocalDate birthDate = LocalDate.of(year, month, day);

                    System.out.println("Enter your phone number: ");
                    String phoneNumber = scanner.nextLine();

                    System.out.println("Choose a password: ");
                    String password = scanner.nextLine();

                    System.out.println("Enter address details: ");
                    System.out.println("Street: ");
                    String street = scanner.nextLine();
                    System.out.println("City: ");
                    String city = scanner.nextLine();
                    System.out.println("Country: ");
                    String country = scanner.nextLine();
                    System.out.println("Postal code: ");
                    String postalCode = scanner.nextLine();

                    Customer newCustomer = services.createCustomer(firstName, lastName, birthDate, phoneNumber, password,
                            street, city, country, postalCode, SecuPub);
                    writeService.add( newCustomer );

                    System.out.println("Customer registered successfully!!");

                    audit.addAction( "Regsiter customer" );
                    break;
                }
                case 5: {
                    System.out.println("Log into your customer account:\n");
                    System.out.println("Phone number: ");
                    String phoneNumber = scanner.nextLine();
                    System.out.println("Password: ");
                    String password = scanner.nextLine();

                    customer = services.logIntoCustomerAccount(phoneNumber, password, customer, SecuPub);

                    if ( !customer.equals(guest) )
                        System.out.println("Logged successfully!!");
                    else System.out.println("The credentials you entered are incorrect!!");

                    audit.addAction( "Customer login" );
                    break;
                }
                case 6: {
                    System.out.println("Customer details: ");
                    if (customer.equals(guest))
                        System.out.println("You are not logged into your customer account");
                    else services.printCustomerDetails(customer);

                    audit.addAction( "Show customer details" );
                    break;
                }
                case 7: {

                    if (customer.equals(guest)) {
                        System.out.println("Forbidden action, you are not logged into your customer account");
                        break;
                    }

                    System.out.println("Create an order:");
                    products = services.printProductList(SecuPub);
                    System.out.println("This is our product list, enter the product Id and quantity for each product you want to order.");
                    System.out.println("Finish your order by entering -1 as Id");

                    int id, quantity;
                    int totalPrice = 0;

                    Map<Product, Integer> orderedProducts = new HashMap<>();

                    boolean empty = true;
                    do {
                        System.out.println("Id:");
                        id = scanner.nextInt();
                        scanner.nextLine();
                        if (id == -1) break;

                        empty = false;

                        System.out.println("Quantity: ");
                        quantity = scanner.nextInt();
                        scanner.nextLine();

                        if (products.containsKey(id)) {
                            Product product = services.getProductById(id, SecuPub);
                            orderedProducts.put(product, quantity);
                            totalPrice += product.getPrice() * quantity;
                            System.out.println(product.getName() + " was successfully added.");
                        } else System.out.println(id + "is not a valid id.");

                    } while (true);

                    if( empty == true ){
                        System.out.println("Your order is empty!");
                        break;
                    }

                    System.out.println("Order type, choose between Delivery and Physical: ");
                    String orderType = scanner.nextLine();

                    if( services.checkOrder( orderedProducts, customer, SecuPub ) ) {
                        Order order = services.createOrder(orderedProducts, orderType, LocalDate.now(), customer, SecuPub);
                        writeService.add( order );
                    }

                    audit.addAction( "Create order" );
                    break;
                }
                case 8: {

                    if (customer.equals(guest)) {
                        System.out.println("Forbidden action, you are not logged into your customer account");
                        break;
                    }

                    System.out.println("Show order history:");
                    services.printOrderHistory(customer, SecuPub);

                    audit.addAction( "Show customer's order history");
                    break;
                }
            }
        }

        audit.writeAudit();
    }
}
