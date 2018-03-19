package com.devdungeon.mysql2excel;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Handles the connection to the database and dumping the data to a .xlsx file.
 * 
 * @author NanoDano <nanodano@devdungeon.com>
 */
public class MysqlDumper {

    /**
     * Given database credentials and table name, this function connects to the
     * database and then dumps all of the data to a spreadsheet file including a
     * header row.
     * 
     * @param outputFileName Name of output file (e.g. output.xlsx)
     * @param dbHost Database host (e.g. localhost)
     * @param dbName Name of database
     * @param dbUser Database username
     * @param dbPass Database user password
     * @param tableName Single table name in database
     */
    static void dumpMysqlToExcelFile(String outputFileName, String dbHost, String dbName, String dbUser, String dbPass, String tableName) {
        // Create spreadsheet
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet mySheet = workbook.createSheet("Data");
        // Connect to database
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://" + dbHost + ":3306/" + dbName + "?zeroDateTimeBehavior=convertToNull", dbUser, dbPass);
        } catch (SQLException ex) {
            Log.logException("Error connecting to database.", ex);
        }
        if (conn == null) {
            Log.logSevere("No connection established. Exiting.");
            System.exit(1);
        }
        Log.log("Connected.");
        
        ArrayList<String> columnNames = new ArrayList();
        // Get list of column names and write sheet headers
        int cellNum = 0;
        try {
            stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery("DESCRIBE " + tableName);
            Row row = mySheet.createRow(0); // Header row
            while (results.next()) {
                // Each column name in the table
                Cell cell = row.createCell(cellNum++);
                cell.setCellValue(results.getString(1));
                columnNames.add(results.getString(1));
            }
        } catch (SQLException ex) {
            Log.logException("Error making SQL query to check table column headers.", ex);
        }
        if (stmt == null) {
            Log.logSevere("Statement is null when it shouldn't be. Exiting.");
            System.exit(1);
        }
        Log.log("Columns found: " + columnNames.size());
        Log.log(columnNames);
        // Get list of all data and dump
        Log.log("Dumping data...");
        int rowNum = 1;
        try {
            stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery("SELECT * FROM " + tableName);
            // write each field to db column and append row
            while (results.next()) {
                Row row = mySheet.createRow(rowNum++); // 0-based index
                int cellnum = 0;
                for (String colName : columnNames) {
                    Cell cell = row.createCell(cellnum++);
                    cell.setCellValue(results.getString(colName));
                }
            }
        } catch (SQLException ex) {
            Log.logException("Error creating prepared statement.", ex);
        }
        // Save spreadsheet
        FileOutputStream os;
        try {
            os = new FileOutputStream(outputFileName);
            workbook.write(os);
            Log.log("Writing on XLSX file Finished ...");
        } catch (IOException ex) {
            Log.logException("Error writing Excel file.", ex);
        }
        // Clean up db stuff
        try {
            stmt.close();
            conn.close();
        } catch (SQLException ex) {
            Log.logException("Error closing statement and database. Exiting", ex);
            System.exit(1);
        }
    }
    
}
