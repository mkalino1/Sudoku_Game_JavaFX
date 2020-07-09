package classes;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

import java.util.Collections;

/**
 * Zawiera metody obslugujace zdarzenia generowane z widoku
 */
public class Controller {
    @FXML Pane boardPane;
    @FXML Menu resetMenu;
    @FXML Menu hintsMenu;
    @FXML CheckMenuItem colorValidationItem;
    @FXML Pane gameEndAlert;
    /**
     * Kontroler zawiera obiekt klasy Model
     */
    private Model gameModel = new Model();

    /**
     * Metoda odpowiedzialna za zatwierdzenie lub odrzucenie proby wpisania cyfry
     * @param actionEvent zdarzenie wpisania znakow do sudoku - pewnego pola textfield
     */
    public void handleTextChange(ActionEvent actionEvent) {
        TextField caller = (TextField) actionEvent.getSource();
        int row = Character.getNumericValue(caller.getId().charAt(1));
        int col = Character.getNumericValue(caller.getId().charAt(2));
        boolean isInputCorrect = false;
        for (int digit = 1; digit < 10; digit++){
            if (caller.getText().equals(Integer.toString(digit)))
                isInputCorrect = true;
        }
        if (!isInputCorrect){
            caller.setText("");
            return;
        };
        int num = Integer.parseInt(caller.getText());
        if(gameModel.isCorrect(row, col, num)) {
            gameModel.getUserCorrectAnswers().add(caller);
            gameModel.getBlanks().remove(caller);
            if (colorValidationItem.isSelected())
                caller.setStyle("-fx-background-color: lightgreen;");
            caller.setEditable(false);
            if (gameModel.getBlanks().size() == 0)
                gameEndAlert.setVisible(true);
        }
        else{
            caller.setText("");
        }
    }

    /**
     * Wywoluje generowanie planszy z gameModel oraz wywoluje swoja
     * pomocnicza funkcje initalizeBoard()
     */
    public void setupGeneratedBoard(){
        gameModel.generateBoard();
        initializeBoard();
        resetMenu.setDisable(false);
        hintsMenu.setDisable(false);
    }

    /**
     * Resetuje stan planszy na ekranie do stanu po wygenerowaniu planszy
     * korzystajac z metody initializeBoard()
     */
    public void resetToInitial(){
        initializeBoard();
    }

    /**
     * Inicjalizuje plansze na ekranie do stanu planszy startowej. Ustawia wlasnosci wszystkich textfieldow
     * na odpowiednie oraz resetuje odpowiednio liste poprawnych odpowiedzi i liste pustych pol
     */
    private void initializeBoard(){
        gameModel.getUserCorrectAnswers().clear();
        gameModel.getBlanks().clear();
        String name;
        TextField field;
        for(int j=0; j<9; j++)
            for(int i=0; i<9; i++) {
                name = "#f" + j + i;
                field = (TextField) boardPane.lookup(name);
                if (gameModel.getInitial()[j][i] != 0) {
                    field.setText(Integer.toString(gameModel.getInitial()[j][i]));
                    field.setStyle("-fx-background-color: lightgrey;");
                    field.setEditable(false);
                }
                else {
                    gameModel.getBlanks().add(field);
                    field.setText("");
                    field.setStyle("-fx-background-color: white;");
                    field.setEditable(true);
                }
            }
    }

    /**
     *Umozliwia poruszanie sie po polach sudoku za pomoca strzalek poprzez przekazywanie
     * focusa odpowiednimu polu - przyciski inne niz strzalki sa ignorowane
     * @param keyEvent zdarzenie wcisniecia przycisku gdy focus jest na polu sudoku
     */
    public void handleArrows(KeyEvent keyEvent) {
        String keyCode = keyEvent.getCode().toString();
        TextField source = (TextField) keyEvent.getSource();
        int row = Character.getNumericValue(source.getId().charAt(1));
        int col = Character.getNumericValue(source.getId().charAt(2));
        switch (keyCode) {
            case "LEFT":
                col = (col + 8) % 9;
                break;
            case "RIGHT":
                col = (col + 1) % 9;
                break;
            case "UP":
                row = (row + 8) % 9;
                break;
            case "DOWN":
                row = (row + 1) % 9;
                break;
            default:
                return;
        }
        String destinationID = "#f" + row + col;
        TextField destination = (TextField) boardPane.lookup(destinationID);
        destination.requestFocus();
    }

    /**
     * Obsluguje zmiane preferencji uzytkownika miedzy uzywaniem koloru do zaznaczenia
     * poprawnych odpowiedzi, a brakiem koloru.
     * @param actionEvent zdarzenie nacisniecia przycisku zmieny preferencji
     */
    public void changeColorValidation(ActionEvent actionEvent) {
        if (colorValidationItem.isSelected()) {
            for (TextField field : gameModel.getUserCorrectAnswers()) {
                field.setStyle("-fx-background-color: lightgreen;");
            }
        }
        else{
            for (TextField field : gameModel.getUserCorrectAnswers()) {
                field.setStyle("-fx-background-color: white;");
            }
        }
    }

    /**
     * Metoda pomocnicza rozwiazujca jedno losowe pole
     */
    private void doSingleHint(){
        TextField field = gameModel.getBlanks().get(0);
        int row = Character.getNumericValue(field.getId().charAt(1));
        int col = Character.getNumericValue(field.getId().charAt(2));
        gameModel.getUserCorrectAnswers().add(field);
        gameModel.getBlanks().remove(field);
        if (colorValidationItem.isSelected()) field.setStyle("-fx-background-color: lightgreen;");
        field.setText(Integer.toString(gameModel.getSolution()[row][col]));
        field.setEditable(false);
        if (gameModel.getBlanks().size() == 0) gameEndAlert.setVisible(true);
    }

    /**
     * Rozwiazuje jedno losowe pole korzystajac z doSingleHint
     * @param actionEvent zdarzenie wcisniecia odpowiedniego przycisku
     */
    public void singleHint(ActionEvent actionEvent) {
        Collections.shuffle(gameModel.getBlanks());
        if(gameModel.getBlanks().isEmpty())
            return;
        doSingleHint();
    }

    /**
     * Rozwiazuje trzy (maksymalnie) losowe pola korzystajac z doSingleHint
     * @param actionEvent zdarzenie wcisniecia odpowiedniego przycisku
     */
    public void tripleHint(ActionEvent actionEvent) {
        Collections.shuffle(gameModel.getBlanks());
        for (int i = 0; i<3; i++) {
            if (gameModel.getBlanks().isEmpty())
                return;
            doSingleHint();
        }
    }

    /**
     * Rozwiazuje sudoku do konca korzystajac z doSingleHint
     * @param actionEvent zdarzenie wcisniecia odpowiedniego przycisku
     */
    public void finishSolving(ActionEvent actionEvent) {
        int blanksNumber = gameModel.getBlanks().size();
        for (int i = 0; i< blanksNumber; i++)
            doSingleHint();
    }

    /**
     * Chowa alert konca gry
     * @param actionEvent zdarzenie wcisniecia przycisku zatwierdzajacego alert konca gry
     */
    public void hideGameEndAlert(ActionEvent actionEvent) {
        gameEndAlert.setVisible(false);
    }
}