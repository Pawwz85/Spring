package example;

import example.Vehicle;

public class Motorcycle extends Vehicle {

    private String category;

    public Motorcycle(String brand, String model, int year, int price, boolean rented, int id, String category){
        super(brand, model, year, price, rented, id);
        this.category = category;
        super.vehicleType = "motorcycle";
    }

    Motorcycle(String[] csvLine){
        super(csvLine);
        this.category = csvLine[7];
    }
    @Override
    public String toString() {
        String status = (isRented())?"Rented" : "Available";
        return String.format("ID: %d, %s %s from year %d. Price: %d.%d %s. Requires %s license", getId(), getBrand(), getModel(), getYear(),
                getPrice()/100, getPrice()%100, status, category);
    }

    @Override
    public String toCSV(){
        String result = super.toCSV();
        result += ";" + category;
        return result;
    }

    public String getCategory() {
        return category;
    }
}
