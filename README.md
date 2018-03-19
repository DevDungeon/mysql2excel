mysql2excel
=============

Command-line tool to dump MySQL tables to Microsoft Excel Spreadsheets written
in Java.


## Downloading

Download the precompiled .jar file or clone the repo from
https://github.com/DevDungeon/mysql2excel and then run:

    mvn package


## Usage

### Print help information

    java -jar mysql2excel-1.0.0.jar

### Generate a settings template file

    java -jar mysql2excel-1.0.0.jar -g sample.config

### Dump from MySQL to Excel using settings in config file

    java -jar mysql2excel-1.0.0.jar my.config


## Project Page

* https://www.devdungeon.com/content/mysql2excel
* https://www.github.com/DevDungeon/mysql2excel

## Contact

NanoDano <nanodano@devdungeon.com>


## Changelog

* In current dev branch (Planned release version: v1.0.1)
    * Added convert zeroDateTimeBehavior to convertToNull, minor formatting tweaks
* 2018-03-18 v1.0.0
    * Initial stable release
