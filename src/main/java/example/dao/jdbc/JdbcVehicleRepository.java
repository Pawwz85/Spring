package example.dao.jdbc;

import example.Car;
import example.Motorcycle;
import example.Vehicle;
import example.dao.IVehicleRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

public class JdbcVehicleRepository implements IVehicleRepository {
    private static JdbcVehicleRepository instance;
    private final DatabaseManager manager;
    private final String GET_ALL_VEHICLE_SQL = "SELECT * FROM tvehicle";
    private final String GET_VEHICLE_SQL = "SELECT * FROM tvehicle where id Like ?";
    private final String RENT_CAR_SQL = "UPDATE tvehicle SET rent = 1 WHERE id LIKE ? AND rent = 0";
    private final  String RENT_UPDATE_USER_SQL = "UPDATE tuser SET rentedID = ? WHERE login LIKE ? AND rentedID IS NULL";
    private final String RETURN_CAR_SQL = "UPDATE tvehicle SET rent = 0 WHERE id LIKE ? AND rent = 1";
    private final  String RETURN_UPDATE_USER_SQL = "UPDATE tuser SET rentedID = NULL WHERE login LIKE ? AND rentedID IS NOT NULL";
    private final String REMOVE_VEHICLE_SQL = "DELETE FROM tvehicle WHERE id LIKE ?";
    private final String INSERT_VEHICLE_SQL = "INSERT INTO tvehicle (brand, model, year, price, id, category) VALUES (?,?,?,?,?,?)";




    public static JdbcVehicleRepository getInstance(){
        if (JdbcVehicleRepository.instance==null){
            instance = new JdbcVehicleRepository();
        }
        return instance;
    }

    private JdbcVehicleRepository() {
        this.manager = DatabaseManager.getInstance();
    }


