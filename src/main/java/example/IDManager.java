package example;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;


public class IDManager {
    private  final Set<Integer> registeredIDs = new HashSet<>();
    private  final Random RNG = new Random();

    private static IDManager instance = null;

    IDManager(){
        registeredIDs.add(0); // SQL query will
    }
    public static IDManager getInstance(){
        if(instance == null)
            instance = new IDManager();
        return instance;
    }
    public  Integer assignNewID(){
        int ID = RNG.nextInt();
        while (registeredIDs.contains(ID))
            ID = RNG.nextInt();
        registeredIDs.add(ID);
        return ID;
    }
    public void registerID(int ID){
        registeredIDs.add(ID);
    }
}
