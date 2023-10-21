package ku.cs.service;

import ku.cs.entity.*;
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

    public static void migrate() throws IllegalAccessException, SQLException {
        migrate(false);
    }

    public static void migrate(boolean dropAll) throws IllegalAccessException, SQLException {

        if (dropAll) {
            ProjectUtility.debug("Dropping all tables...");
            DataSourceDB.dropTableAll();
        }

        for (SQLTable table: tables){
            ProjectUtility.debug("Migrating "+ table.getCreateQuery() +"...");
            DataSourceDB.createTable(table);
        }

    }

}
