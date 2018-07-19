
package sample;

import javafx.scene.input.ScrollEvent;
import sample.Main;
import javafx.beans.Observable;
import java.util.ResourceBundle;
import javafx.beans.InvalidationListener;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.ChangeListener;
import javafx.scene.input.MouseEvent;
import java.io.File;
import java.net.URL;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.scene.control.Slider;
import javafx.util.Duration;

public class Controller implements Initializable {

    public String filePath;
    @FXML
    public Slider timeSlider;
    @FXML
    public MenuItem openMI;
    @FXML
    public MenuItem exitMI;
    @FXML
    public MediaPlayer mp;
    @FXML
    public MediaView mv;
    @FXML
    Label timeStamp =new Label();
    @FXML
    Label totalDuration= new Label();
    @FXML
    Slider volSlider= new Slider();

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {}


    @FXML
    protected void openAction(){
        handleOpenClick();
    }

    @FXML
    void exitAction(){
        Platform.exit();
    }

    @FXML
    private void handleOpenClick() {
        FileChooser fc= new FileChooser();
        fc.getExtensionFilters().addAll(new
                FileChooser.ExtensionFilter("Click to select extensions.", "*"),new
                FileChooser.ExtensionFilter("Mp3 Extension files", "*.mp3"),new
                FileChooser.ExtensionFilter("Mp4 Extension files", "*.mp4"),new
                FileChooser.ExtensionFilter("M4a Extension files", "*.m4a"));
        File file= fc.showOpenDialog(null);
        filePath= file.toURI().toString();

        if(filePath!= null) {
            System.out.println("FilePath is- "+filePath);
            Media media= new Media(filePath);
            mp= new MediaPlayer(media);
            mv.setMediaPlayer(mp);
            DoubleProperty width= mv.fitWidthProperty();
            DoubleProperty height= mv.fitHeightProperty();
            width.bind(Bindings.selectDouble(mv.sceneProperty(), "width"));
            height.bind(Bindings.selectDouble(mv.sceneProperty(), "height"));
            mp.play();
            mp.setOnReady(()-> {
                volSlider.setValue(mp.getVolume()* 100);
                volSlider.valueProperty().addListener(new InvalidationListener() {
                    @Override
                    public void invalidated(Observable observable) {
                        mp.setVolume(volSlider.getValue()/ 100);
                    }
                });
                volSlider.setOnScroll(new EventHandler<ScrollEvent>() {
                    @Override
                    public void handle(ScrollEvent event) {
                        volSlider.setTranslateX(volSlider.getTranslateX()+ event.getDeltaX());
                    }
                });
                timeSlider.setMax(mp.getTotalDuration().toSeconds());
                timeStamp.textProperty().bind(Bindings.createStringBinding(()->{
                    Duration time= mp.getCurrentTime();
                    int secondsPassed = (int) time.toSeconds() % 3600;
                    while (secondsPassed > 59) {
                        secondsPassed -= 60;
                    }
                    return String.format("%d:%02d:%02d", (int) time.toHours(), (int) time.toMinutes()% 60, secondsPassed);
                },mp.currentTimeProperty()));

                totalDuration.textProperty().bind(Bindings.createStringBinding(()-> {
                    Duration totalTime= mp.getTotalDuration();
                    int secondsPassed= (int) totalTime.toSeconds() % 3600;
                    while(secondsPassed> 59){
                        secondsPassed-= 60;
                    }
                    return  String.format("%d:%02d:%02d",(int) totalTime.toHours(), (int) totalTime.toMinutes()% 60, secondsPassed);
                },mp.totalDurationProperty()));

            });
            mp.currentTimeProperty().addListener(new ChangeListener<Duration>(){
                @Override
                public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue){
                    timeSlider.setValue(newValue.toSeconds());
                }
            });
            timeSlider.setOnMouseClicked(new EventHandler<MouseEvent>(){
                @Override
                public void handle(MouseEvent event) {
                    mp.seek(Duration.seconds(timeSlider.getValue()));
                }});
        }
    }
    @FXML public void playButton(ActionEvent event) {mp.play(); mp.setRate(1); }
    @FXML public void pauseButton(ActionEvent event){ mp.pause();}
    @FXML public void stopButton(ActionEvent event) {mp.stop();}
    @FXML public void fasterButton(ActionEvent event) {mp.setRate(2);}
    @FXML public void slowerButton(ActionEvent event) {mp.setRate(0.5);}
}
