public class UserAPI {
    private static IVehicleRepository vehicleRepository;
    private static IUserRepository userRepository;

    public static void setUserRepository(IUserRepository userRepository) {
        UserAPI.userRepository = userRepository;
    }

    public static void setVehicleRepository(IVehicleRepository vehicleRepository) {
        UserAPI.vehicleRepository = vehicleRepository;
    }

    public static boolean login(String login, String password){
        return new Authentication(userRepository).auth(login, password) != null;
    }

    public static boolean rentVehicle(int vehicleID, User user){
        if(user.getRentedVehicleID() == null)
            return false;
        boolean success = vehicleRepository.rentVehicle(vehicleID);
        if(success)
            user.setRentedVehicleID(vehicleID);
        return success;
    }

    public static boolean returnVehicle(int vehicleID,User user){
        if(user.getRentedVehicleID() == null || !user.getRentedVehicleID().equals(vehicleID))
            return false;
        user.setRentedVehicleID(null);
        vehicleRepository.returnVehicle(vehicleID);
        return true;
    }
}
