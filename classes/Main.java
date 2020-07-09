package classes;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Klasa glowna aplikacji Sudoku
 */

public class Main extends Application {
    /**
     * Nadpisuje metode start klasy Application. Wywoluje obsluge stage w klasie View
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        View sudokuView = new View();
        sudokuView.handleStage(primaryStage);
    }

    /**
     * Jedynym zadaniem metody main jest launch aplikacji
     * @param args argumenty wywolania programu
     */
    public static void main(String[] args) {
        launch(args);
    }
}
