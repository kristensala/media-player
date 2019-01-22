package sample;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;

public class playerController extends playlistController {

    Media media;
    MediaPlayer mediaPlayer;
    Duration duration;
    File mediaFile;
    String fileName;
    FileChooser chooser;

    // player
    @FXML
    private Button playButton;
    @FXML
    private Button pauseButton;
    @FXML
    private ToolBar toolBar;
    @FXML
    private MenuBar menuBar;
    @FXML
    private Menu file;
    @FXML
    private MenuItem openFile;
    @FXML
    private Slider timeSlider;
    @FXML
    private MediaView mediaView;
    @FXML
    private BorderPane borderPane;
    @FXML
    private VBox vBoxTop;
    @FXML
    private Label mediaName;
    @FXML
    private Label playTime;
    @FXML
    private VBox vBox;
    @FXML
    private RadioMenuItem repeat;
        @FXML
    private Button playlistButton;
    @FXML
    private Slider volumeSlider;

    @FXML
    public void setOpenFile() {
        chooser = new FileChooser();
        if (mediaFile != null) {
            File exist = mediaFile.getParentFile();
            chooser.setInitialDirectory(exist);
        }
        FileChooser.ExtensionFilter extensionFilter =
                new FileChooser.ExtensionFilter("Media Files", "*.mp3", "*.mp4", "*.m4a", "*.wav", "*aiff", "*aac");
        chooser.getExtensionFilters().add(extensionFilter);

        mediaFile = chooser.showOpenDialog(new Stage());

        if (mediaFile != null) {
            if (media != null) {
                mediaPlayer.stop();
            }
            String path = mediaFile.getAbsolutePath();
            path.replace("\\", "/");
            media = new Media((new File(path).toURI().toString()));
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setAutoPlay(true);
            mediaView.setMediaPlayer(mediaPlayer);

            getFileName();
            setTimeSlider();
            setVolumeSlider();

            if (getFileExtension().equals("mp4")) {
                setMediaView();
            }
        } else {
            System.out.println("No file was chosen");
        }
    }

    @FXML
    public void setPlaylistButton() {
        playlistController playlistController = new playlistController();
        playlistController.start(new Stage());
    }

    @FXML
    public void setPlayButton() {
        if (media != null) {
            mediaPlayer.play();
        }
    }

    @FXML
    public void setPauseButton() {
        if (media != null) {
            mediaPlayer.pause();
        }
    }

    @FXML
    public void setRepeat() {
        if (media != null) {
            if (repeat.isSelected()) {
                mediaPlayer.setCycleCount(mediaPlayer.INDEFINITE);
            } else {
                mediaPlayer.setCycleCount(1);
            }
        }
    }

    public void setTimeSlider() {
        mediaPlayer.setOnReady(new Runnable() {
            @Override
            public void run() {
                duration = mediaPlayer.getMedia().getDuration();
                updateTimeSliderValues();
            }
        });
        mediaPlayer.currentTimeProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                updateTimeSliderValues();
            }
        });
        timeSlider.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                if (timeSlider.isValueChanging()) {
                    if (duration != null) {
                        mediaPlayer.seek(duration.multiply(timeSlider.getValue() / 100.0));
                    }
                    updateTimeSliderValues();
                }
            }
        });
    }
    public void setVolumeSlider(){
        volumeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (volumeSlider.isValueChanging()) {
                    mediaPlayer.setVolume(volumeSlider.getValue() / 100.0);
                }
            }
        });
    }

    public void setMediaView() {
        borderPane.heightProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                mediaView.fitHeightProperty().bind(borderPane.getScene().heightProperty().subtract(150));
            }
        });
        borderPane.widthProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                mediaView.fitWidthProperty().bind(borderPane.getScene().widthProperty());
            }
        });
    }

    public void updateTimeSliderValues() {
        if (timeSlider != null && playTime != null && volumeSlider != null) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    Duration currentTime = mediaPlayer.getCurrentTime();
                    playTime.setText(formatTime(currentTime, duration));
                    timeSlider.setDisable(duration.isUnknown());
                    if (!timeSlider.isDisabled() && duration.greaterThan(Duration.ZERO) && !timeSlider.isValueChanging()) {
                        timeSlider.setValue(currentTime.divide(duration.toMillis()).toMillis() * 100.0);
                    }
                    if (!volumeSlider.isValueChanging()) {
                        volumeSlider.setValue((int) Math.round(mediaPlayer.getVolume() * 100));
                    }
                }
            });
        }
    }

    public static String formatTime(Duration elapsed, Duration duration) {
        int intElapsed = (int) Math.floor(elapsed.toSeconds());
        int elapsedHours = intElapsed / (60 * 60);
        if (elapsedHours > 0) {
            intElapsed -= elapsedHours * 60 * 60;
        }
        int elapsedMinutes = intElapsed / 60;
        int elapsedSeconds = intElapsed - elapsedHours * 60 * 60 - elapsedMinutes * 60;

        if (duration.greaterThan(Duration.ZERO)) {
            int intDuration = (int) Math.floor(duration.toSeconds());
            int durationHours = intDuration / (60 * 60);
            if (durationHours > 0) {
                intDuration -= durationHours * 60 * 60;
            }
            int durationMinutes = intDuration / 60;
            int durationSeconds = intDuration - durationHours * 60 * 60 - durationMinutes * 60;

            if (durationHours > 0) {
                return String.format("%d:%02d:%02d/%d:%02d:%02d",
                        elapsedHours, elapsedMinutes, elapsedSeconds,
                        durationHours, durationMinutes, durationSeconds);
            } else {
                return String.format("%02d:%02d/%02d:%02d",
                        elapsedMinutes, elapsedSeconds,
                        durationMinutes, durationSeconds);
            }
        } else {
            if (elapsedHours > 0) {
                return String.format("%d:%02d:%02d",
                        elapsedHours, elapsedMinutes, elapsedSeconds);
            } else {
                return String.format("%02d:%02d",
                        elapsedMinutes, elapsedSeconds);
            }
        }
    }

    public void doubleClickFullScreen(BorderPane root, Stage primaryStage) {
        root.setOnMousePressed(e -> {
            if (e.getClickCount() == 2) {
                primaryStage.setFullScreen(!primaryStage.isFullScreen());
            }
        });
    }

    public void getFileName() {
        fileName = mediaFile.getName();
        mediaName.setText(fileName);

        // Writes the file name into txt file
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream("filename.txt"), "utf-8"))) {
            writer.write(fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileChooser myFile = new FileChooser();
        myFile.setInitialFileName(fileName);

    }

    public String getFileExtension() {
        fileName = mediaFile.getName();
        String fileExtension = fileName.substring(fileName.indexOf(".") + 1, mediaFile.getName().length());
        return fileExtension;
    }
}