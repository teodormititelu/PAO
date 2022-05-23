package PAO.Entities.Addresses;

import PAO.Config.DatabaseConfiguration;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AddressRepository {

    private static AddressRepository addressRepository;

    private AddressRepository(){}

    public static AddressRepository getInstance() {
        if (addressRepository == null)
            addressRepository = new AddressRepository();

        return addressRepository;
    }

    public void createTable() {
        String createTableSql = "CREATE TABLE IF NOT EXISTS Addresses" +
                "(addressId int PRIMARY KEY, " +
                "street varchar(50), " +
                "city varchar(20), " +
                "country varchar(20)," +
                "postalCode varchar(10))";

        Connection connection = DatabaseConfiguration.getDatabaseConnection();

        try {
            Statement stmt = connection.createStatement();
            stmt.execute(createTableSql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addAddress(Address address) {
        String insertAddress = "INSERT INTO Addresses(addressId, street, city, country, postalCode) VALUES(?, ?, ?, ?, ?)";

        Connection connection = DatabaseConfiguration.getDatabaseConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement( insertAddress );
            preparedStatement.setInt(1, address.getAddressId());
            preparedStatement.setString(2, address.getStreet());
            preparedStatement.setString(3, address.getCity());
            preparedStatement.setString(4, address.getCountry());
            preparedStatement.setString( 5, address.getPostalCode());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Address getAddressById( int addressId) {
        String selectSql = "SELECT * FROM Addresses WHERE addressId = ?";

        Connection connection = DatabaseConfiguration.getDatabaseConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(selectSql);
            preparedStatement.setInt(1, addressId );
            ResultSet resultSet = preparedStatement.executeQuery();
            return mapToAddress( resultSet );
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<String[]> getAddresss() {
        List<String[]> tableData = new ArrayList<>();
        String selectSql = "SELECT * FROM Addresses";

        Connection connection = DatabaseConfiguration.getDatabaseConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(selectSql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next())
            {
                String[] row = {Integer.toString(resultSet.getInt(1)),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getString(5)};
                tableData.add( row );
            }
            return tableData;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private Address mapToAddress (ResultSet resultSet) throws SQLException {
        if (resultSet.next()) {
            return new Address( resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5) );
        }

        return null;
    }
}
