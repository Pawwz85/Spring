public class GenericVehicle extends Vehicle{

    GenericVehicle(String vehicleType, String brand, String model, int year, int price, boolean rented){
        super(brand, model, year, price, rented);
        super.vehicleType = vehicleType;
    }

    GenericVehicle(String[] csvLine){
        super(csvLine);
        super.vehicleType = csvLine[0];
    }
    @Override
    public String toString() {
        return String.format("ID: %d, %s %s from year %d. Price: %d.%d %s", getId(), getBrand(), getModel(), getYear(),
                getPrice()/100, getPrice()%100, vehicleType);
    }
}
