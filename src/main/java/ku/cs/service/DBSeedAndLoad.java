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
        w2.setNote("ผ่าน");
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
        w4.setCreateDate(ProjectUtility.getDate()); // Excel : 2023-10-12
        w4.setDeadline(ProjectUtility.getDate(5)); // Excel : 2023-10-20
        w4.setGoalAmount(20);
        w4.setProgressAmount(20);
//        w4.setRepairWork(w1);
        w4.setNote("ไม่ผ่าน");
        w4.save();

        Work w5 = new Work();
        w5.setWorkType(Works.type_repair);
        w5.setProduct(p5);
        w5.setStatus(Works.status_waitForWorking);
        w5.setCreateDate(ProjectUtility.getDate()); // Excel : 2023-10-13
        w5.setDeadline(ProjectUtility.getDate(15)); // Excel : 2023-10-21
        w5.setGoalAmount(5);
        w5.setProgressAmount(0);
        w5.save();

        Work w6 = new Work();
        w6.setWorkType(Works.type_repair);
        w6.setProduct(p5);
        w6.setStatus(Works.status_waitForMaterial);
        w6.setCreateDate(ProjectUtility.getDate()); // Excel : 2023-10-14
        w6.setDeadline(ProjectUtility.getDate(15)); // Excel : 2023-10-22
        w6.setGoalAmount(10);
        w6.setProgressAmount(0);
        w6.save();

        Work w7 = new Work();
        w7.setWorkType(Works.type_normal);
        w7.setProduct(p4);
        w7.setStatus(Works.status_waitForAccept);
        w7.setCreateDate(ProjectUtility.getDate()); // Excel : 2023-10-14
        w7.setDeadline(ProjectUtility.getDate(15)); // Excel : 2023-10-31
        w7.setGoalAmount(30);
        w7.setProgressAmount(0);
        w7.save();

        Work w8 = new Work();
        w8.setWorkType(Works.type_normal);
        w8.setProduct(p2);
        w8.setStatus(Works.status_waitForAccept);
        w8.setCreateDate(ProjectUtility.getDate()); // Excel : 2023-10-15
        w8.setDeadline(ProjectUtility.getDate(15)); // Excel : 2023-11-10
        w8.setGoalAmount(25);
        w8.setProgressAmount(0);
        w8.save();

        Work w9 = new Work();
        w9.setWorkType(Works.type_normal);
        w9.setProduct(p3);
        w9.setStatus(Works.status_waitForWorking);
        w9.setCreateDate(ProjectUtility.getDate()); // Excel : 2023-10-16
        w9.setDeadline(ProjectUtility.getDate(15)); // Excel : 2023-11-11
        w9.setGoalAmount(30);
        w9.setProgressAmount(0);
        w9.save();

        Work w10 = new Work();
        w10.setWorkType(Works.type_normal);
        w10.setProduct(p1);
        w10.setStatus(Works.status_working);
        w10.setCreateDate(ProjectUtility.getDate()); // Excel : 2023-10-19
        w10.setDeadline(ProjectUtility.getDate(15)); // Excel : 2023-11-14
        w10.setGoalAmount(20);
        w10.setProgressAmount(20);
        w10.save();

        Work w11 = new Work();
        w11.setWorkType(Works.type_normal);
        w11.setProduct(p3);
        w11.setStatus(Works.status_working);
        w11.setCreateDate(ProjectUtility.getDate()); // Excel : 2023-10-20
        w11.setDeadline(ProjectUtility.getDate(15)); // Excel : 2023-11-15
        w11.setGoalAmount(20);
        w11.setProgressAmount(15);
        w11.save();
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
        dr1.setDate(ProjectUtility.getDate()); // Excel : 2023-09-27
        dr1.setAmount(2);
        dr1.save();

        DailyRecord dr2 = new DailyRecord();
        dr2.setForWork(w1);
        dr2.setDate(ProjectUtility.getDate()); // Excel : 2023-09-28
        dr2.setAmount(6);
        dr2.save();

        DailyRecord dr3 = new DailyRecord();
        dr3.setForWork(w1);
        dr3.setDate(ProjectUtility.getDate()); // Excel : 2023-09-29
        dr3.setAmount(8);
        dr3.save();

        DailyRecord dr4 = new DailyRecord();
        dr4.setForWork(w1);
        dr4.setDate(ProjectUtility.getDate()); // Excel : 2023-09-30
        dr4.setAmount(6);
        dr4.save();

        DailyRecord dr5 = new DailyRecord();
        dr5.setForWork(w2);
        dr5.setDate(ProjectUtility.getDate()); // Excel : 20203-10-01
        dr5.setAmount(4);
        dr5.save();

        DailyRecord dr6 = new DailyRecord();
        dr6.setForWork(w2);
        dr6.setDate(ProjectUtility.getDate()); // Excel : 2023-10-02
        dr6.setAmount(6);
        dr6.save();

        DailyRecord dr7 = new DailyRecord();
        dr7.setForWork(w2);
        dr7.setDate(ProjectUtility.getDate()); // Excel : 2023-10-03
        dr7.setAmount(5);
        dr7.save();

        DailyRecord dr8 = new DailyRecord();
        dr8.setForWork(w2);
        dr8.setDate(ProjectUtility.getDate()); // Excel : 2023-10-04
        dr8.setAmount(5);
        dr8.save();

        DailyRecord dr9 = new DailyRecord();
        dr9.setForWork(w2);
        dr9.setDate(ProjectUtility.getDate()); // Excel : 2023-10-05
        dr9.setAmount(7);
        dr9.save();

        DailyRecord dr10 = new DailyRecord();
        dr10.setForWork(w2);
        dr10.setDate(ProjectUtility.getDate()); // Excel : 2023-10-06
        dr10.setAmount(3);
        dr10.save();

        DailyRecord dr11 = new DailyRecord();
        dr11.setForWork(w3);
        dr11.setDate(ProjectUtility.getDate()); // Excel : 2023-10-02
        dr11.setAmount(6);
        dr11.save();

        DailyRecord dr12 = new DailyRecord();
        dr12.setForWork(w3);
        dr12.setDate(ProjectUtility.getDate()); // Excel : 2023-10-03
        dr12.setAmount(4);
        dr12.save();

        DailyRecord dr13 = new DailyRecord();
        dr13.setForWork(w3);
        dr13.setDate(ProjectUtility.getDate()); // Excel : 2023-10-04
        dr13.setAmount(7);
        dr13.save();

        DailyRecord dr14 = new DailyRecord();
        dr14.setForWork(w3);
        dr14.setDate(ProjectUtility.getDate()); // Excel : 2023-10-05
        dr14.setAmount(8);
        dr14.save();

        DailyRecord dr15 = new DailyRecord();
        dr15.setForWork(w7);
        dr15.setDate(ProjectUtility.getDate()); // Excel : 2023-10-14
        dr15.setAmount(5);
        dr15.save();

        DailyRecord dr16 = new DailyRecord();
        dr16.setForWork(w8);
        dr16.setDate(ProjectUtility.getDate()); // Excel : 2023-10-04
        dr16.setAmount(5);
        dr16.save();

        DailyRecord dr17 = new DailyRecord();
        dr17.setForWork(w8);
        dr17.setDate(ProjectUtility.getDate()); // Excel : 2023-10-05
        dr17.setAmount(5);
        dr17.save();

        DailyRecord dr18 = new DailyRecord();
        dr18.setForWork(w8);
        dr18.setDate(ProjectUtility.getDate()); // Excel : 2023-10-06
        dr18.setAmount(5);
        dr18.save();

        DailyRecord dr19 = new DailyRecord();
        dr19.setForWork(w8);
        dr19.setDate(ProjectUtility.getDate()); // Excel : 2023-10-07
        dr19.setAmount(5);
        dr19.save();

        DailyRecord dr20 = new DailyRecord();
        dr20.setForWork(w10);
        dr20.setDate(ProjectUtility.getDate()); // Excel : 2023-11-08
        dr20.setAmount(5);
        dr20.save();

        DailyRecord dr21 = new DailyRecord();
        dr21.setForWork(w10);
        dr21.setDate(ProjectUtility.getDate()); // Excel : 2023-11-09
        dr21.setAmount(6);
        dr21.save();

        DailyRecord dr22 = new DailyRecord();
        dr22.setForWork(w10);
        dr22.setDate(ProjectUtility.getDate()); // Excel : 2023-11-10
        dr22.setAmount(3);
        dr22.save();

        DailyRecord dr23 = new DailyRecord();
        dr23.setForWork(w10);
        dr23.setDate(ProjectUtility.getDate()); // Excel : 2023-11-11
        dr23.setAmount(7);
        dr23.save();

        DailyRecord dr24 = new DailyRecord();
        dr24.setForWork(w11);
        dr24.setDate(ProjectUtility.getDate()); // Excel : 2023-11-10
        dr24.setAmount(4);
        dr24.save();

        DailyRecord dr25 = new DailyRecord();
        dr25.setForWork(w11);
        dr25.setDate(ProjectUtility.getDate()); // Excel : 2023-11-11
        dr25.setAmount(3);
        dr25.save();

        DailyRecord dr26 = new DailyRecord();
        dr26.setForWork(w11);
        dr26.setDate(ProjectUtility.getDate()); // Excel : 2023-11-12
        dr26.setAmount(8);
        dr26.save();
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
