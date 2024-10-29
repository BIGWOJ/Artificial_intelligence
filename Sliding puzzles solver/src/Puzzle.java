import sac.State;
import sac.StateFunction;
import sac.graph.GraphState;
import sac.graph.GraphStateImpl;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Puzzle extends GraphStateImpl {
    public byte[][] board;
    private int empty_tile_row = 0;
    private int empty_tile_column = 0;


    @Override
    public List<GraphState> generateChildren() {
        List<GraphState> children = new ArrayList<GraphState>();

        //Wiersz inny niż górna krawędź
        if (empty_tile_row != 0) {
            Puzzle puzzle_children = new Puzzle(this);
            puzzle_children.board[empty_tile_row][empty_tile_column] = board[empty_tile_row - 1][empty_tile_column];

            puzzle_children.board[empty_tile_row - 1][empty_tile_column] = 0;
            puzzle_children.empty_tile_row--;
            //Ruch w dół pustego kafelka
            puzzle_children.setMoveName("down");
            children.add(puzzle_children);
        }

        //Wiersz inny niż dolna krawędź
        if (empty_tile_row != board.length - 1) {
            Puzzle puzzle_children = new Puzzle(this);
            puzzle_children.board[empty_tile_row][empty_tile_column] = board[empty_tile_row + 1][empty_tile_column];

            puzzle_children.board[empty_tile_row + 1][empty_tile_column] = 0;
            puzzle_children.empty_tile_row++;
            //Ruch w górę pustego kafelka
            puzzle_children.setMoveName("up");
            children.add(puzzle_children);
        }

        //Kolumna inna niż lewa krawędź
        if (empty_tile_column != 0) {
            Puzzle puzzle_children = new Puzzle(this);
            puzzle_children.board[empty_tile_row][empty_tile_column] = board[empty_tile_row][empty_tile_column - 1];

            puzzle_children.board[empty_tile_row][empty_tile_column - 1] = 0;
            puzzle_children.empty_tile_column--;
            //Ruch w lewo pustego kafelka
            puzzle_children.setMoveName("left");
            children.add(puzzle_children);
        }

        //Kolumna inna niż prawa krawędź
        if (empty_tile_column != board.length - 1) {
            Puzzle puzzle_children = new Puzzle(this);
            puzzle_children.board[empty_tile_row][empty_tile_column] = board[empty_tile_row][empty_tile_column + 1];

            puzzle_children.board[empty_tile_row][empty_tile_column + 1] = 0;
            puzzle_children.empty_tile_column++;
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
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                board[i][j] = (byte) (i * n + j);
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
        this.empty_tile_row = state.empty_tile_row;
        this.empty_tile_column = state.empty_tile_column;
        //this.misplaced_tiles_counter = state.misplaced_tiles_counter;
    }


    int misplaced_tiles() {
        int misplaced_counter = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] != 0 && board[i][j] != (i * board.length + j)) {
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
                    int correct_row = current / board.length;
                    int correct_column = current % board.length;
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
//
//    public void from_string(String numbers) {
//        board = new byte[3][3];
//        for (int i = 0; i < board.length; i++) {
//            for (int j = 0; j < board.length; j++) {
//                board[i][j] = (byte) numbers.charAt(i * 3 + j);
//            }
//        }
//    }

    int hash_code() {
        String board = toString();
        return board.hashCode();
    }

    public GraphState shuffle(int n) {
        Random r = new Random();
        GraphState s = this;
        for (int i = 0; i < n; i++) {
            List<GraphState> children = s.generateChildren();
            s = children.get(r.nextInt(children.size()));
        }
        return s;
    }
}

class Heurystyka extends StateFunction {
    @Override
    public double calculate (State s) {
        if (s instanceof Puzzle) {
            return ((Puzzle) s).misplaced_tiles();
        }
        else return Double.NaN;
    }
}