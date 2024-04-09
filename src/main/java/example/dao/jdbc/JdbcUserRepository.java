package example.dao.jdbc;


import example.User;
import example.UserRole;
import example.dao.IUserRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

public class JdbcUserRepository implements IUserRepository {
    private static JdbcUserRepository instance;
    private final DatabaseManager databaseManager;

    private static String GET_ALL_USERS_SQL = "SELECT login, password, role, rentedID FROM tuser";
    private static String GET_USER_SQL = "SELECT * FROM tuser WHERE login LIKE ?";
    private static String REMOVE_USER_SQL = "DELETE * FROM tuser WHERE login LIKE ?";
    private static String INSERT_USER_SQL = "INSERT INTO tuser (login, password, role ,rentedID) VALUES (?, ?, ?, ?)";


    private JdbcUserRepository() {
        this.databaseManager = DatabaseManager.getInstance();
    }


    @Override
    public User getUser(String login) {
        User user = null;
        Connection connection =null;
        ResultSet rs = null;
        PreparedStatement preparedStatement = null;
        try{
            connection = databaseManager.getConnection();
            connection.createStatement();
            preparedStatement = connection.prepareStatement(GET_USER_SQL);
            preparedStatement.setString(1, login);
            rs = preparedStatement.executeQuery();
            if(rs.next()) {

                user = new User(
                    rs.getString("login"),
                    rs.getString("password"),
                    UserRole.valueOf(rs.getString("role"))
                );

                int rentedID = rs.getInt("rentedID");
                user.setRentedVehicleID( (rentedID==0)?null: rentedID);

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        finally {

            try {
                if (rs != null)
                    rs.close();

                if (connection != null)
                    connection.close();

                if (preparedStatement != null)
                    preparedStatement.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return user;
    }

    @Override
    public void addUser(User user) {
        Connection connection =null;
        PreparedStatement preparedStatement = null;

        Integer vehicleID = user.getRentedVehicleID();
        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(INSERT_USER_SQL);
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getRole().toString());
            preparedStatement.setInt(4, (vehicleID == null)?0:vehicleID);
            preparedStatement.executeUpdate();
        } catch (SQLException e){}
        finally {

            try{
                if(preparedStatement != null) preparedStatement.close();
                if(connection != null) connection.close();
            }catch (SQLException e){ throw new RuntimeException();}
        }
    }

    @Override
    public void removeUser(String login) {
        Connection connection =null;
        PreparedStatement return_vehicle_statement = null;
        PreparedStatement remove_user_statement = null;


        User user = getUser(login); // We need this to check if user has rented any vehicle
        if (user == null) return;

        try{
            connection = databaseManager.getConnection();
            connection.setAutoCommit(false);

            if(user.getRentedVehicleID() != null){
                return_vehicle_statement = connection.prepareStatement( "UPDATE tvehicle SET rent = 0 WHERE id LIKE ? AND rent = 1");
                return_vehicle_statement.setInt(1, user.getRentedVehicleID());
            }

            remove_user_statement = connection.prepareStatement(REMOVE_USER_SQL);
            remove_user_statement.setString(1, user.getLogin());

            int removed = remove_user_statement.executeUpdate();
            int returned_vehicles = (return_vehicle_statement != null)? return_vehicle_statement.executeUpdate(): 0;

            boolean success = removed > 0 && (returned_vehicles > 0 || user.getRentedVehicleID() == null);

            if(success)
                connection.commit();
            else
                connection.rollback();


        } catch (SQLException e) {

            try{if (connection != null)
                connection.rollback();
            } catch (SQLException ignored){}

            throw  new RuntimeException();
        } finally {
            try{
                if (connection != null) connection.close();
                if (return_vehicle_statement != null) return_vehicle_statement.close();
                if (remove_user_statement != null) remove_user_statement.close();
            } catch (SQLException e){
                throw new RuntimeException();
            }
        }
    }

    @Override
    public Collection<User> getUsers() {
        Collection<User> users = new ArrayList<>();
        try(Connection connection = databaseManager.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(GET_ALL_USERS_SQL)) {
            while(resultSet.next()){
                String login = resultSet.getString("login");
                String password = resultSet.getString("password");
                UserRole role = UserRole.valueOf(resultSet.getString("role"));
                Integer id = resultSet.getInt("rentedID");
                id = (id == 0)? null: id; // Check for null value

                User user = new User(login, password, role, id);
                users.add(user);
            }
        }catch (SQLException e){
                e.printStackTrace();
        }
        return users;
    }


    public static JdbcUserRepository getInstance(){
        if (JdbcUserRepository.instance == null){
            JdbcUserRepository.instance = new JdbcUserRepository();
        }
        return instance;
    }

}
