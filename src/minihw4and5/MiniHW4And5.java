/*
 * Upload data to a MySQL database from a CSV file:
 *
 * While the use of java classes like Scanner/File or BufferedReader/Filereader
 * seemed to be the most straightforward approach, the time to process all the
 * entries, even if in batch, was orders of magnitude longer than what is
 * possible by letting the database to interact with the file directly.
 *
 * Here I used Scanner to read the queries from a .sql file, then passing the
 * instructions to the database. I used the sql command LOAD DATA LOCAL INFILE
 * to retrieve and upload the csv entry at the speed allowed by the server,
 * reducing the overhead caused by the java runtime reading entries and sending
 * queries in a loop. (the code should run approximately five times faster, for
 * approximately t00:15 for 300k+ entries)
 *
 * However, due to security concerns, local data parsing is disabled by default
 * and it must be activated either from the server configuration file, or trough
 * the command 'allowLoadLocalInfile' provided by the driver.
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
 * @author <sba22064@student.cct.ie>
 */
public class MiniHW4And5 {
    /*
     * QUERY PARSER
     * Parses the sql file and sends the command to the database
     */
    static void runQuery(String url, String userName, String passWord, String sqlPath) throws SQLException, FileNotFoundException {
        /*
         * Java 6 made this call unnecessary, see
         * https://docs.oracle.com/javase/8/docs/api/java/sql/DriverManager.html
         */
//        Class.forName("com.mysql.cj.jdbc.Driver").newInstance();

        /*
         * OPEN CONNECTION
         */
        Connection conn = DriverManager.getConnection(
                url + "?allowLoadLocalInfile=true", // The url query string referencing the driver setting for local data upload
                userName,
                passWord
        );
        Statement stmt = conn.createStatement();
        conn.setAutoCommit(false); // prevents database interaction before all the queries are loaded
        /*
         * ADD STATEMENTS FROM SQL FILE
         * Scanner reads the file in sections delimited by a semicolon, the
         * regex prevents the delimiter removal from the string by moving the
         * selection cursor right after the symbol. (Positive Lookbehind)
         */
        Scanner sc = new Scanner(new File(sqlPath)).useDelimiter("(?<=;)");
        while (sc.hasNext()) {
            stmt.addBatch(sc.next()); // Add every single query to the statement batch in a loop.
        }
        /*
         * SEND STATEMENT AND CLOSE
         */
        update(stmt.executeBatch()); // Run the batch and print the number of edits
        conn.commit();               // send the instructions to the database
        conn.close();                // close the connection
        sc.close();                  // close the scanner
    }

    /*
     * CONSOLE OUTPUT
     * Based on the update count returned from the executeBatch() command.
     */
    static void update(int[] updateCount) {
        for (int i = 0; i < updateCount.length; i++) {
            System.out.println("Query n. " + (i + 1) + ": " + updateCount[i] + " rows affected.");
        }
    }

    /*
     * MAIN METHOD
     */
    public static void main(String[] args) throws SQLException, FileNotFoundException {

        final String url = "jdbc:mysql://localhost";
        final String username = "pooa";
        final String password = "pooa";
        final String sqlPath = "src/minihw4and5/queries.sql";

        runQuery(url, username, password, sqlPath);

    }//main

}//class
