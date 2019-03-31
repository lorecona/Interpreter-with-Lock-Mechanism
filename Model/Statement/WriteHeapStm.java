package Model.Statement;

import Model.Expression.IExpression;
import Model.ProgramState;
import Exception.ExpressionException;

public class WriteHeapStm implements IStatement {

    private String id;
    private IExpression expr;

    public WriteHeapStm(String id, IExpression expr) {
        this.id = id;
        this.expr = expr;
    }

    @Override
    public ProgramState execute(ProgramState ps) {
        Integer address = ps.getSymTable().get(id);

        if(address == null)
            throw new ExpressionException("(In WriteHeapStm) Unknown variable!");

        Integer value = this.expr.evaluate(ps.getSymTable(), ps.getHeap());
        ps.getHeap().putValue(address, value);

        return null;
    }

    @Override
    public String toString() {
        return "writeHeap(" + id + ", " + expr + ")";
    }

}
