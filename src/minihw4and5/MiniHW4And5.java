package minihw4and5;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author <sba22064@student.cct.ie>
 */
public class MiniHW4And5 {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws ClassNotFoundException , InstantiationException , IllegalAccessException {
//        DBUploader dbup = new DBUploader();
//        dbup.uploadCSV("");
        try {
            System.out.println(parseCSV("src/minihw4and5/newData.csv").get(0).get(0));
        } catch (IOException e) {
            System.out.println(e);
        };
    }//main

    static ArrayList<List<String>> parseCSV(String path) throws IOException , FileNotFoundException {
        ArrayList<List<String>> table = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(path));
        String tuple;
        while ((tuple = br.readLine()) != null) {
            String[] attributes = tuple.split(",");
            table.add(Arrays.asList(attributes));
        }
        return table;
    }

}//class
