import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class Formula1Driver extends Driver{
    private static final Scanner input = new Scanner(System.in);
    LinkedHashMap <String, Formula1Driver> hashMap = new LinkedHashMap<>();
    private int points;
    private LocalDate localDate;

    public Formula1Driver(){}
    public Formula1Driver(String driverName, String location, int points, LocalDate today){
        setDriverName(driverName);
        setLocation(location);
        this.points = points;
        this.localDate = today;

    }

    public int getPoints() {
        return points;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    @Override
    public String toString() {
        return String.format("%s,%s,%s",super.toString(),getPoints(), getLocalDate());
    }


    /***
     * Siva Reddy
     * https://www.youtube.com/watch?v=HlpWrH3CcoM
     * ***/
    public void storeData(LinkedHashMap<String,Formula1Driver> details){
        Set<Map.Entry<String, Formula1Driver>> set = details.entrySet();
        try {
            FileWriter myWriter = new FileWriter("output.txt");
            for (Map.Entry<String, Formula1Driver> entry: set){
                myWriter.write(entry.getKey()+","+entry.getValue()+"\n");
            } myWriter.close();
            System.out.println("Successfully Updated");

        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace(); }
    }
    public void loadFile() {

        String line;
        String driv;
        String manuf;
        String locat;
        int point;
        LocalDate date;
        try {
            File myObj = new File("output.txt");
            BufferedReader myReader = new BufferedReader(new FileReader(myObj));
            while ((line = myReader.readLine()) != null) {
                String[] data=line.split(",");
                driv = data[1];
                manuf = data[0];
                locat = data[2];
                point = Integer.parseInt(data[3]);
                date = LocalDate.parse(data[4]);
                hashMap.put(manuf, new Formula1Driver(driv, locat, point, date));

            }
        }
        catch (IOException fileNotFoundException) {
            fileNotFoundException.printStackTrace(); }
    }

    @Override
    public void changeDriver() {
        System.out.print("Enter the name of the car manufacturer: ");
        String manufacturerName = input.next();
        manufacturerName= manufacturerName.substring(0,1).toUpperCase()+manufacturerName.substring(1).toLowerCase();

        System.out.print("Enter the name of the new driver: ");
        String driverName = input.next();
        driverName= driverName.substring(0,1).toUpperCase()+driverName.substring(1).toLowerCase();

        System.out.print("Enter the location: ");
        String location = input.next();
        location= location.substring(0,1).toUpperCase()+location.substring(1).toLowerCase();

        if(hashMap.containsKey(manufacturerName)){
            Formula1Driver old = hashMap.get(manufacturerName);
            hashMap.replace(manufacturerName, old, new Formula1Driver(driverName,location,old.getPoints(),old.getLocalDate()));
            storeData(hashMap);
        }
        else{
            System.out.printf("%s team is not registered for the competition.", manufacturerName).println();
        }

    }
}
