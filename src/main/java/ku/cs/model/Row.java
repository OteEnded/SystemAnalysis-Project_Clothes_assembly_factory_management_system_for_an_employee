package ku.cs.model;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;

public interface Row {

    HashMap<String, Object> getData();
    void setData(HashMap<String, Object> data);
    void load(int id) throws SQLException;
    void load(String id) throws SQLException;
    int save() throws SQLException, ParseException;
    int delete() throws SQLException, ParseException;
    HashMap<String, Object> getPrimaryKeys();

}
