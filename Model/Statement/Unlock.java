package Model.Statement;

import Model.ProgramState;

public class Unlock implements IStatement{

    private String var;

    public Unlock(String var) {this.var = var;}

    @Override
    public ProgramState execute(ProgramState ps) {

        Integer id = ps.getSymTable().get(var);

        if(id == null)
            throw new RuntimeException("[In Lock] Invalid key in symtable!");

        synchronized (ps.getLockTable()) {

            if(ps.getLockTable().contains(id))
                if(ps.getLockTable().getValue(id) == ps.getId())
                    ps.getLockTable().putValue(id, -1);
        }

        return null;
    }

    @Override
    public String toString(){
        return "Unlock("+var+")";
    }

}
