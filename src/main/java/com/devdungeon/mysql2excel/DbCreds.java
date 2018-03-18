package com.devdungeon.mysql2excel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

/**
 *
 * @author NanoDano <nanodano@devdungeon.com>
 */
class DbCreds {

    String dbName;
    String dbHost;
    String dbUser;
    String dbPass;
    String dbTable;
    String outFile;

    static DbCreds loadConfig(String configFileName) {
        DbCreds creds = new DbCreds();
        File configFile = new File(configFileName);
        try {
            FileReader reader = new FileReader(configFile);
            Properties props = new Properties();
            props.load(reader);
            creds.dbHost = props.getProperty("dbHost");
            creds.dbName = props.getProperty("dbName");
            creds.dbPass = props.getProperty("dbPass");
            creds.dbTable = props.getProperty("dbTable");
            creds.dbUser = props.getProperty("dbUser");
            creds.outFile = props.getProperty("outFile");
            reader.close();
        } catch (FileNotFoundException ex) {
            Log.log("Configuration file not found. Expected to find mysql2excel.config file. Try mysql2excel --generate-config-template.");
            System.exit(1);
        } catch (IOException ex) {
            Log.logException("Error reading from file.", ex);
        }
        return creds;
    }

    static void generateTemplateSettingsFile(String outfileName) {
        Properties props = new Properties();
        props.setProperty("dbHost", "localhost");
        props.setProperty("dbName", "myDb");
        props.setProperty("dbUser", "linusTorvalds");
        props.setProperty("dbPass", "iHatePenguins");
        props.setProperty("dbTable", "myTable");
        props.setProperty("outFile", "output.xlsx");
        
        File outfile = new File(outfileName);
        OutputStream outstream;
        try {
            outstream = new FileOutputStream(outfile);
            props.store(outstream, "Leave dbPass blank for no pass.");
        } catch (IOException ex) {
            Log.logException("Error creating configuration file.", ex);
        }
    }

}
