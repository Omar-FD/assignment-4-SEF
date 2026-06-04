
package bus;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BusIntegrationTest {

    //private BusRepository busRepository;

    
  
     //Add a valid bus and verify it can be retrieved from the TXT file.
    @BeforeEach
    void setUp() throws IOException {
        // Delete the file before each test so we start fresh
        Files.deleteIfExists(Paths.get("data/busData.txt"));
    }

    @Test
    @Order(1)
    void ValidBusesStoredCorrectly() {
        BusRepository repository = new BusRepository();
        
        Bus bus = new Bus("12345678", 40, 80.0, "Diesel");
        boolean result = repository.add(bus);

        assertTrue(result);
        assertEquals(1, repository.count());
    }






    //Add multiple valid buses and confirm all are persisted.
    @Test
    @Order(2)
    void InvalidBusesAreRejected() {

        BusRepository repository = new BusRepository();

        try {
            Bus bus = new Bus("INVALID", 30, 60.0, "Diesel");
            boolean result = repository.add(bus);
            assertFalse(result);
        } catch (IllegalArgumentException e) {
            
            assertEquals(0, repository.count());
        }
       

    }

    





     //Attempt to add a bus with a duplicate busID – must be rejected.
   
    @Test
    @Order(3)
    void UpdatesPersistedCorrectly() {

        BusRepository repository = new BusRepository();

        Bus bus = new Bus("55555555", 40, 60.0, "Diesel");
        repository.add(bus);

        Bus updated = new Bus("55555555", 40, 95.0, "Hybrid");
        boolean result = repository.update(updated);

        assertTrue(result);
        assertEquals(1, repository.count());
    }











    //Count starts at 0, increments with each valid add,
   
    @Test
    @Order(6)
    void CountsUpdatedCorrectly() {
        
        BusRepository repository = new BusRepository();
        repository.add(new Bus("10000001", 30, 60.0, "Diesel"));
        assertEquals(1, repository.count());

        repository.add(new Bus("10000002", 45, 80.0, "Hybrid"));
        assertEquals(2, repository.count());

        try {
            repository.add(new Bus("INVALID", 20, 50.0, "Diesel"));
        } catch (IllegalArgumentException e) {
            
        }
        assertEquals(2, repository.count());
    }

}