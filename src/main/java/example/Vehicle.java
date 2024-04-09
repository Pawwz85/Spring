package example;

public abstract class Vehicle {

    protected String vehicleType = "vehicle";
    private String brand;
    private String model;
    private int year;
    private int price;
    private boolean rented;
    private int id;

    private void init(String brand, String model, int year, int price, boolean rented, Integer id){
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.price = price;
        this.rented = rented;

        if(id != null)
            IDManager.getInstance().registerID(id);
        this.id = (id == null)? IDManager.getInstance().assignNewID() : id;
    }

    public Vehicle(String brand, String model, int year, int price, boolean rented, Integer id){
       init(brand, model, year, price, rented, id);
    }

    public Vehicle(String[] csvLine){
        init(csvLine[1], csvLine[2], Integer.parseInt(csvLine[3]), Integer.parseInt(csvLine[4]),
                Boolean.parseBoolean(csvLine[5]), Integer.parseInt(csvLine[6]));
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public int getId() {
        return id;
    }

    public boolean isRented() {
        return rented;
    }

    public int getPrice() {
        return price;
    }

    public int getYear() {
        return year;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setRented(boolean rented) {
        this.rented = rented;
    }

    @Override
    public abstract String toString();

    public String toCSV() {
        return String.format("%s;%s;%s;%d;%d;%b;%d",vehicleType , brand, model, year, price, rented, id);
    }
}
