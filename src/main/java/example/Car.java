package example;

import example.Vehicle;

public class Car extends Vehicle {
    public Car(String brand, String model, int year, int price, boolean rented, Integer id){
        super(brand, model, year, price, rented, id);
        super.vehicleType = "car";
    }
    public Car(String[] csvLine){
        super(csvLine);
        super.vehicleType = "car";
    }
    @Override
    public String toString() {
        String status = (isRented())?"Rented" : "Available";
        return String.format("ID: %d, %s %s from year %d. Price: %d.%d %s.", getId(), getBrand(), getModel(), getYear(),
                getPrice()/100, getPrice()%100, status);
    }

}
