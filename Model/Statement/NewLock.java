package Model.Statement;

import Model.ProgramState;

public class NewLock implements IStatement {

    private String var;

    public NewLock(String var) {this.var = var;}

    @Override
    public ProgramState execute(ProgramState ps) {

        synchronized (ps.getLockTable()) {

            int id = ps.getLockTable().allocate(-1);
            ps.getSymTable().put(var, id);
        }
        return null;
    }

    @Override
    public String toString(){
        return "NewLock("+var+")";
    }

}
