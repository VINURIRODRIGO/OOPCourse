import java.io.*;
import java.util.*;

public interface ChampionshipManager {
    int [] noPoint = {25, 18, 15, 12, 10, 8, 6, 4, 2, 1};
    LinkedHashMap<String, ArrayList<Integer>> pointHashMap = new LinkedHashMap<>();

    void runRace();
    void update(String manufacturer, int position);
    void storePoints();
    void addDriver();
    void deleteDriver();
    void driverStatistics();

   static void loadFile() {
        String line;
        String manuf;
        try {
            File myObj = new File("points.txt");
            BufferedReader myReader = new BufferedReader(new FileReader(myObj));
            while ((line = myReader.readLine()) != null) {
                line = line.replace("[","").replace("]","").replace(" ","");
                String[] data=line.split(",");
                manuf = data[0];
                ArrayList<Integer> driv = new ArrayList<>();
                for(int i=1;i<11; i++){
                    driv.add(Integer.valueOf(data[i]));
                }
                pointHashMap.put(manuf, driv);
            }
        }
        catch (IOException fileNotFoundException) {
            fileNotFoundException.printStackTrace(); }
    }
}
