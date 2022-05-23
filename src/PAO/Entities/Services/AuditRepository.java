package PAO.Entities.Services;

import PAO.Config.DatabaseConfiguration;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AuditRepository {

    private final DateTimeFormatter formatter;
    private static AuditRepository auditRepository;

    private AuditRepository(){
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    }

    public static AuditRepository getInstance() {
        if (auditRepository == null)
            auditRepository = new AuditRepository();

        return auditRepository;
    }

    public void createTable() {
        String createTableSql = "CREATE TABLE IF NOT EXISTS audit" +
                "(id int PRIMARY KEY AUTO_INCREMENT, action varchar(100), datetime varchar(20))";

        Connection connection = DatabaseConfiguration.getDatabaseConnection();

        try {
            Statement stmt = connection.createStatement();
            stmt.execute(createTableSql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addAction(String action) {
        String insertActionSql = "INSERT INTO audit(action, datetime) VALUES(?, ?)";

        Connection connection = DatabaseConfiguration.getDatabaseConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(insertActionSql);
            String datetime = formatter.format(LocalDateTime.now());
            preparedStatement.setString(1, action);
            preparedStatement.setString(2, datetime);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getActionById(int id) {
        String selectSql = "SELECT * FROM audit WHERE id = ?";

        Connection connection = DatabaseConfiguration.getDatabaseConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(selectSql);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.getString(1) + "," + resultSet.getString(2);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
