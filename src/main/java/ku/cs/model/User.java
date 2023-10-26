package ku.cs.model;

import ku.cs.entity.Users;
import ku.cs.utility.ProjectUtility;

import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.HashMap;

public class User implements Row {

    private HashMap<String, Object> data = Users.getMap();

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
//        Users.load();
    }

    public User(HashMap<String, Object> data){
        setData(data);
    }

    public HashMap<String, Object> getData() {
        return data;
    }

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

    public void addData(String key, Object value){
        data.put(key, value);
    }

    public int save() throws SQLException, ParseException {
        return Users.save(this);
    }

    public void load(int id){
        load("U" + String.format("%05d", id));
    }

    public void load(String id){
        boolean isUserNew;
        try {
            isUserNew = Users.isNewUser(id);
        }
        catch (RuntimeException e){
            isUserNew = true;
        }
        if (isUserNew) throw new RuntimeException("Can find user with user_id: " + id);
        setData(Users.getData().get(id).getData());

    }

    public int delete() throws SQLException, ParseException {
        return Users.delete(this);
    }

    @Override
    public String getPrimaryKey() {
        return getId();
    }

    @Override
    public String toString(){
        return data.toString();
    }
}
