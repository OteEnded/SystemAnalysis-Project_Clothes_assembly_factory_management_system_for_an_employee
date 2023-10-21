module cs.ku {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;
    requires reflections;


    opens ku.cs to javafx.fxml;
    exports ku.cs;

    //บรรทัดนี้ เพื่อให้เข้าถึง class controller ที่อยู่ใน ku.cs.controller ได้
    exports ku.cs.controller;
    opens ku.cs.controller to javafx.fxml;
    exports ku.cs.entity;
    opens ku.cs.entity to java.base;
    exports ku.cs.service;
    opens ku.cs.service to javafx.fxml;
    exports ku.cs.testingarea_noJUnit;
    opens ku.cs.testingarea_noJUnit to javafx.fxml;
    exports ku.cs.utility;
    opens ku.cs.utility to javafx.fxml;
    exports ku.cs.model;
    opens ku.cs.model to java.base;
    exports ku.cs.tool;
    opens ku.cs.tool to java.base;

}