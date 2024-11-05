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


    //Creating children for the puzzle state by moving the empty tile
    @Override
    public List<GraphState> generateChildren() {
        List<GraphState> children = new ArrayList<GraphState>();

        //Row different than top edge
        if (empty_tile_row != 0) {
            Puzzle puzzle_children = new Puzzle(this);
            puzzle_children.board[empty_tile_row][empty_tile_column] = board[empty_tile_row - 1][empty_tile_column];

            puzzle_children.board[empty_tile_row - 1][empty_tile_column] = 0;
            puzzle_children.empty_tile_row--;
            //Move down empty tile
            puzzle_children.setMoveName("down");
            children.add(puzzle_children);
        }

        //Row different than bottom edge
        if (empty_tile_row != board.length - 1) {
            Puzzle puzzle_children = new Puzzle(this);
            puzzle_children.board[empty_tile_row][empty_tile_column] = board[empty_tile_row + 1][empty_tile_column];

            puzzle_children.board[empty_tile_row + 1][empty_tile_column] = 0;
            puzzle_children.empty_tile_row++;
            //Move up empty tile
            puzzle_children.setMoveName("up");
            children.add(puzzle_children);
        }

        //Column different than left edge
        if (empty_tile_column != 0) {
            Puzzle puzzle_children = new Puzzle(this);
            puzzle_children.board[empty_tile_row][empty_tile_column] = board[empty_tile_row][empty_tile_column - 1];

            puzzle_children.board[empty_tile_row][empty_tile_column - 1] = 0;
            puzzle_children.empty_tile_column--;
            //Move left empty tile
            puzzle_children.setMoveName("left");
            children.add(puzzle_children);
        }

        //Column different than right edge
        if (empty_tile_column != board.length - 1) {
            Puzzle puzzle_children = new Puzzle(this);
            puzzle_children.board[empty_tile_row][empty_tile_column] = board[empty_tile_row][empty_tile_column + 1];

            puzzle_children.board[empty_tile_row][empty_tile_column + 1] = 0;
            puzzle_children.empty_tile_column++;
            //Move right empty tile
            puzzle_children.setMoveName("right");
            children.add(puzzle_children);
        }
        return children;
    }

    //Checking if the puzzle is solved
    @Override
    public boolean isSolution() {
        return 0 == misplaced_tiles();
    }

    //Constructor with filled correct board with 0 at [0][0] index
    public Puzzle(int n) {
        board = new byte[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                board[i][j] = (byte) (i * n + j);
            }
        }
    }

    //Copy constructor using while creating children
    public Puzzle(Puzzle state) {
        this(state.board.length);
        for (int i = 0; i < state.board.length; i++) {
            for (int j = 0; j < state.board[i].length; j++) {
                board[i][j] = state.board[i][j];
            }
        }
        this.empty_tile_row = state.empty_tile_row;
        this.empty_tile_column = state.empty_tile_column;
    }

    //Number of misplaced tiles
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

    //Manhattan distance for the whole board
    int manhattan_distance() {
        int manhattan_distance = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                int current = board[i][j];
                if (current != 0) {
                    //Caclulating correct position for the current number
                    int correct_row = current / board.length;
                    int correct_column = current % board.length;
                    manhattan_distance += Math.abs(i - correct_row) + Math.abs(j - correct_column);
                }
            }
        }
        return manhattan_distance;
    }

    //Overriding toString method to print the board
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

    //Creating puzzle from string
    public void from_string(String numbers) {
        int n = (int) Math.sqrt(numbers.length());
        board = new byte[n][n];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                board[i][j] = (byte) (numbers.charAt(i * 3 + j) - '0');
            }
        }
    }

    //Overriding hashCode method
    @Override
    public int hashCode() {
        String board = toString();
        return board.hashCode();
    }

    //Shuffling the board
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

//Heuristic class using manhattan distance
class heuristic_class_manhattan extends StateFunction {
    @Override
    public double calculate (State s) {
        if (s instanceof Puzzle) {
            return ((Puzzle) s).manhattan_distance();
        }
        else return Double.NaN;
    }
}

//Heuristic class using misplaced tiles
class heuristic_class_misplaced extends StateFunction {
    @Override
    public double calculate (State s) {
        if (s instanceof Puzzle) {
            return ((Puzzle) s).misplaced_tiles();
        }
        else return Double.NaN;
    }
}
