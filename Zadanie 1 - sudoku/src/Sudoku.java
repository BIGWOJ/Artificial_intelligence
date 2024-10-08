import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.math.*;
import java.util.List;
import java.util.function.DoubleToIntFunction;

import sac.*;
import sac.graph.GraphState;
import sac.graph.GraphStateImpl;

public class Sudoku extends GraphStateImpl {
    private char[][] board;
    int unknowns;

    @Override
    public boolean isSolution (){
        return 0 == unknown_counter();
    }

    @Override
    public List<GraphState> generateChildren() {
        List<GraphState> lst = new ArrayList<>();
        for (int i = 0; i < board.length; i++){
            for (int j = 0; j < board[i].length; j++){
                if (!Character.isDigit(board[i][j])){
                    for (int k = 1; k <= board.length; k++){
                        board[i][j] = (char) (k+'0');
                        unknowns--;
                        if (isValid()){
                            lst.add(new Sudoku(this));
                        }
                        unknowns++;
                        board[i][j] = '.';
                    }
                    return lst;
                }
            }
        }
        return lst;
    }

    public Sudoku(Sudoku state){
        super();
        unknowns = state.unknowns;
        board = new char[state.board.length][];
        for (int i = 0; i < board.length; i++){
            board[i] = java.util.Arrays.copyOf(state.board[i], board[i].length);
        }
    }

    public Sudoku(int n) {
        board = new char[n * n][n * n];
        unknowns = n*n*n*n;
    }

    public void fromString(String numbers) {
        if (numbers.length() != 81){
            System.out.println("Ilość cyfr jest niepoprawna. Jest " + numbers.length() + " zamiast 81.");
            System.exit(1);
        }
        //System.out.println(board.length);
        board = new char[9][9];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                board[i][j] = numbers.charAt(i * 9 + j);
            }
        }
        unknowns = unknown_counter();
    }

    @Override
    public String toString() {
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                text.append(board[i][j]);
            }
            text.append("\n");
        }
        return text.toString();
    }

    boolean isValid() {
        // Sprawdzanie wierszy
        HashSet<Character> row = new HashSet<>();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                char current = board[i][j];
                if (row.contains(current) && Character.isDigit(current)) {
                    return false;
                }
                row.add(current);
            }
            row.clear();
        }

        // Sprawdzanie kolumn
        HashSet<Character> column = new HashSet<>();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                char current = board[j][i];
                if (column.contains(current) && Character.isDigit(current)) {
                    return false;
                }
                column.add(current);
            }
            column.clear();
        }

        // Sprawdzanie małych kwadratów
        int n = (int) Math.sqrt(board.length); // n będzie równe 3 dla Sudoku 9x9

        for (int blockRow = 0; blockRow < n; blockRow++) {
            for (int blockCol = 0; blockCol < n; blockCol++) {
                HashSet<Character> mini_square = new HashSet<>();
                System.out.println(blockRow);
                // Iteracja po komórkach w danym kwadracie 3x3
                for (int i = 0; i < n; i++) { // 0, 1, 2
                    for (int j = 0; j < n; j++) { // 0, 1, 2
                        //System.out.println(board[i][j]);
                        char current = board[blockRow * n + i][blockCol * n + j];

                        // Sprawdź, czy obecny znak to cyfra i czy już istnieje w mini kwadracie
                        if (Character.isDigit(current)) {
                            if (mini_square.contains(current)) {
                                return false; // Znalazłem duplikat
                            }
                            mini_square.add(current); // Dodaj do zbioru
                        }
                    }
                }
            }
        }

        return true;
    }

    public int unknown_counter(){
        int counter = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                char current = board[i][j];
                if (!Character.isDigit(current)){
                    counter++;
                }
            }
        }
        return counter;
    }
}


