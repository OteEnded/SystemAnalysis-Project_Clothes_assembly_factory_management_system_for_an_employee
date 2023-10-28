package ku.cs.model;

import ku.cs.entity.Users;
import ku.cs.utility.EntityUtility;

import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class User implements Row {

    private HashMap<String, Object> data = EntityUtility.getMap(Users.getSqlTable());

    public User() throws SQLException {
        this("null", 0);
    }

    public User(String name) throws SQLException {
        this(name, 0);
    }

    public User(String name, int age){
        data.put("name", name);
        data.put("age", age);
        data.put("sign_up_date", LocalDate.now().toString());
    }

    public User(HashMap<String, Object> data){
        setData(data);
    }

    @Override
    public HashMap<String, Object> getData() {
        return data;
    }

    @Override
    public void setData(HashMap<String, Object> data) {
        this.data = data;
    }

    public String getId() {
        return (String) data.get("id");
    }

    public void setId(String id) {
        data.put("id", id);
    }

    public String getName() {
        return (String) data.get("name");
    }

    public void setName(String name) {
        data.put("name", name);
    }

    public int getAge() {
        return (int) data.get("age");
    }

    public void setAge(int age) {
        data.put("age", age);
    }

    public String getSignUpDate() {
        return (String) data.get("sign_up_date");
    }

    public void setSignUpDate(String signUpDate) {
        data.put("sign_up_date", signUpDate);
    }

    @Override
    public void load(int id) throws SQLException {
        load(EntityUtility.idFormatter(Users.getSqlTable(), id));
    }

    @Override
    public void load(String id) throws SQLException {
        Users.load();
        boolean isUserNew;
        try {
            isUserNew = Users.isNew(id);
        }
        catch (RuntimeException e){
            isUserNew = true;
        }
        if (isUserNew) throw new RuntimeException("User[load]: Can't find user with user_id: " + id);
        setData(Users.getData().get(id).getData());
    }

    @Override
    public int save() throws SQLException, ParseException {
        return Users.save(this);
    }

    @Override
    public int delete() throws SQLException, ParseException {
        return Users.delete(this);
    }

    @Override
    public HashMap<String, Object> getPrimaryKeys() {
        HashMap<String, Object> primaryKeys = new HashMap<>();
        for (SQLColumn sqlColumn : Users.getSqlTable().getPrimaryKeys()) {
            primaryKeys.put(sqlColumn.getName(), data.get(sqlColumn.getName()));
        }
        return primaryKeys;
    }

    @Override
    public String toString(){
        return "Object-User: " + data.toString();
    }
}
