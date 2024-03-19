import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


// TODO: solve the ID problem
public class VehicleRepository implements IVehicleRepository{

    private final Map<Integer, Vehicle> vehicleMap = new HashMap<>();
    private String repositoryPath;
    @Override
    public void rentVehicle(int vehicleID) {
        vehicleMap.get(vehicleID).setRented(false);
        save(repositoryPath);
    }

    @Override
    public void returnVehicle(int vehicleID) {
        vehicleMap.get(vehicleID).setRented(true);
        save(repositoryPath);
    }

    @Override
    public List<Vehicle> getVehicles() {
        return vehicleMap.values().stream().toList();
    }

    @Override
    public void save(String path)  {
        File  f = new File(path);

        try{

            if(!f.exists())
                f.createNewFile();

            FileWriter fileWriter = new FileWriter(f);

            for (var v : vehicleMap.values()){
                fileWriter.write(v.toCSV() +"\n");
            }

            fileWriter.close();
        } catch (IOException e){
            throw new RuntimeException();
        }


    }

    public void loadRepositoryFromFile(){
        File file = new File(repositoryPath);

        if(!file.exists())
            return;


        Scanner scanner;
        try {
            scanner = new Scanner(file);
            while (scanner.hasNext()){
                Vehicle vehicle = CSVVehicleDecoder.decode (scanner.nextLine());
                addToRepository(vehicle);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void addToRepository(Vehicle vehicle) {
        vehicleMap.put(vehicle.getId(), vehicle);
        save(repositoryPath);
    }

    @Override
    public void removeFromRepository(Vehicle vehicle) {
        vehicleMap.remove(vehicle.getId());
        save(repositoryPath);
    }

    VehicleRepository(String repositoryPath){
        this.repositoryPath = repositoryPath;
        loadRepositoryFromFile();
    }
}
