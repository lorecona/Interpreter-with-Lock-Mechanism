import Controller.Controller;
import Model.Expression.*;
import Model.ProgramState;
import Model.Statement.*;
import Model.Statement.NewStm;
import Model.Statement.WriteHeapStm;
import Model.Utils.MyHeap;
import Model.Utils.MyList;
import Repository.IRepo;
import Repository.Repo;
import javafx.application.Application;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyMapProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;
import javafx.util.Pair;


public class GUITEMP extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    private TableView heapTableView;
    private ListView<Text> outputListView;
    private TableView fileTableView;
    private TableView symTableView;
    private ListView<Text> exeStackListView;
    private ListView<Text> programStatesView;
    private TableView lockTableView;
    private Text noOfPrgStates;

    private Controller ctrl1, ctrl2, ctrl3, ctrl4, ctrl5, ctrl6, ctrl7, ctrl8, ctrl9, ctrl10;
    private Controller currentController;
    private int currIndex[] = {0};
    private int programStateIndex[] = {0};


    Button oneStepBtn;

    public void updateSymTableView(int index) {
        this.symTableView.getItems().clear();

        ObservableList<Map.Entry<String, Integer>> symTableViewItems = FXCollections.observableArrayList(this.currentController.getRepo().getProgramStates().get(index).getSymTable().getMap().entrySet());
        this.symTableView.getItems().addAll(symTableViewItems);
    }


    public void updateExeStackListView(int index) {
        this.exeStackListView.getItems().clear();
        ObservableList<Text> exeStackListViewItems = FXCollections.observableArrayList();

        for (IStatement elem : this.currentController.getRepo().getProgramStates().get(index).getExeStack().getStack()) {
            Text text = new Text(elem.toString());
            text.wrappingWidthProperty().bind(this.exeStackListView.widthProperty());
            exeStackListViewItems.add(text);
        }

        this.exeStackListView.getItems().addAll(exeStackListViewItems);
    }

    private void updateOutListView() {
        this.outputListView.getItems().clear();
        this.currentController.getRepo().getProgramStates().forEach(
                (ps) -> {
                    Text text = new Text(ps.getOutput().toString());
                    text.wrappingWidthProperty().bind(outputListView.widthProperty());
                    this.outputListView.getItems().add(text);
                }
        );
    }

    private void updateProgramStates() {
        this.programStatesView.getItems().clear();
        this.currentController.getRepo().getProgramStates().forEach(
                (ps) -> {
                    Text text = new Text(String.valueOf(ps.getId()));
                    text.wrappingWidthProperty().bind(outputListView.widthProperty());
                    this.programStatesView.getItems().add(text);
                }
        );

    }

    private void updateHeapTableView() {
        this.heapTableView.getItems().clear();
        this.currentController.getRepo().getProgramStates().forEach(
                (ps) -> {
                    ObservableList<Map.Entry<Integer, Integer>> heapTableViewItems = FXCollections.observableArrayList(ps.getHeap().getMap().entrySet());
                    this.heapTableView.getItems().addAll(heapTableViewItems);
                }
        );
    }

    private void updateLockTableView() {
        this.lockTableView.getItems().clear();
        this.currentController.getRepo().getProgramStates().forEach(
                (ps) -> {
                    ObservableList<Map.Entry<Integer,Integer>> lockTableViewItems = FXCollections.observableArrayList(ps.getLockTable().getTable().entrySet());
                    this.lockTableView.getItems().addAll(lockTableViewItems);
                }
        );
    }

    private void updateFileTableView() {
        this.fileTableView.getItems().clear();
        this.currentController.getRepo().getProgramStates().forEach(
                (ps) -> {
                    ObservableList<Map.Entry<Integer, Pair<String, BufferedReader>>> fileTableViewItems = FXCollections.observableArrayList(ps.getFileTable().getMap().entrySet());
                    this.fileTableView.getItems().addAll(fileTableViewItems);
                }
        );
    }


    public Controller getControllerForIndex() {
        switch (this.currIndex[0]) {
            case 0:
                return ctrl1;

            case 1:
                return ctrl2;

            case 2:
                return ctrl3;

            case 3:
                return ctrl4;

            case 4:
                return ctrl5;

            case 5:
                return ctrl6;

            case 6:
                return ctrl7;

            case 7:
                return ctrl8;

            case 8:
                return ctrl9;

            case 9:
                return ctrl10;

            default:
                return ctrl1;
        }
    }

    static MyList<IStatement> makeStm()
    {
        //1. v=2; print(v)
        IStatement statement1 = new CompoundStm(new AssignmentStm("v",new ConstExp(2)), new PrintStm(new VarExp("v")));

        //2. a=2+3*5; b=a+1; print(b)
        IStatement statement2 = new CompoundStm(new AssignmentStm("a", new ArithExp(new ConstExp(2),'+',new
                ArithExp(new ConstExp(3),'*' ,new ConstExp(5)))),
                new CompoundStm(new AssignmentStm("b",new ArithExp(new VarExp("a"),'+', new
                        ConstExp(1))), new PrintStm(new VarExp("b"))));

        //3. a=2-2; (if a then v=2 else v=3); print(v)
        IStatement statement3 = new CompoundStm(new AssignmentStm("a", new ArithExp( new ConstExp(2),'-', new
                ConstExp(1))), new CompoundStm(new IfStm(new VarExp("a"), new AssignmentStm("v", new ConstExp(2)), new
                AssignmentStm("v", new ConstExp(3))), new PrintStm(new VarExp("v"))));

        IStatement statement4 = new CompoundStm(new OpenRFileStm("var_f", "openfile.txt"),
                new CompoundStm(new ReadFileStm(new VarExp("var_f"), "var_c"),
                        new CompoundStm(new PrintStm(new VarExp("var_c")),
                                new CompoundStm(new IfStm(new VarExp("var_c"),
                                        new CompoundStm(new ReadFileStm(new VarExp("var_f"), "var_c"),
                                                new PrintStm(new VarExp("var_c"))),
                                        new PrintStm(new ConstExp(0))),
                                        new CloseRFileStm(new VarExp("var_f"))))));



        IStatement statement5 = new CompoundStm(new OpenRFileStm("var_f", "openfile.txt"),
                new CompoundStm( new ReadFileStm(new ArithExp(new VarExp("var_f"), '+', new ConstExp(0)), "var_c"),
                        new CompoundStm(new PrintStm(new VarExp("var_c")),
                                new CompoundStm( new IfStm(new VarExp("var_c"),
                                        new CompoundStm(new ReadFileStm(new VarExp("var_f"), "var_c"),
                                                new PrintStm( new VarExp("var_c"))),
                                        new PrintStm(new ConstExp(0))),
                                        new CloseRFileStm(new VarExp("var_f"))))));

        IStatement statement6 = new CompoundStm(new AssignmentStm("v", new ConstExp(10)),
                new CompoundStm(new NewStm("v", new ConstExp(20)),
                        new CompoundStm( new NewStm("a", new ConstExp(22)),
                                new CompoundStm(new WriteHeapStm("a", new ConstExp(30)),
                                        new CompoundStm(new PrintStm(new VarExp("a")),
                                                new CompoundStm(new PrintStm(new ReadHeapExp("a")),
                                                        new AssignmentStm("a", new ConstExp(0))))))));

        IStatement statement7 = new CompoundStm(new AssignmentStm("v", new ArithExp(new ConstExp(10), '+',
                new BooleanExp(new ConstExp(2), "<", new ConstExp(6)))),
                new PrintStm(new VarExp("v")));


        IStatement statement8 = new CompoundStm(new AssignmentStm("v", new ConstExp(6)),
                new CompoundStm(new WhileStm(new ArithExp(new VarExp("v"), '-', new ConstExp(4)),
                        new CompoundStm(new PrintStm(new VarExp("v")),
                                new AssignmentStm("v", new ArithExp(new VarExp("v"), '-', new ConstExp(1))))),
                        new PrintStm(new VarExp("v"))));

        IStatement statement9 = new CompoundStm(new CompoundStm(new AssignmentStm("v", new ConstExp(10)),
                new NewStm("a", new ConstExp(22))),
                new CompoundStm(new ForkStm(new CompoundStm(
                        new WriteHeapStm("a", new ConstExp(30)),
                        new CompoundStm(new AssignmentStm("v", new ConstExp(32)),
                                new CompoundStm(new PrintStm(new VarExp("v")),
                                        new PrintStm(new ReadHeapExp("a")))))),
                        new CompoundStm(new PrintStm(new VarExp("v")),
                                new PrintStm(new ReadHeapExp("a")))));

        IStatement statement10 = new CompoundStm(
                new NewStm("v1", new ConstExp(20)),
                new CompoundStm(
                        new NewStm("v2", new ConstExp(30)),
                        new CompoundStm(
                                new NewLock("x"),
                                new CompoundStm(
                                        new ForkStm(new CompoundStm(
                                                new ForkStm(new CompoundStm(
                                                        new Lock("x"),
                                                        new CompoundStm(
                                                                new WriteHeapStm("v1", new ArithExp(new ReadHeapExp("v1"),'-', new ConstExp(1))),
                                                                new Unlock("x")))),
                                                new CompoundStm(
                                                        new Lock("x"),
                                                        new CompoundStm(
                                                                new WriteHeapStm("v1", new ArithExp(new ReadHeapExp("v1"), '+', new ConstExp(1))),
                                                                new Unlock("x"))))),
                                        new CompoundStm(
                                                new NewLock("q"),
                                                new CompoundStm(
                                                        new ForkStm(new CompoundStm(
                                                                new ForkStm(
                                                                        new CompoundStm(
                                                                                new Lock("q"),
                                                                                new CompoundStm(
                                                                                        new WriteHeapStm("v2", new ArithExp(new ReadHeapExp("v2"), '+', new ConstExp(5))),
                                                                                        new Unlock("q")))),
                                                                new CompoundStm(
                                                                        new AssignmentStm("m",new ConstExp(100)),
                                                                        new CompoundStm(
                                                                                new Lock("q"),
                                                                                new CompoundStm(
                                                                                        new WriteHeapStm("v2", new ArithExp(new ReadHeapExp("v2"), '+', new ConstExp(1))),
                                                                                        new Unlock("q")))))),
                                                        new CompoundStm(
                                                                new AssignmentStm("z", new ConstExp(200)),
                                                                new CompoundStm(
                                                                        new AssignmentStm("z", new ConstExp(300)),
                                                                        new CompoundStm(
                                                                                new AssignmentStm("z", new ConstExp(400)),
                                                                                new CompoundStm(
                                                                                        new Lock("x"),
                                                                                        new CompoundStm(
                                                                                                new PrintStm(new ReadHeapExp("v1")),
                                                                                                new CompoundStm(
                                                                                                        new Unlock("x"),
                                                                                                        new CompoundStm(
                                                                                                                new Lock("q"),
                                                                                                                new CompoundStm(
                                                                                                                        new PrintStm(new ReadHeapExp("v2")),
                                                                                                                        new Unlock("q")))))))))))))));


        MyList<IStatement> statements = new MyList<>();
        statements.add(statement1);
        statements.add(statement2);
        statements.add(statement3);
        statements.add(statement4);
        statements.add(statement5);
        statements.add(statement6);
        statements.add(statement7);
        statements.add(statement8);
        statements.add(statement9);
        statements.add(statement10);

        return statements;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        MyList<IStatement> statements = makeStm();

        IRepo repository1 = new Repo();
        ProgramState program1 = new ProgramState(statements.get(0));
        repository1.add(program1);
        this.ctrl1 = new Controller(repository1);

        IRepo repository2 = new Repo();
        ProgramState program2 = new ProgramState(statements.get(1));
        repository2.add(program2);
        this.ctrl2 = new Controller(repository2);

        IRepo repository3 = new Repo();
        ProgramState program3 = new ProgramState(statements.get(2));
        repository3.add(program3);
        this.ctrl3 = new Controller(repository3);

        IRepo repository4 = new Repo();
        ProgramState program4 = new ProgramState(statements.get(3));
        repository4.add(program4);
        this.ctrl4 = new Controller(repository4);

        IRepo repository5 = new Repo();
        ProgramState program5 = new ProgramState(statements.get(4));
        repository5.add(program5);
        this.ctrl5 = new Controller(repository5);

        IRepo repository6 = new Repo();
        ProgramState program6 = new ProgramState(statements.get(5));
        repository6.add(program6);
        this.ctrl6 = new Controller(repository6);

        IRepo repository7 = new Repo();
        ProgramState program7 = new ProgramState(statements.get(6));
        repository7.add(program7);
        this.ctrl7 = new Controller(repository7);

        IRepo repository8 = new Repo();
        ProgramState program8 = new ProgramState(statements.get(7));
        repository8.add(program8);
        this.ctrl8 = new Controller(repository8);

        IRepo repository9 = new Repo();
        ProgramState program9 = new ProgramState(statements.get(8));
        repository9.add(program9);
        this.ctrl9 = new Controller(repository9);

        IRepo repository10 = new Repo();
        ProgramState program10 = new ProgramState(statements.get(9));
        repository10.add(program10);
        this.ctrl10 = new Controller(repository10);


        this.currentController = ctrl1;


        primaryStage.setTitle("MAP Interpreter");
        VBox layout = new VBox();
        layout.setSpacing(10);

        //--------------------------------------------------------------------------------------
        this.noOfPrgStates = new Text("noOfPrgStates = " + String.valueOf(this.currentController.getRepo().getProgramStates().size()));
        layout.getChildren().add(noOfPrgStates);

        //--------------------------------------------------------------------------------------
        Text heapText = new Text("Heap:");
        layout.getChildren().add(heapText);

        this.heapTableView = new TableView();
        this.heapTableView.setEditable(false);
        this.heapTableView.setPrefSize(170, 120);
        this.heapTableView.setMaxWidth(160);

        TableColumn<Map.Entry<Integer, Integer>, Number> address = new TableColumn<>("Address");
        address.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getKey()));

        TableColumn<Map.Entry<Integer, Integer>, Number> value = new TableColumn<>("Value");
        value.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getValue()));

        this.heapTableView.getColumns().setAll(address, value);
        this.updateHeapTableView();

        layout.getChildren().add(this.heapTableView);

        //---------------------------------------------------------------------------------
        Text outputText = new Text("Output:");
        layout.getChildren().add(outputText);

        this.outputListView = new ListView<>();
        this.outputListView.setEditable(false);
        this.outputListView.setPrefSize(160, 120);
        this.outputListView.setMaxWidth(160);

        this.updateOutListView();

        layout.getChildren().add(this.outputListView);

        //--------------------------------------------------------------------------
        Text fileTableText = new Text("File Table:");
        layout.getChildren().add(fileTableText);

        this.fileTableView = new TableView();
        this.fileTableView.setEditable(false);
        this.fileTableView.setPrefSize(160, 120);
        this.fileTableView.setMaxWidth(160);

        TableColumn<Map.Entry<Integer, Integer>, Number> identifier = new TableColumn<>("Identifier");
        identifier.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getKey()));

        TableColumn<Map.Entry<String, Pair<String, BufferedReader>>, String> fileName = new TableColumn<>("File  Name");
        fileName.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getValue().getKey()));

        this.fileTableView.getColumns().setAll(identifier, fileName);
        this.updateFileTableView();

        layout.getChildren().add(this.fileTableView);

        //------------------------------------------------------------------------------------7
        Text programStatesText = new Text("Program States");
        layout.getChildren().add(programStatesText);

        this.programStatesView = new ListView<>();
        this.programStatesView.setEditable(false);
        this.programStatesView.setPrefSize(150, 100);
        this.programStatesView.setMaxWidth(150);

        this.updateProgramStates();

        programStatesView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        programStatesView.setOnMouseClicked((event) -> {
            int selectedIndex = programStatesView.getSelectionModel().getSelectedIndex();
            if (selectedIndex != -1)
                programStateIndex[0] = selectedIndex;

            this.updateSymTableView(programStateIndex[0]);
            this.updateExeStackListView(programStateIndex[0]);

            //layout.getChildren().add(this.exeStackListView);
            //layout.getChildren().add(this.symTableView);

        });

        layout.getChildren().add(this.programStatesView);
        //-------------------------------------------------------------------------------------
        VBox layout2 = new VBox();
        layout2.setSpacing(10);

        Text symTableText = new Text("Symbol Table:");
        layout2.getChildren().add(symTableText);

        this.symTableView = new TableView();
        this.symTableView.setPrefSize(160, 120);
        this.symTableView.setMaxWidth(160);
        this.symTableView.setEditable(false);


        TableColumn<Map.Entry<String, Integer>, String> varNameCol = new TableColumn<>("Variable");
        varNameCol.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getKey()));

        TableColumn<Map.Entry<String, Integer>, Integer> varValueCol = new TableColumn<>("Value");
        varValueCol.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getValue()));

        this.symTableView.getColumns().setAll(varNameCol, varValueCol);
        layout2.getChildren().add(symTableView);

        //------------------------------------------------------------------------------------
        Text exeStackText = new Text("Execution Stack");
        layout2.getChildren().add(exeStackText);

        this.exeStackListView = new ListView<>();
        this.exeStackListView.setPrefSize(160, 120);
        this.exeStackListView.setMaxWidth(160);

        layout2.getChildren().add(exeStackListView);

        //-------------------------------------------------------------------------------------

        Text lockTableText = new Text("Lock Table");
        layout2.getChildren().add(lockTableText);

        this.lockTableView = new TableView();
        this.lockTableView.setPrefSize(160, 120);
        this.lockTableView.setMaxWidth(160);

        TableColumn<Map.Entry<Integer, Integer>, Number> indexCol = new TableColumn<>("Index");
        indexCol.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getKey()));

        TableColumn<Map.Entry<Integer, Integer>, Number> lockvalueCol = new TableColumn<>("Value");
        lockvalueCol.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getValue()));

        this.lockTableView.getColumns().setAll(indexCol, lockvalueCol);
        this.updateLockTableView();
        layout2.getChildren().add(lockTableView);


        //-------------------------------------------------------------------------------------

        this.oneStepBtn = new Button("Execute one step");

        this.oneStepBtn.setOnAction(event -> {
            try {

                this.currentController.oneStep2();

                this.updateProgramStates();
                this.updateHeapTableView();
                this.updateOutListView();
                this.updateFileTableView();
                this.updateSymTableView(this.programStateIndex[0]);
                this.updateExeStackListView(this.programStateIndex[0]);
                this.updateLockTableView();


            } catch (Exception e) {
                e.toString();
            }
        });
        //trimiti cv pe mircea.suceveanu@siemens.com

        layout2.getChildren().add(oneStepBtn);


        ///////////////////////////////////////////////////////////////////////////////////////////
        ///////////////////////////////////////////////////////////////////////////////////////////


        ListView<Text> listView = new ListView<>();
        listView.setPrefWidth(300);

        Text t1 = new Text(statements.get(0).toString());
        t1.wrappingWidthProperty().bind(listView.widthProperty());

        Text t2 = new Text(statements.get(1).toString());
        t2.wrappingWidthProperty().bind(listView.widthProperty());

        Text t3 = new Text(statements.get(2).toString());
        t3.wrappingWidthProperty().bind(listView.widthProperty());

        Text t4 = new Text(statements.get(3).toString());
        t4.wrappingWidthProperty().bind(listView.widthProperty());

        Text t5 = new Text(statements.get(4).toString());
        t5.wrappingWidthProperty().bind(listView.widthProperty());

        Text t6 = new Text(statements.get(5).toString());
        t6.wrappingWidthProperty().bind(listView.widthProperty());

        Text t7 = new Text(statements.get(6).toString());
        t7.wrappingWidthProperty().bind(listView.widthProperty());

        Text t8 = new Text(statements.get(7).toString());
        t8.wrappingWidthProperty().bind(listView.widthProperty());

        Text t9 = new Text(statements.get(8).toString());
        t9.wrappingWidthProperty().bind(listView.widthProperty());

        Text t10 = new Text(statements.get(9).toString());
        t10.wrappingWidthProperty().bind(listView.widthProperty());

        listView.getItems().addAll(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10);
        listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        listView.setBackground(Background.EMPTY);
        listView.setStyle("-fx-control-inner-background: rgba(211, 238, 252);");

        listView.setOnMouseClicked((event) -> {

            int selectedIndex = listView.getSelectionModel().getSelectedIndex();
            if (selectedIndex != -1)
                currIndex[0] = selectedIndex;


            this.noOfPrgStates.setText("noOfPrgStates = " + String.valueOf(this.getControllerForIndex().getRepo().getProgramStates().size()));
            this.currentController = this.getControllerForIndex();


            this.updateHeapTableView();
            this.updateOutListView();
            this.updateFileTableView();
            this.updateSymTableView(this.programStateIndex[0]);
            this.updateExeStackListView(this.programStateIndex[0]);
            this.updateLockTableView();
        });


        HBox layout3 = new HBox();
        BorderPane layout4 = new BorderPane();
        Insets insets = new Insets(10);
        String style = "-fx-background-color: rgba(188, 176, 201);";

        layout3.getChildren().add(listView);

        layout4.setBackground(Background.EMPTY);
        layout4.setStyle(style);
        layout4.setLeft(layout);
        layout4.setCenter(layout2);
        layout4.setRight(layout3);
        layout4.setMargin(layout2, insets);
        layout4.setMargin(layout3, insets);

        Scene scene = new Scene(layout4, 700, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

    }
}
