package ku.cs.service;

import ku.cs.entity.*;
import ku.cs.model.SQLRow;
import ku.cs.model.SQLTable;
import ku.cs.utility.ProjectUtility;

import java.sql.SQLException;
import java.util.*;

public class DBMigration {

    // list out tables that need to be created here
    private static ArrayList<SQLTable> tables = new ArrayList<>();
    static {
        tables.add(Users.getSqlTable());
        tables.add(Materials.getSqlTable());
        tables.add(Products.getSqlTable());
        tables.add(MaterialUsages.getSqlTable());
        tables.add(Works.getSqlTable());
        tables.add(DailyRecords.getSqlTable());
    }

    public static void migrate() throws SQLException {
        migrate(false);
    }

    public static void migrate(boolean dropAll) throws SQLException {

        if (dropAll) {
            ProjectUtility.debug("DBMigration[migrate(dropAll)]: Dropping all tables...");
            ProjectUtility.debug("DBMigration[migrate(dropAll)]: DROPPED ->", DataSourceDB.dropTableAll());
        }

        List<SQLRow> sqlRows = DataSourceDB.query("show tables");
        List<String> tableNames = new ArrayList<>();
        for (SQLRow sqlRow: sqlRows){
            tableNames.add((String) sqlRow.getValues().get(0));
        }

        for (SQLTable table: tables){
            ProjectUtility.debug("DBMigration[migrate]: Migrating "+ table.getName() +"...");
            boolean isExist = false;
            for (String tableName: tableNames){
                if (tableName.equalsIgnoreCase(table.getName())) {
                    isExist = true;
                    ProjectUtility.debug("DBMigration[migrate]: Table "+ table.getName() +" already exists.");
                    break;
                }
            }
            if (!isExist) {
                DataSourceDB.createTable(table);
            }
        }
    }

}
