import java.io.FileWriter;
import java.io.IOException;
import java.util.*;


public class Formula1ChampionshipManager implements ChampionshipManager{
    private static final Formula1Driver fd = new Formula1Driver();
    private static final Formula1ChampionshipManager fdm = new Formula1ChampionshipManager();

    static Scanner stringInput = new Scanner(System.in);
    static Scanner input = new Scanner(System.in);
    public static void main(String[] args) {
        String choice;
        do {
            fd.loadFile();
            ChampionshipManager.loadFile();
            System.out.println("----------Menu---------");
            System.out.println("Enter A: To add a new Customer");
            System.out.println("Enter B: To delete a driver and the team");
            System.out.println("Enter C: To change the driver");
            System.out.println("Enter D: To find a driver's driving statistics");
            System.out.println("Enter E: To Start the race");
            System.out.println("Enter F: To display all the drivers competing");
            System.out.println("Enter Q: To quit");
            System.out.print("Enter your choice: ");
            choice = stringInput.nextLine().toUpperCase();
            switch (choice){
                case "A" -> fdm.addDriver();
                case "B" -> fdm.deleteDriver();
                case "C" -> fd.changeDriver();
                case "D" -> fdm.driverStatistics();
                case "E" -> fdm.runRace();
                case "F" -> fdm.comparison();
                case "Q" -> System.out.println("Thank you");
                default -> System.out.println("Invalid choice");
            }
        }while (!choice.equals("Q"));
    }

    @Override
    public void runRace() {
            fd.loadFile();
            ArrayList<String> cars = new ArrayList<>(fd.hashMap.keySet());
            Collections.shuffle(cars, new Random());
            for (int i=0; i< cars.size(); i++){
                Formula1Driver oldData = fd.hashMap.get(cars.get(i));
                String driver = oldData.getDriverName();
                String location = oldData.getLocation();
                try {
                    update(cars.get(i), i);
                    int points = oldData.getPoints()+noPoint[i];
                    fd.hashMap.replace(cars.get(i), oldData, new Formula1Driver(driver,location,points,java.time.LocalDate.now()));
                } catch (ArrayIndexOutOfBoundsException e){
                    fd.hashMap.replace(cars.get(i), oldData, new Formula1Driver(driver,location,oldData.getPoints(),oldData.getLocalDate()));
                }
                storePoints();
            }
            fd.storeData(fd.hashMap);
    }

    @Override
    public void update(String manufacturer, int position) {
        try {
            ChampionshipManager.loadFile();
            ArrayList<Integer> totalPosition = new ArrayList<>();
            ArrayList <Integer> x =pointHashMap.get(manufacturer);
            for (int i=0; i< x.size(); i++){
                if (i==position){
                    totalPosition.add(x.get(i)+1);
                }
                else{
                    totalPosition.add(x.get(i));
                }
            }pointHashMap.put(manufacturer,totalPosition);
        }
        catch (NullPointerException e){
            ArrayList<Integer> totalPosition = new ArrayList<>();
            for (int i=0; i< 10; i++){
                if (i==position){
                    totalPosition.add(1);
                }
                else {
                    totalPosition.add(0);
                }
            }pointHashMap.put(manufacturer,totalPosition);
        }
    }

    @Override
    public void storePoints() {
        Set<Map.Entry<String, ArrayList<Integer>>> set = pointHashMap.entrySet();
        try {
            FileWriter myWriter = new FileWriter("points.txt");
            for (Map.Entry<String, ArrayList<Integer>> entry: set){
                myWriter.write(entry.getKey()+","+ entry.getValue() +"\n");
            } myWriter.close();

        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace(); }
    }

    @Override
    public void addDriver() {
        System.out.print("Enter how may drivers you want to add: ");
        int num = input.nextInt();

        for (int i =0; i<num; i++){
            System.out.print("Enter the name of the driver: ");
            String driverName = input.next();
            driverName= driverName.substring(0,1).toUpperCase()+driverName.substring(1).toLowerCase();

            System.out.print("Enter the name of the car manufacturer: ");
            String manufacturerName = input.next();
            manufacturerName= manufacturerName.substring(0,1).toUpperCase()+manufacturerName.substring(1).toLowerCase();

            System.out.print("Enter the location: ");
            String location = input.next();
            location= location.substring(0,1).toUpperCase()+location.substring(1).toLowerCase();

            fd.setCarManufacturer(manufacturerName);
            Formula1Driver newDriver = new Formula1Driver(driverName, location, 0, java.time.LocalDate.now());
            if(!fd.hashMap.containsKey(manufacturerName)){
                fd.hashMap.put(fd.getCarManufacturer(), newDriver);
                fd.storeData(fd.hashMap);
                ArrayList<Integer> totalPosition = new ArrayList<>();
                for (int u=0; u< 10; u++){
                    totalPosition.add(0);
                }
                pointHashMap.put(manufacturerName,totalPosition);
                storePoints();
            }
            else{
                System.out.printf("%s team is already registered for the competition.", manufacturerName).println();
            }
        }
    }

    @Override
    public void deleteDriver() {
        System.out.print("Enter the name of the car manufacturer: ");
        String manufacturerName = input.next();
        manufacturerName= manufacturerName.substring(0,1).toUpperCase()+manufacturerName.substring(1).toLowerCase();

        if(fd.hashMap.containsKey(manufacturerName)) {
            fd.hashMap.remove(manufacturerName);
            fd.storeData(fd.hashMap);
        }
        else{
            System.out.printf("%s team is not registered for the competition.", manufacturerName).println();
        }
    }

