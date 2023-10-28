package ku.cs.model;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;

public interface Row {

    void setData(HashMap<String, Object> data);
    HashMap<String, Object> getData();
    void load(String primaryKeys) throws SQLException;
    int save() throws SQLException, ParseException;
    int delete() throws SQLException, ParseException;
    HashMap<String, Object> getPrimaryKeys();

}
