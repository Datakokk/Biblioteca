package com.biblioteca.util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LoggerUtil {

    private static final String LOG_FILE = "log.txt";

    public static void registrarEvento(String mensaje) {
        try (FileWriter fileWriter = new FileWriter(LOG_FILE, true);
             PrintWriter printWriter = new PrintWriter(fileWriter)) {
            
            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            printWriter.println(timestamp + " - " + mensaje);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
