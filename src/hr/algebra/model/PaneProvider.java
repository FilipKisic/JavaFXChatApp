package hr.algebra.model;

import javafx.scene.layout.BorderPane;

public class PaneProvider {
    private static BorderPane pane;
    private final static PaneProvider INSTANCE = new PaneProvider();

    private PaneProvider(){}

    public static PaneProvider getInstance(){
        return INSTANCE;
    }

    public void setPane(BorderPane pane){
        PaneProvider.pane = pane;
    }

    public BorderPane getPane(){
        return pane;
    }

}
