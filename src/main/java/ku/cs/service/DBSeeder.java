package ku.cs.service;

import ku.cs.entity.*;
import ku.cs.model.SQLTable;

import java.util.ArrayList;

public class DBSeeder {
    // list out tables that need to be seeded here
    private static ArrayList<SQLTable> tables = new ArrayList<>();
    static {
        tables.add(Users.getSqlTable());
        tables.add(Materials.getSqlTable());
        tables.add(Products.getSqlTable());
        tables.add(MaterialUsages.getSqlTable());
        tables.add(Works.getSqlTable());
        tables.add(DailyRecords.getSqlTable());
    }

}
