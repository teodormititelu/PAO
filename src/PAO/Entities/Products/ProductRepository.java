package PAO.Entities.Products;

import PAO.Config.DatabaseConfiguration;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProductRepository {

    private static ProductRepository productRepository;

    private ProductRepository(){}

    public static ProductRepository getInstance() {
        if (productRepository == null)
            productRepository = new ProductRepository();

        return productRepository;
    }

    public void createTable() {
        String createTableSql = "CREATE TABLE IF NOT EXISTS Products" +
                "(productId int PRIMARY KEY, " +
                "name varchar(20), " +
                "price int, " +
                "ageRestricted boolean)";

        Connection connection = DatabaseConfiguration.getDatabaseConnection();

        try {
            Statement stmt = connection.createStatement();
            stmt.execute(createTableSql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addProduct(Product product) {
        String insertProduct = "INSERT INTO Products(ProductId, name, price, ageRestricted) VALUES(?, ?, ?, ?)";

        Connection connection = DatabaseConfiguration.getDatabaseConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement( insertProduct );
            preparedStatement.setInt(1, product.getProductId());
            preparedStatement.setString(2, product.getName());
            preparedStatement.setInt(3, product.getPrice());
            preparedStatement.setBoolean(4, product.isAgeRestricted());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Product getProductById( int productId) {
        String selectSql = "SELECT * FROM Products WHERE productId = ?";

        Connection connection = DatabaseConfiguration.getDatabaseConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(selectSql);
            preparedStatement.setInt(1, productId );
            ResultSet resultSet = preparedStatement.executeQuery();
            return mapToProduct( resultSet );
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<String[]> getProducts() {
        List<String[]> tableData = new ArrayList<>();
        String selectSql = "SELECT * FROM Products";

        Connection connection = DatabaseConfiguration.getDatabaseConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(selectSql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next())
            {
                String[] row = {Integer.toString(resultSet.getInt(1)),
                        resultSet.getString(2),
                        Integer.toString(resultSet.getInt(3)),
                        Boolean.toString(resultSet.getBoolean(4)) };
                tableData.add( row );
            }
            return tableData;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private Product mapToProduct (ResultSet resultSet) throws SQLException {
        if (resultSet.next()) {
            return new Product( resultSet.getString(2),
                    resultSet.getInt(3),
                    resultSet.getBoolean(4));
        }

        return null;
    }
}
