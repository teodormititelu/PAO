package PAO.Entities.Customers;

import PAO.Config.DatabaseConfiguration;
import PAO.Entities.Addresses.AddressRepository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CustomerRepository {

    private static CustomerRepository customerRepository;

    private CustomerRepository(){}

    public static CustomerRepository getInstance() {
        if (customerRepository == null)
            customerRepository = new CustomerRepository();

        return customerRepository;
    }

    public void createTable() {
        // fname, lname, date, phone, password, addressId
        String createTableSql = "CREATE TABLE IF NOT EXISTS Customers" +
                "(CustomerId int PRIMARY KEY, " +
                "firstName varchar(20), " +
                "lastName varchar(20), " +
                "birthDate varchar(12)," +
                "phoneNumber varchar(15)," +
                "password varchar(30)," +
                "addressId int NOT NULL," +
                "FOREIGN KEY (addressId) references Addresses(addressId))";

        Connection connection = DatabaseConfiguration.getDatabaseConnection();

        try {
            Statement stmt = connection.createStatement();
            stmt.execute(createTableSql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addCustomer(Customer customer) {
        String insertCustomer = "INSERT INTO Customers(CustomerId, firstName, lastName, birthDate, phoneNumber, password, addressId) " +
                "VALUES(?, ?, ?, ?, ?, ?, ?)";

        Connection connection = DatabaseConfiguration.getDatabaseConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement( insertCustomer );
            preparedStatement.setInt(1, customer.getCustomerId());
            preparedStatement.setString(2, customer.getFirstName());
            preparedStatement.setString(3, customer.getLastName());
            preparedStatement.setString(4, customer.getBirthDate().toString());
            preparedStatement.setString( 5, customer.getPhoneNumber());
            preparedStatement.setString(6, customer.getPassword() );
            preparedStatement.setInt(7, customer.getAddress().getAddressId());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Customer getCustomerById( int customerId) {
        String selectSql = "SELECT * FROM Customers WHERE customerId = ?";

        Connection connection = DatabaseConfiguration.getDatabaseConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(selectSql);
            preparedStatement.setInt(1, customerId );
            ResultSet resultSet = preparedStatement.executeQuery();
            return mapToCustomer( resultSet );
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<String[]> getCustomers() {
        List<String[]> tableData = new ArrayList<>();
        String selectSql = "SELECT * FROM Customers";

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
                        resultSet.getString(5),
                        resultSet.getString(6),
                        Integer.toString(resultSet.getInt(7))};
                tableData.add( row );
            }
            return tableData;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private Customer mapToCustomer (ResultSet resultSet) throws SQLException {
        if (resultSet.next()) {
            AddressRepository addressRepository = AddressRepository.getInstance();

            return new Customer( resultSet.getString(2),
                    resultSet.getString(3),
                    LocalDate.parse( resultSet.getString(4) ),
                    resultSet.getString(5),
                    resultSet.getString(6),
                    addressRepository.getAddressById( resultSet.getInt(7) ));
        }

        return null;
    }
}
