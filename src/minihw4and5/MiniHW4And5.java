package minihw4and5;

import java.sql.SQLException;
/**
 *
 * @author <sba22064@student.cct.ie>
 */
public class MiniHW4And5 {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SQLException {
        DBUploader dbup = new DBUploader();
        dbup.loadCSV("src/minihw4and5/newData.csv");

    }//main


}//class//class
