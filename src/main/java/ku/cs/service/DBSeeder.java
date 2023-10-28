package ku.cs.service;

import ku.cs.entity.*;
import ku.cs.model.Material;
import ku.cs.model.Product;
import ku.cs.model.SQLTable;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;

public class DBSeeder {
    // list out tables that need to be seeded here
    private static final ArrayList<SQLTable> tables = new ArrayList<>();
    static {
        tables.add(Users.getSqlTable());
        tables.add(Materials.getSqlTable());
        tables.add(Products.getSqlTable());
        tables.add(MaterialUsages.getSqlTable());
        tables.add(Works.getSqlTable());
        tables.add(DailyRecords.getSqlTable());
    }

    public static void seed() throws SQLException, ParseException {
        Product p1 = new Product();
        p1.setName("pant");
        p1.setSize(15);
        p1.setProgressRate(5.00);
        p1.save();

        Product p2 = new Product();
        p2.setName("shirt");
        p2.setSize(10);
        p2.setProgressRate(5.00);
        p2.save();

        Material m1 = new Material();
        m1.setName("cloth");
        m1.setUnitName("meter");
        m1.save();

        Material m2 = new Material();
        m2.setName("button");
        m2.setUnitName("piece");
        m2.save();

        Material m3 = new Material();
        m3.setName("thread");
        m3.setUnitName("meter");
        m3.save();
    }
}
