package ku.cs.model;

import java.util.HashMap;

public interface DataList<T> {

    void setInfo(HashMap<String, T> info);
    void load();
    HashMap<String, T> getInfo();
    void add(T t);
    void delete(String key);
    T getInfoOf(String key);
    void save();

}
