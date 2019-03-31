package Model.Utils;

import java.util.Map;
import java.util.HashMap;

public class MyLockTable<T> implements ILockTable<T> {

    private int indexes;
    private Map<Integer, T> values;

    public MyLockTable(Map<Integer, T> values) { this.values = values;}

    @Override
    public int allocate(T val)
    {
        ++this.indexes;
        this.values.put(this.indexes, val);

        return this.indexes;
    }

    @Override
    public void putValue(int id, T val) {
        this.values.put(id,val);
    }

    @Override
    public T getValue(int id) {
        return this.values.get(id);
    }

    @Override
    public T deallocate(int id) {
        return this.values.remove(id);
    }

    @Override
    public boolean contains(int id) {return this.values.containsKey(id);}

    @Override
    public void setTable(Map<Integer, T> table) {
        this.values = table;
    }

    @Override
    public Map<Integer, T> getTable() {
        return this.values;
    }

    @Override
    public String toString()
    {
        String s = "";
        boolean firstTime = true;

        for (HashMap.Entry<Integer, T> entry : this.values.entrySet()) {
            if (!firstTime)
                s += "\n";


            s += entry.getKey().toString() + " -> " + entry.getValue().toString();
            firstTime = false;
        }

        return s;
    }

}
