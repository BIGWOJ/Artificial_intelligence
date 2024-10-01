import java.util.HashSet;

public class Sudoku {
    private char[][] board;

    public Sudoku(int n) {
        board = new char[n * n][n * n];
    }

    public void fromString(String numbers) {
        if (numbers.length() != 81){
            System.out.println("Ilosc cyfr jest niepoprawna.");
            return;
        }
        //System.out.println(board.length);
        board = new char[9][9];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                board[i][j] = numbers.charAt(i * 9 + j);
            }
        }
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
        HashSet<Character> row = new HashSet<>();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                char current = board[i][j];
                if (row.contains(current) || !Character.isDigit(current)) {
                    return false;
                }
                row.add(current);
            }
            row.clear();
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


