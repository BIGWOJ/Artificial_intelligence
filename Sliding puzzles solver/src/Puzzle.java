import sac.graph.GraphState;
import sac.graph.GraphStateImpl;
import java.util.ArrayList;
import java.util.List;

public class Puzzle extends GraphStateImpl{
    public byte[][] board;
    private int empty_tile_row = 0;
    private int empty_tile_column = 0;
    int misplaced_tiles_counter;

    @Override
    public List<GraphState> generateChildren() {
        List<GraphState> children = new ArrayList<GraphState>();

        //Wiersz inny niż górna krawędź
        if (empty_tile_row != 0) {
            Puzzle puzzle_children = new Puzzle(this);
            puzzle_children.board[empty_tile_row][empty_tile_column] = board[empty_tile_row-1][empty_tile_column];

            puzzle_children.board[empty_tile_row-1][empty_tile_column] = 0;
            puzzle_children.empty_tile_row--;
            //Ruch w dół pustego kafelka
            puzzle_children.setMoveName("down");
            children.add(puzzle_children);
        }

        //Wiersz inny niż dolna krawędź
        if (empty_tile_row != board.length - 1) {
            Puzzle puzzle_children = new Puzzle(this);
            puzzle_children.board[empty_tile_row][empty_tile_column] = board[empty_tile_row+1][empty_tile_column];

            puzzle_children.board[empty_tile_row+1][empty_tile_column] = 0;
            puzzle_children.empty_tile_row--;
            //Ruch w górę pustego kafelka
            puzzle_children.setMoveName("up");
            children.add(puzzle_children);
        }

        //Kolumna inna niż lewa krawędź
        if (empty_tile_column != 0) {
            Puzzle puzzle_children = new Puzzle(this);
            puzzle_children.board[empty_tile_row][empty_tile_column] = board[empty_tile_row][empty_tile_column-1];

            puzzle_children.board[empty_tile_row][empty_tile_column-1] = 0;
            puzzle_children.empty_tile_row--;
            //Ruch w lewo pustego kafelka
            puzzle_children.setMoveName("left");
            children.add(puzzle_children);
        }

        //Kolumna inna niż prawa krawędź
        if (empty_tile_column != board.length - 1) {
            Puzzle puzzle_children = new Puzzle(this);
            puzzle_children.board[empty_tile_row][empty_tile_column] = board[empty_tile_row][empty_tile_column+1];

            puzzle_children.board[empty_tile_row][empty_tile_column+1] = 0;
            puzzle_children.empty_tile_row--;
            //Ruch w prawo pustego kafelka
            puzzle_children.setMoveName("right");
            children.add(puzzle_children);
        }
        return children;
    }

    @Override
    public boolean isSolution() {
        return 0 == misplaced_tiles();
    }

    public Puzzle(int n) {
        board = new byte[n][n];
        misplaced_tiles_counter = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                board[i][j] = (byte) (i*n+j+1);
            }
        }
    }

    //Konstruktor kopiujący
    public Puzzle(Puzzle state) {
        this(state.board.length);
        for (int i = 0; i < state.board.length; i++) {
            for (int j = 0; j < state.board[i].length; j++) {
                board[i][j] = state.board[i][j];
            }
        }
    }

    int misplaced_tiles() {
        int misplaced_counter = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] != 0 && board[i][j] != (i * board.length + j)+1) {
                    misplaced_counter++;
                }
            }
        }
        return misplaced_counter;
    }

    //Odległość dla całej planszy
    int manhattan_distance() {
        int manhattan_distance = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                int current = board[i][j];
                if (current != 0) {
                    int correct_row = (current - 1) / board.length;
                    int correct_column = (current - 1) % board.length;
                    manhattan_distance += Math.abs(i - correct_row) + Math.abs(j - correct_column);
                }
            }
        }
        return manhattan_distance;
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

    int hash_code(){
        String board = toString();
        return board.hashCode();
    }

    boolean isValid() {
        return misplaced_tiles_counter == 0;
    }





}
