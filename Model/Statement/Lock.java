package Model.Statement;

import Model.ProgramState;

public class Lock implements IStatement {

    private String var;

    public Lock(String var) {this.var = var;}

    @Override
    public ProgramState execute(ProgramState ps)
    {
        Integer id = ps.getSymTable().get(var);

        if(id == null)
            throw new RuntimeException("[In Lock] Invalid key in symtable!");

        synchronized (ps.getLockTable()) {

            if(!ps.getLockTable().contains(id))
                throw new RuntimeException("[In Lock] Invalid id in lock table!");

            if (ps.getLockTable().getValue(id) == -1)
                ps.getLockTable().putValue(id, ps.getId());
            else
                ps.getExeStack().push(this);
        }
        return null;
    }

    @Override
    public String toString(){
        return "Lock("+var+")";
    }
}
