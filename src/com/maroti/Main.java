package com.maroti;

import com.maroti.model.ReminderData;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("mainController.fxml"));
        primaryStage.setTitle("Reminder");
        primaryStage.setScene(new Scene(root, 900, 600));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void init() throws Exception {
        ReminderData.getInstance().loadData();
    }

    @Override
    public void stop() throws Exception {
        ReminderData.getInstance().storeData();
    }
}
