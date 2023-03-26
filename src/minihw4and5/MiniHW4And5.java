package minihw4and5;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author <sba22064@student.cct.ie>
 */
public class MiniHW4And5 {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws ClassNotFoundException , InstantiationException , IllegalAccessException, SQLException, FileNotFoundException {
//        DBUploader dbup = new DBUploader();
//        dbup.uploadCSV("");

        /*
         * parseCSV v.1
         */
        try {
            parseCSV("src/minihw4and5/newData.csv");
        } catch (FileNotFoundException e) {
        };
        /*
         * parseCSV v.2
         */
//        try {
//            System.out.println(parseCSV("src/minihw4and5/newData.csv").get(0).get(0));
//        } catch (IOException e) {
//            System.out.println(e);
//        };

    }//main

    static void parseCSV(String path) throws FileNotFoundException {
        ArrayList<String[]> table = new ArrayList<>();
        Scanner sc = new Scanner(new File(path));
        sc.useDelimiter("\n");
        while (sc.hasNext()) {
            String[] attributes = sc.next().split(",");
//            System.out.println(String.join(",", attributes));
            table.add(attributes);
        }
        System.out.println(table.get(0)[0]);
    }

//    static ArrayList<List<String>> parseCSV(String path) throws IOException , FileNotFoundException {
//        ArrayList<List<String>> table = new ArrayList<>();
//        BufferedReader br = new BufferedReader(new FileReader(path));
//        String tuple;
//        while ((tuple = br.readLine()) != null) {
//            String[] attributes = tuple.split(",");
//            table.add(Arrays.asList(attributes));
//        }
//        return table;
//    }
}//class
