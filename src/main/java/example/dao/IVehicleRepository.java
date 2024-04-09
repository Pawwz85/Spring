package example.dao;

import example.Car;
import example.Vehicle;

import java.util.Collection;

public interface IVehicleRepository {

    boolean rentVehicle(int ID,String login);
    boolean returnVehicle(int ID,String login );

    boolean addVehicle(Vehicle vehicle);

    boolean removeVehicle(int ID);

    Vehicle getVehicle(int id);

    Collection<Vehicle> getVehicles();

}
