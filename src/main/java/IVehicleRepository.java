import java.util.List;

public interface IVehicleRepository {
    boolean rentVehicle(int vehicleID);
    void returnVehicle(int vehicleID);
    Vehicle getVehicle(int vehicleID);
    List<Vehicle> getVehicles();
    void save(String path);
    void addToRepository(Vehicle vehicle);
    void removeFromRepository(Vehicle vehicle);
}
