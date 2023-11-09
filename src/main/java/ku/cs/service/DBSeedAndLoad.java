package ku.cs.service;

import ku.cs.entity.*;
import ku.cs.model.Material;
import ku.cs.model.Product;
import ku.cs.model.Work;
import ku.cs.model.*;
import ku.cs.utility.ProjectUtility;

import java.sql.SQLException;
import java.text.ParseException;

public class DBSeedAndLoad {

    public static void quickLoad() throws SQLException {
        Products.load();
        Works.load();
    }

    public static void load() throws SQLException {
        Products.load();
        Materials.load();
        MaterialUsages.load();
        Works.load();
        DailyRecords.load();
    }

    public static void seed() throws SQLException, ParseException {
//      Section: Products
        Product p1 = new Product();
        p1.setName("กระโปรงขาว");
        p1.setSize(26);
        p1.setProgressRate(5.375);
        p1.save();

        Product p2 = new Product();
        p2.setName("กระโปรงดำ");
        p2.setSize(28);
        p2.setProgressRate(-1);
        p2.save();

        Product p3 = new Product();
        p3.setName("กางเกงขาว");
        p3.setSize(28);
        p3.setProgressRate(5);
        p3.save();

        Product p4 = new Product();
        p4.setName("กางเกงดำ");
        p4.setSize(30);
        p4.setProgressRate(4);
        p4.save();

        Product p5 = new Product();
        p5.setName("เสื้อขาว");
        p5.setSize(34);
        p5.setProgressRate(5.625);
        p5.save();
//      End Section: Products

//      Section: Works
        Work w1 = new Work();
        w1.setWorkType(Works.type_normal);
        w1.setProduct(p1);
        w1.setStatus(Works.status_checked);
        w1.setCreateDate(ProjectUtility.getDate()); // Excel : 2023-09-27
        w1.setDeadline(ProjectUtility.getDate(10)); // Excel : 2023-09-31
        w1.setGoalAmount(20);
        w1.setProgressAmount(20);
        w1.setNote("-");
        w1.save();

        Work w2 = new Work();
        w2.setWorkType(Works.type_normal);
        w2.setProduct(p2);
        w2.setStatus(Works.status_checked);
        w2.setCreateDate(ProjectUtility.getDate()); // Excel : 2023-09-28
        w2.setDeadline(ProjectUtility.getDate(15)); // Excel : 2023-10-07
        w2.setGoalAmount(30);
        w2.setProgressAmount(30);
        w2.save();

        Work w3 = new Work();
        w3.setWorkType(Works.type_rush);
        w3.setProduct(p3);
        w3.setStatus(Works.status_done);
        w3.setCreateDate(ProjectUtility.getDate()); // Excel : 2023-09-28
        w3.setDeadline(ProjectUtility.getDate(18)); // Excel : 2023-10-15
        w3.setGoalAmount(25);
        w3.setProgressAmount(25);
        w3.save();

        Work w4 = new Work();
        w4.setWorkType(Works.type_repair);
        w4.setProduct(p4);
        w4.setStatus(Works.status_checked);
        w4.setCreateDate(ProjectUtility.getDate());
        w4.setDeadline(ProjectUtility.getDate(5));
        w4.setGoalAmount(20);
        w4.setProgressAmount(20);
//        w4.setRepairWork(w1);
        w4.save();

        Work w5 = new Work();
        w5.setWorkType(Works.type_normal);
        w5.setProduct(p5);
        w5.setStatus(Works.status_waitForAccept);
        w5.setCreateDate(ProjectUtility.getDate());
        w5.setDeadline(ProjectUtility.getDate(15));
        w5.setGoalAmount(30);
        w5.setProgressAmount(0);
        w5.save();
//      End Section: Works

//      Section: Materials
        Material m1 = new Material();
        m1.setName("ผ้าขาว หน้ากว้าง 60 นิ้ว");
        m1.setUnitName("เมตร");
        m1.save();

        Material m2 = new Material();
        m2.setName("ผ้าดำ หน้ากว้าง 60 นิ้ว");
        m2.setUnitName("เมตร");
        m2.save();

        Material m3 = new Material();
        m3.setName("ด้ายขาว");
        m3.setUnitName("ม้วน");
        m3.save();

        Material m4 = new Material();
        m4.setName("ด้ายดำ");
        m4.setUnitName("ม้วน");
        m4.save();

        Material m5 = new Material();
        m5.setName("กระดุม");
        m5.setUnitName("เม็ด");
        m5.save();

        Material m6 = new Material();
        m6.setName("ซิป");
        m6.setUnitName("เส้น");
        m6.save();

//      End Section: Materials

//      Section: DailyRecords
        DailyRecord dr1 = new DailyRecord();
        dr1.setForWork(w1);
        dr1.setDate(ProjectUtility.getDate());
        dr1.setAmount(2);
        dr1.save();

        DailyRecord dr2 = new DailyRecord();
        dr2.setForWork(w2);
        dr2.setDate(ProjectUtility.getDate());
        dr2.setAmount(4);
        dr2.save();

        DailyRecord dr3 = new DailyRecord();
        dr3.setForWork(w3);
        dr3.setDate(ProjectUtility.getDate());
        dr3.setAmount(6);
        dr3.save();

        DailyRecord dr4 = new DailyRecord();
        dr4.setForWork(w4);
        dr4.setDate(ProjectUtility.getDate());
        dr4.setAmount(8);
        dr4.save();

        DailyRecord dr5 = new DailyRecord();
        dr5.setForWork(w5);
        dr5.setDate(ProjectUtility.getDate());
        dr5.setAmount(10);
        dr5.save();

//      End Section: DailyRecords

//      Section: MaterialUsages
//      Disclaimer: This section will not use the MaterialUsage class.
//      Instead, it will use the Product class to access and save MaterialUsages.
        p1.saveMaterialUsed(m1, 2, 1);
        p1.saveMaterialUsed(m3, 1, 40);
        p1.saveMaterialUsed(m6, 1, 1);
        p1.save();

        p2.saveMaterialUsed(m2, 2, 1);
        p2.saveMaterialUsed(m4, 1, 40);
        p2.saveMaterialUsed(m6, 1, 1);
        p2.save();

        p3.saveMaterialUsed(m1, 1, 2);
        p3.saveMaterialUsed(m3, 1, 40);
        p3.saveMaterialUsed(m5, 1, 1);
        p3.saveMaterialUsed(m6, 1, 1);
        p3.save();

        p4.saveMaterialUsed(m2, 1, 2);
        p4.saveMaterialUsed(m4, 1, 40);
        p4.saveMaterialUsed(m5, 1, 1);
        p4.saveMaterialUsed(m6, 1, 1);
        p4.save();

        p5.saveMaterialUsed(m1, 3, 2);
        p5.saveMaterialUsed(m3, 1, 40);
        p5.saveMaterialUsed(m5, 5, 1);
        p5.save();
//      End Section: MaterialUsages
    }
}
