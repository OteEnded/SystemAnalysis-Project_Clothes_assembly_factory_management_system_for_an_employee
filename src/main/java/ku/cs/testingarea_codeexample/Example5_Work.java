package ku.cs.testingarea_codeexample;

import ku.cs.entity.Works;
import ku.cs.model.Work;
import ku.cs.utility.ProjectUtility;

import java.sql.SQLException;

public class Example5_Work {
    public static void main(String[] args) throws SQLException {

        ProjectUtility.debug(Works.getData());

        Work work = new Work();
        work.load(1);
    }
}
