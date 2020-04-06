# TollParkingLibrary
A Toll Parking Library 

This JAVA project provides a base API of a toll parking library.

## Purpose

The application is used to manage a toll parking. It has functionalities to insert or remove a car into/from parking slots. There are functionalities to bill the customer while getting out.

### Prerequisites

Java 8+ & Maven
IntelliJ editor is recommended

### Installing

Open the project and build the code with

```
mvn clean install
```

A jar file will be generated in target/TollParkingLibrary-1.0-SNAPSHOT.jar

Include the jar in your project by following this [guide](https://www.jetbrains.com/help/idea/library.html#include-transitive-dependencies) (for IntelliJ) 

An example:
![Including the jar file into IntelliJ](https://ibb.co/pPQxKy8)

### Code example

After integrating the jar library into your project, you are ready to use it.
A small exaple code of the library is showed:

```JAVA
import com.danybr.tollParkingLibrary.model.car.ElectricCar;
...

public class TestTollParking {
    public static void main(String[] args) throws
            WrongInputException, PowerException, InterruptedException, WrongDateException {
        
        ArrayList<Slot> slots = new ArrayList<Slot>();
        // Creating an Electric slot with 20 kW adapter  
        slots.add(new ElectricSlot("E-20"));

        // Base pricing policy, with cost of 2 euro per hour
        TollParking parking = new TollParking(slots, new PricingPolicyHour(2));
        ElectricCar cElectric20 = new ElectricCar(20);

        // Add the car in the parking, it is stored and the timestamp is saved.
        parking.insert(cElectric20);
        // Wait 65 seconds
        TimeUnit.SECONDS.sleep(65);
        // Removing the car from the parking, calculating the time difference and billing the customer.
        // Once inside, the customer pays at least one hour 
        parking.remove(cElectric20);
    }

}
```

### Documentation
All API documentation is available as JavaDoc. It is contained in the *TollParkingLibraryJavaDoc* folder.
To view the documentation simply download the repository and open the index.html file in folder *TollParkingLibraryJavaDoc* with any browser.
