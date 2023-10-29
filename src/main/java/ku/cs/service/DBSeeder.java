package ku.cs.service;

import ku.cs.entity.*;
import ku.cs.model.Material;
import ku.cs.model.Product;
import ku.cs.model.SQLTable;
import ku.cs.model.Work;
import ku.cs.utility.ProjectUtility;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;

public class DBSeeder {
    public static void seed() throws SQLException, ParseException {
//      Section: Products
        Product p1 = new Product();
        p1.setName("กระโปรง");
        p1.setSize(26);
        p1.save();

        Product p2 = new Product();
        p2.setName("กระโปรง");
        p2.setSize(28);
        p2.save();

        Product p3 = new Product();
        p3.setName("กางเกง");
        p3.setSize(28);
        p3.save();

        Product p4 = new Product();
        p4.setName("กางเกง");
        p4.setSize(30);
        p4.save();

        Product p5 = new Product();
        p5.setName("เสื้อ");
        p5.setSize(34);
        p5.save();
//      End Section: Products

//      Section: Works
        Work w1 = new Work();
        w1.setWorkType(Works.type_normal);
        w1.setProduct(p1);
        w1.setStatus(Works.status_checked);
        w1.setCreateDate(ProjectUtility.getDate());
        w1.setStartDate(ProjectUtility.getDate());
        w1.setDeadline(ProjectUtility.getDate());
        w1.setGoalAmount(20);
        w1.setProgressAmount(20);
        w1.setNote("-");
        w1.save();

        Work w2 = new Work();
        w2.setWorkType(Works.type_normal);
        w2.setProduct(p2);
        w2.setStatus(Works.status_sent);
        w2.setCreateDate(ProjectUtility.getDate());
        w2.setStartDate(ProjectUtility.getDate());
        w2.setDeadline(ProjectUtility.getDate());
        w2.setGoalAmount(30);
        w2.setProgressAmount(30);
        w2.save();

        Work w3 = new Work();
        w3.setWorkType(Works.type_rush);
        w3.setProduct(p3);
        w3.setStatus(Works.status_done);
        w3.setCreateDate(ProjectUtility.getDate());
        w3.setStartDate(ProjectUtility.getDate());
        w3.setDeadline(ProjectUtility.getDate());
        w3.setGoalAmount(25);
        w3.setProgressAmount(25);
        w3.save();

        Work w4 = new Work();
        w4.setWorkType(Works.type_repair);
        w4.setProduct(p4);
        w4.setStatus(Works.status_waitForMaterial);
        w4.setCreateDate(ProjectUtility.getDate());
        w4.setStartDate(ProjectUtility.getDate());
        w4.setDeadline(ProjectUtility.getDate());
        w4.setGoalAmount(10);
        w4.setProgressAmount(0);
        w4.setRepairWork(w1);
        w4.save();

        Work w5 = new Work();
        w5.setWorkType(Works.type_normal);
        w5.setProduct(p5);
        w5.setStatus(Works.status_waitForAccept);
        w5.setCreateDate(ProjectUtility.getDate());
        w5.setStartDate(ProjectUtility.getDate());
        w5.setDeadline(ProjectUtility.getDate());
        w5.setGoalAmount(30);
        w5.setProgressAmount(0);
        w5.save();
//      End Section: Works

//      Section: Materials
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
//      End Section: Materials
    }
}
