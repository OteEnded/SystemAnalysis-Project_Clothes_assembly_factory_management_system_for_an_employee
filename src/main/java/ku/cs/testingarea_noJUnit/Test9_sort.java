package ku.cs.testingarea_noJUnit;

import ku.cs.entity.Works;
import ku.cs.utility.ProjectUtility;

public class Test9_sort {
    public static void main(String[] args) {
        ProjectUtility.debug(Works.getSortedBy("created_date"));
    }
}
