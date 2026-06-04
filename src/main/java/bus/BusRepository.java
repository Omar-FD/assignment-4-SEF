package bus;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class BusRepository {
    
    private final ArrayList<Bus> buses = new ArrayList<>();

    public BusRepository(){
        loadFromFile();
    }

    
    private void saveToFile() {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter("data/busData.txt"))) {
        for (Bus bus : buses) {
            writer.write(bus.getBusID() + "," + bus.getCapacity() + "," + 
                        bus.getFuelLevel() + "," + bus.getFuelType());
            writer.newLine();
        }
    } catch (IOException e) {
        System.out.println("Error saving: " + e.getMessage());
    }
}

    private void loadFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader("data/busData.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                buses.add(new Bus(parts[0], Integer.parseInt(parts[1]), 
                                Double.parseDouble(parts[2]), parts[3]));
            }
        } catch (IOException e) {
        
        }
    }

    public boolean add(Bus bus) {

        if (retrieve(bus.getBusID()) != null) 
            return false;

        if (!bus.isValidBusID(bus.getBusID())){
            return false;
        }
        
        buses.add(bus);
        saveToFile();
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
                saveToFile();
                return true;
            }
        }

        return false;
    }

 
    public int count() {
        return buses.size();
    }
}