    @Override
    public boolean rentVehicle(int id, String login) {
        Connection conn = null;
        PreparedStatement rentCarStmt = null;
        PreparedStatement updateUserStmt = null;
        boolean result = false;

        try {
            conn = manager.getConnection();
            conn.setAutoCommit(false); // reczny commit

                                rentCarStmt = conn.prepareStatement(RENT_CAR_SQL);
                                rentCarStmt.setInt(1, id);
                                int changed1 =rentCarStmt.executeUpdate();

                                updateUserStmt = conn.prepareStatement(RENT_UPDATE_USER_SQL);
                                updateUserStmt.setInt(1, id);
                                updateUserStmt.setString(2, login);
                                int changed2 =updateUserStmt.executeUpdate();

                                result = changed1 > 0 && changed2 > 0;
                                if (result) {
                                    System.out.println("wypozyczono");
                                    conn.commit();
                                } else {
                                    System.out.println("Nie wypożyczono");
                                    conn.rollback(); // wycofuje zmiany
                                }

        } catch(Exception e) {
            e.printStackTrace();
            if (conn!= null) {
                try {
                    conn.rollback(); // Wycofuje zmiany
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } finally {
            try { if (rentCarStmt != null) rentCarStmt.close(); } catch (Exception ignored) {}
            try { if (updateUserStmt != null) updateUserStmt.close(); } catch (Exception ignored) {}
            try { if (conn != null) conn.close(); } catch (Exception ignored) {}
        }
        return result;
    }

    @Override
    public boolean returnVehicle(int ID, String login) {

        Connection conn = null;
        PreparedStatement rentCarStmt = null;
        PreparedStatement updateUserStmt = null;
        boolean result = false;

        try {
            conn = manager.getConnection();
            conn.setAutoCommit(false); // reczny commit

            rentCarStmt = conn.prepareStatement(RETURN_CAR_SQL);
            rentCarStmt.setInt(1, ID);
            int changed1 =rentCarStmt.executeUpdate();

            updateUserStmt = conn.prepareStatement(RETURN_UPDATE_USER_SQL);
            updateUserStmt.setString(1, login);
            int changed2 =updateUserStmt.executeUpdate();

            result = changed1 > 0 && changed2 > 0;
            if (result) {
                System.out.println("wypozyczono");
                conn.commit();
            } else {
                System.out.println("Nie wypożyczono");
                conn.rollback(); // wycofuje zmiany
            }

        } catch(Exception e) {
            e.printStackTrace();
            if (conn!= null) {
                try {
                    conn.rollback(); // Wycofuje zmiany
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } finally {
            try { if (rentCarStmt != null) rentCarStmt.close(); } catch (Exception ignored) {}
            try { if (updateUserStmt != null) updateUserStmt.close(); } catch (Exception ignored) {}
            try { if (conn != null) conn.close(); } catch (Exception ignored) {}
        }
        return result;
    }

    @Override
    public boolean addVehicle(Vehicle vehicle) {
        try (Connection conn = manager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_VEHICLE_SQL)) {

            stmt.setString(1, vehicle.getBrand());
            stmt.setString(2, vehicle.getModel());
            stmt.setInt(3, vehicle.getYear());
            stmt.setDouble(4, vehicle.getPrice());
            stmt.setInt(5, vehicle.getId());

            if(vehicle instanceof Motorcycle){
               stmt.setString (6,((Motorcycle) vehicle).getCategory());
            } else
                stmt.setNull(6, Types.VARCHAR);

            int changed = stmt.executeUpdate();

            if (changed  > 0) {
                System.out.println("Pojazd został pomyślnie dodany.");
                return true;
            } else {
                System.out.println("Nie udało się dodać pojazdu.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean removeVehicle(int ID) {
        Connection conn = null;
        PreparedStatement removeVehStatement = null;
        PreparedStatement clearRentedFlagStatement = null;
        boolean result = false;

        Vehicle veh = getVehicle(ID);
        if (veh == null)
            return false;

        try {
            conn = DatabaseManager.getInstance().getConnection();
            conn.setAutoCommit(false);
            removeVehStatement = conn.prepareStatement(REMOVE_VEHICLE_SQL);
            removeVehStatement.setInt(1, ID);

            if(veh.isRented()) {
                clearRentedFlagStatement = conn.prepareStatement("UPDATE tusers SET rentedID = NULL WHERE rentedID = ?");
                clearRentedFlagStatement.setInt(1,ID);
            }

            int flags_cleared = (clearRentedFlagStatement != null)? clearRentedFlagStatement.executeUpdate():0;
            int changed = removeVehStatement.executeUpdate();
            result = changed > 0 && (flags_cleared > 0 || !veh.isRented());

            if(result)
                conn.commit();
            else
                conn.rollback();

        } catch(SQLException e){
            if (conn != null)
                conn.rollback();
            throw new RuntimeException();
        } finally {
            try{
                if(conn != null)
                    conn.close();
                if(removeVehStatement != null)
                    removeVehStatement.close();
            }catch(SQLException e){
                throw new RuntimeException();
        }

        return result;
    }
    }

    @Override
    public Vehicle getVehicle(int ID) {
        Vehicle result = null;
        Connection connection = null;
        ResultSet rs = null;
        try {
            connection = manager.getConnection();
            Statement statement = connection.createStatement();
            rs = statement.executeQuery(GET_VEHICLE_SQL);
            if(rs.next()){
                result = null;
                String category = rs.getString("category");
                int id = rs.getInt("id");
                String brand = rs.getString("brand");
                String model = rs.getString("model");
                int year = rs.getInt("year");
                int price = rs.getInt("price");
                boolean rent = rs.getBoolean("rent");
                if ( category!=null){
                    //example.Motorcycle(String brand, String model, int year, int price, int id, String category)
                    result = new Motorcycle(brand,model,year,price, rent, id, category);

                }else{
                    result = new Car(brand,model,year,price,rent, id);
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        finally {
            if( rs!=null){try {rs.close();} catch (SQLException e) {e.printStackTrace();}}
            if( connection!=null){try {connection.close();} catch (SQLException e) {e.printStackTrace();}}
        }
        return result;

    }

    @Override
    public Collection<Vehicle> getVehicles() {

            Collection<Vehicle> vehicles = new ArrayList<>();
            Connection connection = null;
            ResultSet rs = null;
        try {
                connection = manager.getConnection();
                Statement statement = connection.createStatement();
                rs = statement.executeQuery(GET_ALL_VEHICLE_SQL);
                while(rs.next()){
                    Vehicle v = null;
                    String category = rs.getString("category");
                    int id = rs.getInt("id");
                    String brand = rs.getString("brand");
                    String model = rs.getString("model");
                    int year = rs.getInt("year");
                    int price = rs.getInt("price");
                    boolean rent = rs.getBoolean("rent");
                    if ( category!=null){
                        //example.Motorcycle(String brand, String model, int year, int price, int id, String category)
                        v = new Motorcycle(brand,model,year,price, rent, id, category);

                    }else{
                        v = new Car(brand,model,year,price,rent, id);
                    }
                    vehicles.add(v);
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
            finally {
                if( rs!=null){try {rs.close();} catch (SQLException e) {e.printStackTrace();}}
                if( connection!=null){try {connection.close();} catch (SQLException e) {e.printStackTrace();}}
            }
            return vehicles;

        }

}
