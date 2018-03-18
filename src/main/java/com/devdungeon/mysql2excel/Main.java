package com.devdungeon.mysql2excel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * This program dumps a MySQL table to a Microsoft Excel .xlsx spreadsheet.
 * Database credentials, table name, and output file are configured through a
 * properties file. A sample properties file can be generated.
 *
 * @author NanoDano <nanodano@devdungeon.com>
 */
public class Main {

    /**
     * The program name used throughout the application to refer to itself.
     * Intended to match the name of the final script that launches the program
     *
     * e.g. a bash launcher script named mysql2excel ``` #!/usr/bin/bash java
     * -jar ~/bin/mysql2excel-1.0.0.jar "$@" ```
     */
    static final String PROGRAM_NAME = "mysql2excel";

    /**
     * Print command line usage options to provide user instructions.
     */
    static void printUsage() {
        Log.log(PROGRAM_NAME + " - Dump a MySQL table to an Excel .xlsx spreadsheet.\n"
                + "Usage:\n"
                + "  " + PROGRAM_NAME + " [-h|--help]\n"
                + "  " + PROGRAM_NAME + " (-v|--version)\n"
                + "  " + PROGRAM_NAME + " (-g|--generate-config-template) <outputFilename>\n"
                + "  " + PROGRAM_NAME + " <configFile>\n"
                + "\nExample:\n"
                + "  " + PROGRAM_NAME + " -g sample.config\n"
                + "  " + PROGRAM_NAME + " my.config\n"
        );
    }

    /**
     * Handle the command line arguments. Prints error messages or usage
     * information and exits as needed.
     *
     * @param args
     */
    static void processArgs(String[] args) {
        // -h | --help
        if (args.length == 0 || "-h".equals(args[0]) || "--help".equals(args[0])) {
            printUsage();
            System.exit(0);
        }
        // -v | --version
        if ("-v".equals(args[0]) || "--version".equals(args[0])) {
            String version = Main.class.getPackage().getImplementationVersion();
            Log.log(PROGRAM_NAME + " " + version);
            System.exit(0);
        }
        // -g | --generate-config-template
        if ("-g".equals(args[0]) || "--generate-config-template".equals(args[0])) {
            if (args.length != 2) {
                Log.log("Cannot generate a config file without knowing the output filename.");
                Log.log("Try:\n  " + PROGRAM_NAME + " -g sample.config");
                Log.log("Then modify sample.config and run:\n  "
                        + PROGRAM_NAME + " sample.config");
                System.exit(1);
            }
            Log.log("Generating properties template file: " + args[1]);
            DbCreds.generateTemplateSettingsFile(args[1]);
            System.exit(0);
        }

        // The only case left now is the one where a single arg should be
        // provided and it is the name of the properties file
        if (args.length != 1) {
            Log.log("Unknown usage.");
            printUsage();
            System.exit(1);
        }

        // If end of function is reached, no special flags have been provided,
        // and rest of program should continue as normal
    }

    /**
     * Main program entry
     *
     * @param args
     */
    public static void main(String[] args) {
        // Handle command line arguments
        processArgs(args);

        // Load configuration file
        DbCreds creds = DbCreds.loadConfig(args[0]);
        Log.log("Database host: \t" + creds.dbHost);
        Log.log("Database Name: \t" + creds.dbName);
        Log.log("Database Table: \t" + creds.dbTable);
        Log.log("Database User: \t" + creds.dbUser);
        Log.log("Output File: \t" + creds.outFile);

        // Print starting time
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime startTime = LocalDateTime.now();
        Log.log("Starting at " + dtf.format(startTime));

        // Dump data from MySQL to Excel file
        MysqlDumper.dumpMysqlToExcelFile(creds.outFile,
                creds.dbHost,
                creds.dbName,
                creds.dbUser,
                creds.dbPass,
                creds.dbTable);

        // Time elapsed
        LocalDateTime endTime = LocalDateTime.now();
        Log.log("Completed: " + dtf.format(endTime));
        long timeDelta = startTime.until(endTime, ChronoUnit.SECONDS);
        Log.log("Duration in seconds: " + timeDelta);
    }

}
