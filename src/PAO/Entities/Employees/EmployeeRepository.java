package PAO.Entities.Employees;

import PAO.Config.DatabaseConfiguration;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmployeeRepository {

    private static EmployeeRepository employeeRepository;

    private EmployeeRepository(){}

    public static EmployeeRepository getInstance() {
        if (employeeRepository == null)
            employeeRepository = new EmployeeRepository();

        return employeeRepository;
    }

    public void createTable() {
        String createTableSql = "CREATE TABLE IF NOT EXISTS Employees" +
                "(employeeId int PRIMARY KEY, " +
                "firstName varchar(20), " +
                "lastName varchar(20), " +
                "birthDate varchar(12)," +
                "employeeType varchar(10))";

        Connection connection = DatabaseConfiguration.getDatabaseConnection();

        try {
            Statement stmt = connection.createStatement();
            stmt.execute(createTableSql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addEmployee(Employee employee) {
        String insertEmployee = "INSERT INTO Employees(employeeId, firstName, lastName, birthDate, employeeType) VALUES(?, ?, ?, ?, ?)";

        Connection connection = DatabaseConfiguration.getDatabaseConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement( insertEmployee );
            preparedStatement.setInt(1, employee.getEmployeeId());
            preparedStatement.setString(2, employee.getFirstName());
            preparedStatement.setString(3, employee.getLastName());
            preparedStatement.setString(4, employee.getBirthDate().toString());

            if(employee instanceof Deliverer)
                preparedStatement.setString(5, "Deliverer");
            else if(employee instanceof Waiter)
                preparedStatement.setString(5, "Waiter");

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Employee getEmployeeById( int emplyeeId) {
        String selectSql = "SELECT * FROM Employees WHERE employeeId = ?";

        Connection connection = DatabaseConfiguration.getDatabaseConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(selectSql);
            preparedStatement.setInt(1, emplyeeId );
            ResultSet resultSet = preparedStatement.executeQuery();
            return mapToEmployee( resultSet );
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<String[]> getEmployees() {
        List<String[]> tableData = new ArrayList<>();
        String selectSql = "SELECT * FROM Employees";

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

    private Employee mapToEmployee (ResultSet resultSet) throws SQLException {
        if (resultSet.next()) {
            if(resultSet.getString(5).equals("Deliverer"))
                return new Deliverer( resultSet.getString(2),
                        resultSet.getString(3),
                        LocalDate.parse( resultSet.getString(4)) );
            else if(resultSet.getString(5).equals("Waiter"))
                return new Waiter( resultSet.getString(2),
                        resultSet.getString(3),
                        LocalDate.parse( resultSet.getString(4)) );
        }

        return null;
    }
}
