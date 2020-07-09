package classes;

import javafx.scene.control.TextField;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Zawiera dane stanu planszy oraz zbior metod bedacych operacjami
 * na tych danych
 */
public class Model {

    /**
     * Klasa pomocnicza sluzaca do reprezentowania pozycji: pary wiersz-kolumna
     */
    static class Position
    {
        int row;
        int col;
        Position(int row, int col) {
            this.row = row;
            this.col = col;
        }
    }

    /**
     * Zawiera dane aktualnej solucji. Na początku zapisane jest na sztywno ziarno do generowania solucji.
     */
    private int[][] solution = new int[][]{
            {5, 3, 8, 4, 6, 1, 7, 9, 2},
            {6, 9, 7, 3, 2, 5, 8, 1, 4},
            {2, 1, 4, 7, 8, 9, 5, 6, 3},
            {9, 4, 1, 2, 7, 8, 6, 3, 5},
            {7, 6, 2, 1, 5, 3, 9, 4, 8},
            {8, 5, 3, 9, 4, 6, 1, 2, 7},
            {3, 8, 9, 5, 1, 2, 4, 7, 6},
            {4, 2, 6, 8, 9, 7, 3, 5, 1},
            {1, 7, 5, 6, 3, 4, 2, 8, 9}
    };

    /**
     * Zawiera dane aktualnej planszy startowej
     */
    private int[][] initial = new int[9][9];

    /**
     * Zawiera zbior poprawnych odpowiedzi uzytkownika
     */
    private List<TextField> userCorrectAnswers = new ArrayList<TextField>();

    /**
     * Zawiera zbior pustych odpowiedzi na planszy
     */
    private List<TextField> blanks = new ArrayList<TextField>();

    /**
     * Znajduje nowa solucje i na jej podstawie tworzy plansze startowa - wywolujac swoje prywatne metody
     */
    void generateBoard(){
        rearrangeSolution();
        makeInitial();
    }

    /**
     * Znajduje nowa solucje poprzez wywolania funkcji pomocniczych, ktore bazujac na istniejacej
     * solucji permutuja cyfry oraz przestawiaja kolumny i wiersze
     */
    private void rearrangeSolution() {
        List<Integer> trinity = new ArrayList<Integer>();
        trinity.add(0); trinity.add(1); trinity.add(2);
        permutateDigits();
        for (int i = 0; i<9; i+=3) {
            Collections.shuffle(trinity);
            swapRow(trinity.get(0)+i, trinity.get(1)+i);
        }
        for (int i = 0; i<9; i+=3) {
            Collections.shuffle(trinity);
            swapCol(trinity.get(0)+i, trinity.get(1)+i);
        }
    }

    /**
     * Permutuje cyfry w solucji
     */
    private void permutateDigits(){
        List<Integer> digits = new ArrayList<Integer>();
        for (int dig = 1; dig < 10; dig++)
            digits.add(dig);
        Collections.shuffle(digits);
        for (int row = 0; row < 9; row++)
            for (int col = 0; col < 9; col++)
                solution[row][col] = digits.get(solution[row][col]-1);
    }

    /**
     * Zamienia miejscami kolumny solucji wewnatrz trojek kolumn 1-3, 4-6, 7-9
     * @param col1 Indeks kolumny pierwszej
     * @param col2 Indeks kolumny drugiej
     */
    private void swapCol(int col1, int col2) {
        List<Integer> columnTmp = new ArrayList<Integer>();
        for (int row = 0; row < 9; row++)
            columnTmp.add(solution[row][col1]);
        for(int row = 0; row<9; row++)
            solution[row][col1] = solution[row][col2];
        for(int row = 0; row<9; row++)
            solution[row][col2] = columnTmp.get(row);
    }

    /**
     * Zamienia miejscami rzedy solucji wewnatrz trojek rzedow 1-3, 4-6, 7-9
     * @param row1 Indeks wiersza pierwszego
     * @param row2 Indeks wiersza drugiego
     */
    private void swapRow(int row1, int row2){
        int[] rowTmp = solution[row1].clone();
        for(int i = 0; i<9; i++)
            solution[row1][i] = solution[row2][i];
        for(int i = 0; i<9; i++)
            solution[row2][i] = rowTmp[i];
    }

    /**
     * Tworzy plansze startowa z solucji poprzez usuniecie pewnej ilosci pol
     */
    private void makeInitial(){
        for (int row = 0; row < 9; row++)
            System.arraycopy(solution[row], 0, initial[row], 0, 9);
        List<Position> fields = new ArrayList<Position>();
        for (int row = 0; row < 9; row++)
            for (int col = 0; col < 9; col++)
                fields.add(new Position(row, col));
        Collections.shuffle(fields);
        for (int index = 0; index < 40; index ++)
            initial[fields.get(index).row][fields.get(index).col] = 0;
    }

    /**
     * Waliduje podana cyfre na danym miejscu porownujac ja z solucja
     * @param row Wiersz planszy na ktorym uzytkownik wpisal cyfre
     * @param col Kolumna planszy na ktorej uzytkownik wpisal cyfre
     * @param number Cyfra wybrana przez uzytkownika
     * @return Informacja czy cyfra uzytkownika na danym polu zgadza sie z solucja
     */
    public boolean isCorrect (int row, int col, int number) {
        return (solution[row][col] == number);
    }

    public int[][] getInitial() {
        return initial;
    }
    public int[][] getSolution() {
        return solution;
    }

    public List<TextField> getUserCorrectAnswers() {
        return userCorrectAnswers;
    }

    public List<TextField> getBlanks() {
        return blanks;
    }

}
