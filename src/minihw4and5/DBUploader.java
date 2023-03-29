/* extra commit 2 */
/* extra commit 1 */

package minihw4and5;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Bak <sba22064@student.cct.ie>
 */
public class DBUploader {
    private final String DB_URL = "jdbc:mysql://localhost";
    private final String PASSWD = "pooa";
    private final String USER = "pooa";

    public void uploadCSV(String csvPath) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
        try {
            Connection conn = DriverManager.getConnection(DB_URL , USER , PASSWD);
            Statement stmt = conn.createStatement();
            stmt.addBatch("CREATE DATABASE IF NOT EXISTS minihw4and5;");
            stmt.addBatch("USE minihw4and5;");
            stmt.addBatch("CREATE TABLE IF NOT EXISTS newdata_csv ("
                    + "Invoice int,"
                    + "StockCode int,"
                    + "Description varchar(50),"
                    + "Quantity int,"
                    + "InvoiceDate datetime,"
                    + "Price decimal(5,2),"
                    + "Customer_ID int,Country varchar(20)"
                    + ");");
            stmt.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}// end of class