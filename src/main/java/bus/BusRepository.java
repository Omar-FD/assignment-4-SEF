package bus;
import java.util.ArrayList;

import driver.Bus;

public class BusRepository {

    private final ArrayList<Bus> buses = new ArrayList<>();


    public boolean add(Bus bus) {

        if (retrieve(bus.getBusID()) != null) 
            return false;

        if (!bus.isValidBusID(bus.getBusID())){
            return false;
        }
        

        buses.add(bus);
        return true;
    }


    public Bus retrieve(String busID) {
        for ( int i=0; i< buses.size(); ++i){

            if (buses.get(i).getBusID().equals(busID)) {
                return buses.get(i);
            }
        }
        return null;
    }

    public boolean update(Bus updated) {
        for (int i = 0; i < buses.size(); i++) {
            if (buses.get(i).getBusID().equals(updated.getBusID()) ) {
                
                if (updated.getCapacity() > buses.get(i).getCapacity()){
                    return false;
                }
                
                buses.set(i, updated);
                return true;
            }
        }

        return false;
    }

 
    public int count() {
        return buses.size();
    }
}