    @Override
    public void driverStatistics() {
        int count = 0;
        int found = 0;
        System.out.print("Enter the name of the driver: ");
        String driverName = input.next();
        driverName= driverName.substring(0,1).toUpperCase()+driverName.substring(1).toLowerCase();
        ChampionshipManager.loadFile();
        for (Formula1Driver x : fd.hashMap.values()) {
            if (Objects.equals(x.getDriverName(), driverName)) {
                String manufacturer = String.valueOf(fd.hashMap.keySet().toArray()[count]);
                System.out.println("Car manufacturer = "+manufacturer);
                System.out.println("Driver = " + x.getDriverName());
                System.out.println("Location = " + x.getLocation());
                System.out.println("Total points = " + x.getPoints());
                ArrayList<Integer> points = ChampionshipManager.pointHashMap.get(manufacturer);
                for (int i =0; i<10; i++){
                    System.out.printf("%d positions = %d",i+1, points.get(i)).println();
                }
                found++;
            }
            else if (count == fd.hashMap.size()-1 && found ==0){
                System.out.printf("%s is not an existing driver",driverName).println();
            }
            count++;
        }
    }

//    public void compare() {
//        final Set<Formula1Driver> setToReturn = new HashSet<>();
//        List<Formula1Driver> list = new ArrayList<>(fd.hashMap.values());
//        ChampionshipManager.loadFile();
//        List<ArrayList<Integer>> list2 = new ArrayList<>(pointHashMap.values());
//        list.sort((o1, o2) -> {
//            int index1 = list.indexOf(o1);
//            int index2 = list.indexOf(o2);
//            System.out.println("Index of : "+index1+" "+index2);
//            if (o1.getPoints() > o2.getPoints()){
//                return 1;
//            }
//            else if (o1.getPoints() == o2.getPoints()) {
//                System.out.println(o1.getCarManufacturer()+" "+o2.getDriverName());
//                ArrayList<Integer> array1 = list2.get(index1);
//                ArrayList<Integer> array2 = list2.get(index2);
//                System.out.println(array1+" "+array2);
//                for (int i = 0; i < array1.size(); i++) {
//                    if (!array1.get(i).equals(array2.get(i)) && array1.get(i)>array2.get(i)){
//                        System.out.println(array1.get(i)+ " "+array2.get(i)+" "+(array1.get(i)>array2.get(i)));
//                        return -1;
//                    }
//                }
//
////                LocalDate date1 = o1.getLocalDate();
////                LocalDate date2 = o2.getLocalDate();
////                if (date1.isAfter(date2))
////                    return 0;
//            }
//            if (o1.getPoints() < o2.getPoints()){
//                return 0;
//            }
//            return 0;
//        });
//        System.out.println(list);
//    }

    /***
     *
     * https://www.youtube.com/watch?v=buD-_4-PXWw
     */

    public void comparison() {
        List<ArrayList<Integer>> list2 = new ArrayList<>(pointHashMap.values());
        Set<Map.Entry<String, Formula1Driver>> entries = fd.hashMap.entrySet();
        List<Map.Entry<String, Formula1Driver>> listData = new ArrayList<>(entries);
        listData.sort((o1, o2) -> {
            if (o1.getValue().getPoints() > o2.getValue().getPoints()) {
                return 1;
            } else if (o1.getValue().getPoints() == o2.getValue().getPoints()) {
                ArrayList<Integer> o1Array = list2.get(listData.indexOf(o1));
                ArrayList<Integer> o2Array = list2.get(listData.indexOf(o2));
                for (int i = 0; i < 10; i++) {
                    if (!o1Array.get(i).equals(o2Array.get(i))) {
                        if (o1Array.get(i) > (o2Array.get(i))) {
                            return 1;
                        } else {
                            return -1;
                        }
                    }
                }
                return 0;
            }
            return -1;
        });
        String leftAlignFormat = "| %-20s | %-8s | %-20s | %-12d | %-20s |%n";
        System.out.format("+----------------------+----------+----------------------+--------------+----------------------------------------------------------------------+%n");
        System.out.format("| Car manufacturer     | Driver   | Location             | Total Points |_______________________Total Positions _______________________________|%n");
        System.out.format("|                      |          |                      |              | 1st   | 2nd  | 3rd  | 4th  | 5th  | 6th  | 7th  | 8th  | 9th  | 10th |%n");
        System.out.format("+----------------------+----------+----------------------+--------------+-------+------+------+------+------+------+------+------+------+------+%n");
        for (int r = listData.size()-1; r>=0; r--) {
            Map.Entry<String, Formula1Driver> listDatum = listData.get(r);
            StringBuilder str = new StringBuilder();
            ArrayList<Integer> x =ChampionshipManager.pointHashMap.get(listDatum.getKey());
            for (int i = 0; i < x.size(); i++)
            {   int myNumbersInt = x.get(i);
                if (i!=x.size()-1){
                    str.append( String.format(" %s %-2s |",myNumbersInt," "));
                }else{
                    str.append( String.format(" %s %-2s",myNumbersInt," "));
                }
            }
            System.out.format(leftAlignFormat, listDatum.getKey(), listDatum.getValue().getDriverName(), listDatum.getValue().getLocation(), listDatum.getValue().getPoints(), str);
        }
        System.out.format("+----------------------+----------+----------------------+--------------+-------+------+------+------+------+------+------+------+------+------+%n");


    }

}




