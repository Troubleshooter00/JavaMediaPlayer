package sample;

import javafx.event.ActionEvent;
import javafx.scene.media.MediaPlayer;
import sample.Controller;
import javafx.application.Application;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.event.EventHandler;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage){

        try {
            primaryStage.setTitle("Media_Player_1.01.00_ALPHA");
            Parent root= FXMLLoader.load(getClass().getResource("sample.fxml"));
            Scene scene= new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.getIcons().add(new Image(/*your icon path here*/));
            primaryStage.show();
            primaryStage.setFullScreenExitHint("");
            scene.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if(event.getButton().equals(MouseButton.PRIMARY )){
                        if(event.getClickCount()== 2 && primaryStage.isFullScreen()){
                            primaryStage.setFullScreen(false);
                        }else if(event.getClickCount()== 2){
                            primaryStage.setFullScreen(true);
                        }
                    }
                }
            });
            scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent event) {
                    if(event.getCode()== KeyCode.F && !primaryStage.isFullScreen()){
                        primaryStage.setFullScreen(true);
                    }else{
                        primaryStage.setFullScreen(false);
                    }
                }
            });

        }catch(IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void main(String[] args) {
        launch(args);
    }
}
