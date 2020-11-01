package hr.algebra.dal;

import javafx.scene.layout.BorderPane;

public class PaneHolderSingleton {
    private static BorderPane pane;
    private final static PaneHolderSingleton INSTANCE = new PaneHolderSingleton();

    private PaneHolderSingleton(){}

    public static PaneHolderSingleton getInstance(){
        return INSTANCE;
    }

    public void setPane(BorderPane pane){
        PaneHolderSingleton.pane = pane;
    }

    public BorderPane getPane(){
        return pane;
    }

}
