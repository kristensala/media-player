package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

public class Recent extends playerController{

    File file;
    ListView<String> listView;
    ObservableList<String> list;
    List<File> fileList;
    String itemToRemove;

    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(final Stage primaryStage) {
        primaryStage.setTitle("Playlist");

        listView = new ListView<>();
        list = FXCollections.observableArrayList();

        final Label labelFile = new Label();

        Button openFileButton = new Button();
        Button removeButton = new Button();
        removeButton.setText("Remove selected file");
        openFileButton.setText("Open File");



        VBox vBox = new VBox();
        ToolBar toolBar = new ToolBar(openFileButton, removeButton);
        vBox.getChildren().addAll(labelFile, toolBar);

        vBox.getChildren().add(listView);

        StackPane root = new StackPane();
        root.getChildren().add(vBox);
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
    }
}
