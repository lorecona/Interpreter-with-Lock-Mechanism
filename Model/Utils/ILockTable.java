package Model.Utils;

import java.util.Map;

public interface ILockTable<T> {
    int allocate(T val);
    void putValue(int id, T val);
    T getValue(int id);
    T deallocate(int id);
    boolean contains(int id);

    void setTable(Map<Integer, T> table);
    Map<Integer, T> getTable();
    String toString();

}
