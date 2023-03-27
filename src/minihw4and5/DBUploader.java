/*
 * Rationale:
 *
 * While the use of java classes like Scanner/File or BufferedReader/Filereader
 * seemed to be the most straightforward approach, the time to process all the
 * instructions, even if in batch, was orders of magnitude inferior to methods
 * that rely on the database to interact with the file directly.
 *
 * Here I used the sql command LOAD DATA LOCAL INFILE to retrieve and upload the
 * csv entry at the speed allowed by the server, effectively removing the
 * bottleneck caused by the java runtime.
 * (the code should run for approximately t00:15 for 300k entries, instead of
 * t01:20 with other methods, for a 500% performance increase)
 *
 * Local data parsing is disabled by default and it must be activated either
 * from the server configuration file, or trough the command
 * 'allowLoadLocalInfile' provided by the driver.
 * (see https://dev.mysql.com/doc/connector-j/8.0/en/connector-j-connp-props-security.html#cj-conn-prop_allowLoadLocalInfile )
 *
 * The CSV file contains duplicates (e.g. Invoice=489517, StockCode=21912),
 * hence I refrained from implementing a duplicate check on upload.
 */
package minihw4and5;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

/**
 *
 * @author <@student.cct.ie>
 */
public class DBUploader {
    private final String DB_URL = "jdbc:mysql://localhost?allowLoadLocalInfile=true"; // The url query string referencing the driver setting for local data upload
    private final String USER = "pooa";
    private final String PASSWD = "pooa";

    public void query(String sql) throws SQLException{
        Connection conn = DriverManager.getConnection(DB_URL , USER , PASSWD);
        Statement stmt = conn.createStatement();
        stmt.addBatch(sql);
        stmt.executeBatch();
        conn.close();
    }
    
    public static String[] parseHeader(String path) throws FileNotFoundException{
        Scanner sc = new Scanner(new File(path)); // read file with scanner
        if (sc.hasNextLine()) {
                String header = sc.nextLine();
                String[] attributes = header.split(",");
                return attributes;
        }
        throw new IllegalArgumentException("File is empty");
    }
    
    public void loadCSV(String csvPath) throws SQLException {

//        Class.forName("com.mysql.cj.jdbc.Driver").newInstance(); // Deprecated, see https://docs.oracle.com/javase/8/docs/api/java/sql/DriverManager.html
        Connection conn = DriverManager.getConnection(DB_URL , USER , PASSWD);

        String sql = "LOAD DATA LOCAL INFILE '" + csvPath + "' "
                     + "INTO TABLE newdata_csv "
                     + "FIELDS TERMINATED BY ',' "  // field separator
                     + "LINES TERMINATED BY '\\n' " // line separator
                     + "IGNORE 1 LINES ("
                     + "Invoice,"
                     + "StockCode,"
                     + "Description,"
                     + "Quantity,"
                     + "@InvoiceDate,"
                     + "Price,"
                     + "Customer_ID,"
                     + "Country"
                     + ") SET InvoiceDate = STR_TO_DATE(@InvoiceDate, '%d/%m/%Y %H:%i')";

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
                      + "Customer_ID int,"
                      + "Country varchar(20)"
                      + ");");
        stmt.addBatch(sql);
        stmt.executeBatch();
        conn.close();
    }

}
