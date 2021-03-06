package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        BorderPane root = FXMLLoader.load(getClass().getResource("player.fxml"));
        primaryStage.setTitle("Media Player");
        primaryStage.setScene(new Scene(root));
        primaryStage.setMinHeight(400);
        primaryStage.setMinWidth(450);
        primaryStage.show();

        // got the method from playerController class
        playerController playerController = new playerController();
        playerController.doubleClickFullScreen(root, primaryStage);

    }
    public static void main(String[] args) {
        launch(args);
    }
}
