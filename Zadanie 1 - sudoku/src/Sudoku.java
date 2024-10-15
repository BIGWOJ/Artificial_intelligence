import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (!Character.isDigit(board[i][j])) {
                    for (int k = 1; k <= board.length; k++) {
                        board[i][j] = (char) (k + '0');
                        unknowns--;
                        if (isValid()) {
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


    public Sudoku(Sudoku state) {
        super();
        this.unknowns = state.unknowns;
        this.board = new char[state.board.length][];
        for (int i = 0; i < board.length; i++) {
            this.board[i] = java.util.Arrays.copyOf(state.board[i], state.board[i].length);
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
        for (int i = 0; i < board.length; i++) {
            HashSet<Character> row = new HashSet<>();
            for (int j = 0; j < board[i].length; j++) {
                char current = board[i][j];
                if (Character.isDigit(current) && row.contains(current)) {
                    return false;
                }
                row.add(current);
            }
        }

        // Sprawdzanie kolumn
        for (int i = 0; i < board.length; i++) {
            HashSet<Character> column = new HashSet<>();
            for (int j = 0; j < board[i].length; j++) {
                char current = board[j][i];
                if (Character.isDigit(current) && column.contains(current)) {
                    return false;
                }
                column.add(current);
            }
        }

        // Sprawdzanie małych kwadratów
        int n = (int) Math.sqrt(board.length);
        for (int blockRow = 0; blockRow < n; blockRow++) {
            for (int blockCol = 0; blockCol < n; blockCol++) {
                HashSet<Character> miniSquare = new HashSet<>();
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < n; j++) {
                        char current = board[blockRow * n + i][blockCol * n + j];
                        if (Character.isDigit(current) && miniSquare.contains(current)) {
                            return false;
                        }
                        miniSquare.add(current);
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

class Heurystyka extends StateFunction {
    @Override
    public double calculate (State s) {
        if (s instanceof Sudoku) {
            Sudoku ss = (Sudoku) s;
            return ss.unknown_counter();
        }
        else {
            return Double.NaN;
        }
    }
}

