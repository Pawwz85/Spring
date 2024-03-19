import java.util.HashSet;
import java.util.Random;
import java.util.Set;


public class IDManager {
    private static final Set<Integer> registeredIDs = new HashSet<>();
    private static final Random RNG = new Random();
    public static Integer assignNewID(){
        int ID = RNG.nextInt();
        while (registeredIDs.contains(ID))
            ID = RNG.nextInt();
        registeredIDs.add(ID);
        return ID;
    }
    public static void registerID(int ID){
        registeredIDs.add(ID);
    }
}
