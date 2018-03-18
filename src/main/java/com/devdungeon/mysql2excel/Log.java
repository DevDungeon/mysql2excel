package com.devdungeon.mysql2excel;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Simple printing and logging wrappers
 * 
 * @author NanoDano <nanodano@devdungeon.com>
 */
public class Log {

    static void logSevere(String message) {
        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, message);
    }

    static void logException(String message, Exception ex) {
        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, message);
        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
    }

    static void log(Object message) {
        System.out.println(message.toString());
    }
    
}
