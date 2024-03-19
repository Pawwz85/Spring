public class CSVVehicleDecoder {
    public static Vehicle decode(String recordFromCSV){
        String[] fields = recordFromCSV.split(";");

        String vehicleType = fields[0];

        switch (vehicleType){
            case "car" -> {
                return new Car(fields);
            }

            case "motorcycle" -> {
                return new Motorcycle(fields);
            }
            default -> {
              return new GenericVehicle(fields);
            }
        }

    }
}
