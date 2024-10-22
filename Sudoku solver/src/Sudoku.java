import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import sac.*;
import sac.graph.GraphState;
import sac.graph.GraphStateImpl;

public class Sudoku extends GraphStateImpl {
    private char[][] board;
    int unknowns;

    //Checking if sudoku is solved
    @Override
    public boolean isSolution (){
        return 0 == unknown_counter();
    }

    //Generating children representing different paths of trying to solve sudoku
    @Override
    public List<GraphState> generateChildren() {
        List<GraphState> lst = new ArrayList<>();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                //Assuming the tile is empty when there is something other than digit
                if (!Character.isDigit(board[i][j])) {
                    //Generating children with every variant of numbers to solve sudoku...
                    for (int k = 1; k <= board.length; k++) {
                        board[i][j] = (char) (k + '0');
                        unknowns--;
                        //... and adding to returning list
                        if (isValid()) {
                            lst.add(new Sudoku(this));
                        }
                        unknowns++;
                        board[i][j] = '.';
                    }
                    //Returning list of potential solving variants (generated children)
                    return lst;
                }
            }
        }
        //If no empty tile was found - return the current list
        return lst;
    }

    //Copy constructor using while generating children
    public Sudoku(Sudoku state) {
        super();
        this.unknowns = state.unknowns;
        this.board = new char[state.board.length][];
        for (int i = 0; i < board.length; i++) {
            this.board[i] = java.util.Arrays.copyOf(state.board[i], state.board[i].length);
        }
    }

    //Sudoku constructor
    public Sudoku(int n) {
        board = new char[n * n][n * n];
        unknowns = n*n*n*n;
    }

    //Creating sudoku from given string e.g. "..4.94." which should be 81 long (9x9 sudoku).
    //"." representing empty tile
    public void fromString(String numbers) {
        if (numbers.length() != 81){
            System.out.println("Number of digits is not correct. There are " + numbers.length() + " instead of 81.");
            System.exit(1);
        }

        board = new char[9][9];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                board[i][j] = numbers.charAt(i * 9 + j);
            }
        }
        unknowns = unknown_counter();
    }

    //Representing sudoku as text in console
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

    //Checking if sudoku is valid (different digits in row, column and mini squares (3x3))
    boolean isValid() {
        //Rows validation
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

        //Columns validation
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

        //Mini squares validation
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

    //Checking how many tiles are empty
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

//Heuristic function
class Heurystyka extends StateFunction {
    @Override
    public double calculate (State s) {
        if (s instanceof Sudoku) {
            Sudoku ss = (Sudoku) s;
            //Returning number of empty tiles to help algorithm define the better path to solve
            return ss.unknown_counter();
        }
        else {
            return Double.NaN;
        }
    }
}

