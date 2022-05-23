package PAO.Entities.Orders;

import PAO.Config.DatabaseConfiguration;
import PAO.Entities.Products.Product;
import PAO.Entities.Products.ProductRepository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderToProductsRepository {

    private static OrderToProductsRepository orderToProductsRepository;

    private OrderToProductsRepository(){}

    public static OrderToProductsRepository getInstance() {
        if (orderToProductsRepository == null)
            orderToProductsRepository = new OrderToProductsRepository();

        return orderToProductsRepository;
    }

    public void createTable() {
        // customerId, ordertype, date, product list
        String createTableSql = "CREATE TABLE IF NOT EXISTS OrderToProducts" +
                "(orderId int NOT NULL," +
                "productId int NOT NULL," +
                "quantity int," +
                "FOREIGN KEY (orderId) references Orders( orderId ), " +
                "FOREIGN KEY (productId) references Products( productId ))";

        Connection connection = DatabaseConfiguration.getDatabaseConnection();

        try {
            Statement stmt = connection.createStatement();
            stmt.execute(createTableSql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addOrder (Order order) {
        String insertOrder = "INSERT INTO OrderToProducts(orderId, productId, quantity) VALUES(?, ?, ?)";

        Connection connection = DatabaseConfiguration.getDatabaseConnection();

        try {
            for( var product : order.getProducts().keySet() ){
                PreparedStatement preparedStatement = connection.prepareStatement( insertOrder );
                preparedStatement.setInt(1, order.getOrderId());
                preparedStatement.setInt(2, product.getProductId());
                preparedStatement.setInt(3, order.getProducts().get(product) );

                preparedStatement.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Map< Product, Integer > getOrderProductList( int orderId) {
        String selectSql = "SELECT * FROM OrderToProducts WHERE OrderId = ?";

        Map< Product, Integer > productList = new HashMap<>();

        Connection connection = DatabaseConfiguration.getDatabaseConnection();

        try {
            ProductRepository productRepository = ProductRepository.getInstance();

            PreparedStatement preparedStatement = connection.prepareStatement(selectSql);
            preparedStatement.setInt(1, orderId );
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next() ){
                productList.put( productRepository.getProductById(resultSet.getInt(2)), resultSet.getInt(3));
            }
            return productList;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<String[]> getOrderToProducts() {
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
                        Integer.toString(resultSet.getInt(3))};
                tableData.add( row );
            }
            return tableData;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
