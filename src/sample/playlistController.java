package sample;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

public class playlistController extends Application{

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

        removeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent event) {
                final int selectedIdx = listView.getSelectionModel().getSelectedIndex();
                if (selectedIdx != -1) {
                    itemToRemove = listView.getSelectionModel().getSelectedItem();

                    final int newSelectedIdx =
                            (selectedIdx == listView.getItems().size() - 1)
                                    ? selectedIdx - 1
                                    : selectedIdx;

                    listView.getItems().remove(selectedIdx);
                    listView.getSelectionModel().select(newSelectedIdx);
                }
            }
        });
        openFileButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();

                //Open directory from existing directory
                if( fileList != null ){
                    if( !fileList.isEmpty() ){
                        File existDirectory = fileList.get(0).getParentFile();
                        fileChooser.setInitialDirectory(existDirectory);
                    }
                }
                //Set extension filter
                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Media Files (*.mp3)", "*.mp3", "*m4a");
                fileChooser.getExtensionFilters().add(extFilter);

                fileList = fileChooser.showOpenMultipleDialog(primaryStage);

                for(int i=0; i<fileList.size(); i++) {
                    list.add(fileList.get(i).getPath());
                }
                listView.setItems(list);
            }
        });

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
