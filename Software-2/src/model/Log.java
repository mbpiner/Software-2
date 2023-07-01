
package model;

import java.io.BufferedWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.io.PrintWriter;
import java.io.FileWriter;

public class Log {

    private static final String FILENAME = "log.txt";
    private static final ZonedDateTime now = ZonedDateTime.now();
    public static void log(String username, boolean success) {
        try (FileWriter fileWriter = new FileWriter(FILENAME, true);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                PrintWriter printWriter = new PrintWriter(bufferedWriter)) {
            printWriter.println(now + " " + username + (success ? " Success" : " Failure"));
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
