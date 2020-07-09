package classes;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Zawiera wczytywanie sceny za pomoca FXMLLoadera
 */
public class View {
    /**
     * Wczytuje scene do stage za pomoca FXMLLoadera i ustawia odpowiednie wlasciwosci stage
     */
    void handleStage(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../Scene.fxml"));
        primaryStage.setTitle("Sudoku");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
