package Model;

import Model.Statement.IStatement;
import Model.Utils.*;
import java.util.HashMap;
import java.io.BufferedReader;
import javafx.util.Pair;
import Exception.*;

public class ProgramState {
    private MyStack<IStatement> exeStack;
    private MyList<Integer> output;
    private IDictionary<String, Integer> symTable;
    private IStatement program;
    private MyDictionary<Integer, Pair<String, BufferedReader>> fileTable;
    private MyHeap<Integer> heap;
    private MyLockTable<Integer> lockTable;
    private int id;


    public ProgramState(IStatement program) {
        this.exeStack = new MyStack<>();
        this.output = new MyList<>();
        this.symTable = new MyDictionary<>();
        this.fileTable = new MyDictionary<>();
        this.program = program;
        this.heap = new MyHeap<Integer>(new HashMap<Integer, Integer>());
        this.lockTable = new MyLockTable<Integer>(new HashMap<Integer, Integer>());
        this.id = 1;

        this.exeStack.push(program);
    }

    public ProgramState(IStatement program,
                        MyStack<IStatement> exeStack,
                        MyList<Integer> output,
                        IDictionary<String, Integer> symTable,
                        MyDictionary<Integer, Pair<String, BufferedReader>> fileTable,
                        MyHeap<Integer> heap,
                        MyLockTable<Integer> lockTable,
                        int threadId)
    {
        this.program = program;
        this.exeStack = exeStack;
        this.output = output;
        this.symTable = symTable;
        this.fileTable = fileTable;
        this.heap = heap;
        this.lockTable = lockTable;
        this.id = threadId;

        this.exeStack.push(this.program);
    }

    public IDictionary<String, Integer> getSymTable() {
        return symTable;
    }

    public void setSymTable(MyDictionary<String, Integer> symTable) {
        this.symTable = symTable;
    }

    public MyStack<IStatement> getExeStack() {
        return exeStack;
    }

    public void setExeStack(MyStack<IStatement> exeStack) {
        this.exeStack = exeStack;
    }

    public MyList<Integer> getOutput() { return output; }

    public void setOutput(MyList<Integer> output) {
        this.output = output;
    }


    public void setProgram(IStatement program) {
        this.program = program;
    }

    public IStatement getProgram() {
        return this.program;
    }

    public void setFileTable(MyDictionary<Integer, Pair<String, BufferedReader>> fileTable) {
        this.fileTable = fileTable;
    }

    public MyDictionary<Integer, Pair<String, BufferedReader>> getFileTable() {
        return this.fileTable;
    }

    public void setHeap(MyHeap<Integer> heap) {
        this.heap = heap;
    }

    public MyHeap<Integer> getHeap() {
        return this.heap;
    }

    public MyLockTable<Integer> getLockTable() {
        return this.lockTable;
    }

    public void setLockTable(MyLockTable<Integer> lockTable) {
        this.lockTable = lockTable;
    }

    public boolean isNotCompleted() {
        return !this.exeStack.isEmpty();
    }

    public ProgramState executeProgram()
    {

        if(this.exeStack.isEmpty())
            throw new ADTException("(In ProgramState) The stack is empty!");

        IStatement crtStm = this.exeStack.pop();
        try
        {
            return crtStm.execute(this);
        }
        catch(ADTException e) {
            System.out.println(e.toString());
            return null;
        }
    }

    public int getId() {
        return this.id;
    }

    public String toString(){
        String s = "";
        s += "in exeStack: \n";
        s += this.exeStack.toString();
        s += "\n in symTable:";
        s += this.symTable.toString();
        s += "\n in output:";
        s += this.output.toString();
        s += "\n in file table:";
        s += this.fileTable.toString();
        s += "\n in heap:";
        s += this.heap.toString();
        s += "\n in lock table:";
        s += this.lockTable.toString();
        return s;
    }

}
