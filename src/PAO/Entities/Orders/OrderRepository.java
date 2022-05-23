package PAO.Entities.Orders;

import PAO.Config.DatabaseConfiguration;
import PAO.Entities.Customers.Customer;
import PAO.Entities.Customers.CustomerRepository;
import PAO.Entities.Products.Product;
import PAO.Entities.Products.ProductRepository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrderRepository {

    private static OrderRepository orderRepository;

    private OrderRepository(){}

    public static OrderRepository getInstance() {
        if (orderRepository == null)
            orderRepository = new OrderRepository();

        return orderRepository;
    }

    public void createTable() {
        // customerId, ordertype, date, product list
        String createTableSql = "CREATE TABLE IF NOT EXISTS Orders" +
                "(OrderId int PRIMARY KEY, " +
                "customerId int NOT NULL," +
                "FOREIGN KEY (customerId) references Customers(customerId), " +
                "orderType varchar(20), " +
                "date varchar(20))";

        Connection connection = DatabaseConfiguration.getDatabaseConnection();


        try {
            Statement stmt = connection.createStatement();
            stmt.execute(createTableSql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        OrderToProductsRepository otpRepo = OrderToProductsRepository.getInstance();
        otpRepo.createTable();
    }

    public void addOrder(Order order) {
        String insertOrder = "INSERT INTO Orders(orderId, customerId, orderType, date) VALUES(?, ?, ?, ?)";

        Connection connection = DatabaseConfiguration.getDatabaseConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement( insertOrder );
            preparedStatement.setInt(1, order.getOrderId());
            preparedStatement.setInt(2, order.getCustomer().getCustomerId());
            preparedStatement.setString(3, order.getOrderType());
            preparedStatement.setString(4,order.getDate().toString());

            preparedStatement.executeUpdate();

            OrderToProductsRepository otpRepo = OrderToProductsRepository.getInstance();
            otpRepo.addOrder( order );

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Order getOrderById( int emplyeeId) {
        String selectSql = "SELECT * FROM Orders WHERE OrderId = ?";

        Connection connection = DatabaseConfiguration.getDatabaseConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(selectSql);
            preparedStatement.setInt(1, emplyeeId );
            ResultSet resultSet = preparedStatement.executeQuery();
            return mapToOrder( resultSet );
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<String[]> getOrders() {
        List<String[]> tableData = new ArrayList<>();
        String selectSql = "SELECT * FROM Orders";

        Connection connection = DatabaseConfiguration.getDatabaseConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(selectSql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next())
            {
                String[] row = {Integer.toString(resultSet.getInt(1)),
                        Integer.toString(resultSet.getInt(2)),
                        resultSet.getString(3),
                        resultSet.getString(4)};
                tableData.add( row );
            }
            return tableData;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private Order mapToOrder (ResultSet resultSet) throws SQLException {
        if (resultSet.next()) {
            int orderId = resultSet.getInt(1);

            OrderToProductsRepository otpRepository = OrderToProductsRepository.getInstance();
            Map <Product, Integer > productList = otpRepository.getOrderProductList( 1 );

            CustomerRepository customerRepository = CustomerRepository.getInstance();
            Customer customer = customerRepository.getCustomerById( resultSet.getInt(2) );

            String orderType = resultSet.getString(3);
            LocalDate date = LocalDate.parse( resultSet.getString(4) );

            if( orderType.equals("Delivery") )
                return new OnlineOrder( productList, customer, date );
            if( orderType.equals("Physical") )
                return new PhysicalOrder( productList, customer, date );
        }

        return null;
    }
}
