package hr.algebra.model;

import javafx.fxml.Initializable;

public class ControllerProvider {
    private static Initializable controller;
    private final static ControllerProvider INSTANCE = new ControllerProvider();

    private ControllerProvider(){}

    public static ControllerProvider getInstance(){
        return INSTANCE;
    }

    public void setController(Initializable controller){
        ControllerProvider.controller = controller;
    }

    public Initializable getController(){
        return controller;
    }
}
