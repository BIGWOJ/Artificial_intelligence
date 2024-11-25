import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import sac.State;
import sac.StateFunction;
import sac.game.AlphaBetaPruning;
import sac.game.GameSearchAlgorithm;
import sac.game.GameState;
import sac.game.GameStateImpl;

public class Mill extends GameStateImpl {

    public char [][] board = new char[3][8];
    public int white_pieces_counter = 0;
    public int black_pieces_counter = 0;
    public int white_pieces_to_place = 9;
    public int black_pieces_to_place = 9;

    public Mill(char first_turn){
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = ' ';
                maximizingTurnNow = first_turn == 'W';
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder mill_board = new StringBuilder();
        mill_board.append(board[0][6]+"---------------"+board[0][5]+"---------------"+board[0][4]+"\n");
        mill_board.append("|               |               |\n");
        mill_board.append("|     "+board[1][6]+"---------"+board[1][5]+"---------"+board[1][4]+"     |\n");
        mill_board.append("|     |         |         |     |\n");
        mill_board.append("|     |     "+board[2][6]+"---"+board[2][5]+"---"+board[2][4]+"     |     |\n");
        mill_board.append("|     |     |       |     |     |\n");
        mill_board.append(board[0][7]+"-----"+board[1][7]+"-----"+board[2][7]+"       " + board[2][3] + "-----" + board[1][3] + "-----" + board[0][3] + "\n");
        mill_board.append("|     |     |       |     |     |\n");
        mill_board.append("|     |     "+board[2][0]+"---"+board[2][1]+"---"+board[2][2]+"     |     |\n");
        mill_board.append("|     |         |         |     |\n");
        mill_board.append("|     "+board[1][0]+"---------"+board[1][1]+"---------"+board[1][2]+"     |\n");
        mill_board.append("|               |               |\n");
        mill_board.append(board[0][0]+"---------------"+board[0][1]+"---------------"+board[0][2] + "\n");
        return mill_board.toString();
    }

    public void place_piece(int row, int col, char player){
        if (board[row][col] != ' '){
            do {
                System.out.println("Position already occupied. Place piece elsewhere.");
                Scanner scanner = new Scanner(System.in);
                System.out.println("Enter square: ");
                row = scanner.nextInt();

                System.out.println("Enter column: ");
                col = scanner.nextInt();

            }while (board[row][col] != ' ');
            //System.out.println("asd");
            board[row][col] = player;

        }
        else{
            board[row][col] = player;
        }
    }

    public int[] invalid_move() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter square: ");
        int new_row = scanner.nextInt();

        System.out.println("Enter column: ");
        int new_col = scanner.nextInt();
        return new int []{new_row, new_col};
    }

    public void move_piece(int row, int col, int new_row, int new_col, char player) {
        if (board[new_row][new_col] != ' ') {
            do {
                System.out.println("Position already occupied. Place piece elsewhere.");
                new_row = invalid_move()[0];
                new_col = invalid_move()[1];

            }
            while (board[new_row][new_col] != ' ');

            if ((col % 2 == 0 &&
                    !(new_col == (col + 1) % 8 || new_col == (col - 1) % 8))) {
                {
                    do {
                        System.out.println("Invalid move. Place piece elsewhere.");
                        new_row = invalid_move()[0];
                        new_col = invalid_move()[1];

                    }while ((new_col != (new_col + 1) % 8) || (new_col != (new_col - 1) % 8) || (new_row != (new_row + 1) % 3) || (new_row != (new_row - 1) % 3));

                }

            }

            else if ((col % 2 == 0) && (new_row != row)) {
                do {
                    System.out.println("Invalid move. Place piece elsewhere.");
                    new_row = invalid_move()[0];
                    new_col = invalid_move()[1];
                }while (new_row != row);

            }

            board[new_row][new_col] = player;
            board[row][col] = ' ';

        }
    }

    public boolean mill_created(int row, int col, char player) {

        //Piece at corner
        if (col % 2 == 0) {
            if (board[row][(col + 1) % 8] == player && board[row][(col + 2) % 8] == player) {
                return true;
            }
            else if (board[row][(col + 7) % 8] == player && board[row][(col + 6) % 8] == player) {
                return true;
            }
        }

        else if (row == 0) {
            if (board[row][(col + 1) % 8] == player && board[row][(col + 7) % 8] == player) {
                return true;
            }

            else if (board[(row + 1)][col] == player && board[(row + 2)][col] == player) {
                return true;
            }
        }

        else if (row == 1) {
            if (board[row][(col + 1) % 8] == player && board[row][(col + 7) % 8] == player) {
                return true;
            }

            else if (board[(row + 1)][col] == player && board[(row - 1)][col] == player) {
                return true;
            }
        }

        else if (row == 2) {
            if (board[row][(col + 1) % 8] == player && board[row][(col + 7) % 8] == player) {
                return true;
            }

            else if (board[(row - 1)][col] == player && board[(row - 2)][col] == player) {
                return true;
            }
        }

        return false;
    }

    public List<Mill> handle_mill_generate_children() {
        List<Mill> mill_children = new ArrayList<>();

        boolean piece_removed = false;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 8; j++) {

                if (board[i][j] == (maximizingTurnNow ? 'B' : 'W') && !mill_created(i, j, maximizingTurnNow ? 'B' : 'W')) {
                    Mill mill_child = new Mill(this);
                    mill_child.board[i][j] = ' ';

                    piece_removed = true;
                    if (maximizingTurnNow) {
                        mill_child.black_pieces_counter--;
                    }
                    else {
                        mill_child.white_pieces_counter--;
                    }

                    //Change maximizingTurnNow flag
                    mill_child.maximizingTurnNow = !this.maximizingTurnNow;
                    mill_children.add(mill_child);
                }
            }
        }

        //All pieces are in mill - remove any piece
        if(!piece_removed) {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 8; j++) {

                    if (board[i][j] == (maximizingTurnNow ? 'B' : 'W')) {
                        Mill mill_child = new Mill(this);
                        mill_child.board[i][j] = ' ';

                        if (maximizingTurnNow) {
                            mill_child.black_pieces_counter--;
                        }
                        else {
                            mill_child.white_pieces_counter--;
                        }

                        //Change maximizingTurnNow flag
                        mill_child.maximizingTurnNow = !this.maximizingTurnNow;
                        mill_children.add(mill_child);
                    }
                }
            }
        }

        return mill_children;
    }

    public void handle_mill_player(boolean test) {
        boolean piece_removed = false;
        Scanner scanner = new Scanner(System.in);
        while (!piece_removed) {
            int row = 0;
            int col = 0;
            if (!test) {
                System.out.println("Mill created! Remove an opponent's piece: ");
                row = scanner.nextInt();
                col = scanner.nextInt();
            }
            else {
                outer_loop:
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 8; j++) {
                        if (board[i][j] == (maximizingTurnNow ? 'B' : 'W')) {
                            row = i;
                            col = j;
                            break outer_loop;
                        }
                    }
                }
            }

            if (board[row][col] == (maximizingTurnNow ? 'B' : 'W')) {
                if (maximizingTurnNow) {
                    black_pieces_counter--;
                } else {
                    white_pieces_counter--;
                }

                board[row][col] = ' ';
                piece_removed = true;
            }
            else {
                if (!test) {
                    System.out.println("Invalid piece. Try again.");
                }
            }
        }
    }

    @Override
    public List<GameState> generateChildren() {
        List<GameState> children = new ArrayList<>();

        boolean is_placement_phase = (white_pieces_to_place > 0 || black_pieces_to_place > 0);
        boolean is_moving_phase = (white_pieces_to_place == 0 && black_pieces_to_place == 0);
        boolean is_jumping_phase = (maximizingTurnNow && white_pieces_counter == 3 || !maximizingTurnNow && black_pieces_counter == 3) && is_moving_phase;



        //Placement phase
        if (is_placement_phase) {
            for (int square = 0; square < 3; square++) {
                for (int position = 0; position < 8; position++) {
                    if (board[square][position] == ' ') {
                        Mill child = new Mill(this);

                        child.board[square][position] = maximizingTurnNow ? 'W' : 'B';
                        if (maximizingTurnNow) {
                            child.white_pieces_to_place--;
                            child.white_pieces_counter++;
                        }
                        else {
                            child.black_pieces_to_place--;
                            child.black_pieces_counter++;
                        }

                        if (child.mill_created(square, position, maximizingTurnNow ? 'W' : 'B')) {
                            List<Mill> mill_children = child.handle_mill_generate_children();
                            children.addAll(mill_children);
                            for (Mill mill_child : mill_children) {
                                mill_child.setMoveName(String.format("%d %d", square, position));
                            }
                        }
                        else {
                            //No mill created, change maximizingTurnNow flag
                            child.setMoveName(String.format("%d %d", square, position));
                            child.maximizingTurnNow = !child.maximizingTurnNow;
                            children.add(child);
                        }
                    }
                }
            }
        }

        //Moving phase
        else if (!is_jumping_phase) {
            if ((white_pieces_counter <3) || (black_pieces_counter < 3)) {
                return children;
            }
            for (int square = 0; square < 3; square++) {
                for (int position = 0; position < 8; position++) {
                    if (board[square][position] == (maximizingTurnNow ? 'W' : 'B')) {

                        //Iterate over all neighbors of this position
                        //System.out.println(square + ", " + position);
                        for (int[] neighbor : get_neighbors(square, position)) {
                            //System.out.println("\t" + neighbor[0] + ", " + neighbor[1]);
                            int neighbor_square = neighbor[0];
                            int neighbor_position = neighbor[1];

                            if (board[neighbor_square][neighbor_position] == ' ') {
                                Mill child = new Mill(this);
                                //System.out.println(child);
                                //System.out.println("Moving piece from [" + square + ", " + position + "] to [" + neighbor_square + ", " + neighbor_position + "]");
                                child.board[square][position] = ' ';
                                child.board[neighbor_square][neighbor_position] = maximizingTurnNow ? 'W' : 'B';

                                if (child.mill_created(neighbor_square, neighbor_position, maximizingTurnNow ? 'W' : 'B')) {
                                    List<Mill> mill_children = child.handle_mill_generate_children();
                                    children.addAll(mill_children);
                                    for (Mill mill_child : mill_children) {
                                        mill_child.setMoveName(String.format("%d %d", square, position));
                                    }
                                }
                                else {
                                    //No mill created, change maximizingTurnNow flag
                                    child.setMoveName(String.format("%d %d", square, position));
                                    child.maximizingTurnNow = !child.maximizingTurnNow;
                                    children.add(child);
                                }
                            }
                        }
                    }
                }
            }
        }

        //Jumping phase
        else {
            if ((white_pieces_counter <3) || (black_pieces_counter < 3)) {
                return children;
            }
            for (int square = 0; square < 3; square++) {
                for (int position = 0; position < 8; position++) {
                    if (board[square][position] == (maximizingTurnNow ? 'W' : 'B')) {
                        //List<int[]> available_moves = get_available_jumps();
                        for (int[] available_move : get_available_jumps()) {
                            int new_square = available_move[0];
                            int new_position = available_move[1];

                            Mill child = new Mill(this);
                            //System.out.println(child);
                            child.board[square][position] = ' ';
                            child.board[new_square][new_position] = maximizingTurnNow ? 'W' : 'B';

                            if (child.mill_created(new_square, new_position, maximizingTurnNow ? 'W' : 'B')) {
                                List<Mill> mill_children = child.handle_mill_generate_children();
                                children.addAll(mill_children);
                                for (Mill mill_child : mill_children) {
                                    mill_child.setMoveName(String.format("%d %d", square, position));
                                }
                            }
                            else {
                                //No mill created, change maximizingTurnNow flag
                                child.setMoveName(String.format("%d %d", square, position));
                                child.maximizingTurnNow = !child.maximizingTurnNow;
                                children.add(child);
                            }
                        }
                    }
                }
            }
        }

        return children;
    }

    public List<int[]> get_neighbors(int square, int position) {
        List<int[]> neighbors = new ArrayList<>();

        //The same square neighbors
        neighbors.add(new int[]{square, (position + 1) % 8});
        neighbors.add(new int[]{square, (position + 7) % 8});

        //Next square neighbors
        if (position % 2 == 1) {
            if (square > 0) neighbors.add(new int[]{square - 1, position});
            if (square < 2) neighbors.add(new int[]{square + 1, position});
        }

//        System.out.println("Neighbors for [" + square + ", " + position + "] " + board[square][position]);
//        for (int[] neighbor : neighbors) {
//            System.out.println("\tNeighbor: [" + neighbor[0] + ", " + neighbor[1] + "] " + board[neighbor[0]][neighbor[1]]);
//        }
//        System.out.println("\n\n");

        return neighbors;
    }

    public List<int[]> get_available_jumps() {
        List<int[]> available_moves = new ArrayList<>();

        for (int square = 0; square < 3; square++) {
            for (int position = 0; position < 8; position++) {
                if (board[square][position] == ' ') {
                    available_moves.add(new int[]{square, position});
                }
            }
        }
        return available_moves;
    }

    public boolean get_available_moves(char player) {
        for (int square = 0; square < 3; square++) {
            for (int position = 0; position < 8; position++) {
                if (board[square][position] == player) {
                    for (int[] neighbor : get_neighbors(square, position)) {
                        int neighbor_square = neighbor[0];
                        int neighbor_position = neighbor[1];

                        if (board[neighbor_square][neighbor_position] == ' ') {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public static int calculate_states(Mill state, int depth) {
        //System.out.println(state);
        if (depth == 0) {
            //state.toStr();
            return 1;
        }

        //Generate children of the current state
        List<GameState> children = state.generateChildren();
        int total_states = 0;

        //Recursively compute states for all children
        for (GameState child : children) {
            total_states += calculate_states((Mill) child, depth - 1);
        }

        return total_states;


    }

    //Copy constructor while creating children
    public Mill(Mill state) {
        this.board = new char[3][8];
        this.white_pieces_counter = state.white_pieces_counter;
        this.black_pieces_counter = state.black_pieces_counter;
        this.maximizingTurnNow = state.maximizingTurnNow;
        this.white_pieces_to_place = state.white_pieces_to_place;
        this.black_pieces_to_place = state.black_pieces_to_place;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 8; j++) {
                this.board[i][j] = state.board[i][j];
            }
        }
    }

    public void toStr() {
        for (int i = 0; i <3; i++) {
            for(int j = 0; j < 8; j++) {
                if(board[i][j] == 'W') {
                    System.out.print(i*8+j + " ");
                }

            }
        }
        System.out.print("|");

        for (int i = 0; i <3; i++) {
            for(int j = 0; j < 8; j++) {
                if(board[i][j] == 'B') {
                    System.out.print(" " + (i*8+j));
                }

            }
        }

        //System.out.println();
    }

   class heuristic_class extends StateFunction {
    @Override
    public double calculate(State state) {
        Mill mill = (Mill) state;
        int whitePawns = mill.white_pieces_counter;
        int blackPawns = mill.black_pieces_counter;
        int pawnsToPlace = mill.white_pieces_to_place + mill.black_pieces_to_place;

        if (pawnsToPlace <=0) {

            if (blackPawns < 3) {
                return Double.POSITIVE_INFINITY;
            } else if (whitePawns < 3) {
                return Double.NEGATIVE_INFINITY;
            }
        }
        return whitePawns - blackPawns;
    }
}

//    class heuristic_class extends StateFunction {
//        @Override
//        public double calculate(State state) {
//            Mill mill = (Mill) state;
//            int white_pieces = 0;
//            int black_pieces = 0;
//            for (int i = 0; i < 3; i++) {
//                for (int j = 0; j < 8; j++) {
//                    if (mill.board[i][j] == mill.white_player) {
//                        white_pieces++;
//                    }
//                    else if (mill.board[i][j] == mill.black_player) {
//                        black_pieces++;
//                    }
//                }
//            }

//            int pieces_diff = white_pieces - black_pieces;
//
//            if (black_pieces <= 2 || !get_available_moves('B') ) {
//                return Double.POSITIVE_INFINITY;
//            }
//            else if (white_pieces <= 2 || !get_available_moves('W')) {
//                return Double.NEGATIVE_INFINITY;
//            }
//
//            return pieces_diff;
//        }
//    }

    @Override
    public int hashCode() {
        StringBuilder board = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 8; j++) {
                board.append(this.board[i][j]);
            }
        }
        return board.hashCode();
    }

    //PLAY

    public void play_pdf() {
        Mill game = new Mill('W');
        GameSearchAlgorithm algorithm = new AlphaBetaPruning();

        String turn;
        //Human turn
        maximizingTurnNow = true;
        while (!game.isWinTerminal() && !game.isNonWinTerminal()) {
            List<GameState> children = game.generateChildren();
            for (GameState child : children) {
                Scanner scanner = new Scanner(System.in);
                turn = scanner.nextLine();
                if (turn.equals(child.getMoveName())) {
                    game = (Mill) child;
                    break;
                } else {
                    System.out.println("Invalid move. Try again.");
                    turn = scanner.nextLine();
                }

                if (game.isWinTerminal() || game.isNonWinTerminal()) {
                    break;
                }
                children = game.generateChildren();
                algorithm.setInitial(game);
                algorithm.execute();
                turn = algorithm.getFirstBestMove();

            }


            //Computer turn
            maximizingTurnNow = true;
            while (!game.isWinTerminal() && !game.isNonWinTerminal()) {
                children = game.generateChildren();
                algorithm.setInitial(game);
                algorithm.execute();
                turn = algorithm.getFirstBestMove();
                for (GameState child : children) {
                    if (turn.equals(child.getMoveName())) {
                        game = (Mill) child;
                        break;
                    } else {
                        turn = algorithm.getFirstBestMove();
                    }

                    if (game.isWinTerminal() || game.isNonWinTerminal()) {
                        break;
                    }
                    children = game.generateChildren();
                    algorithm.setInitial(game);
                    algorithm.execute();
                    turn = algorithm.getFirstBestMove();

                }
            }
        }
    }

    public void play() {
        Scanner scanner = new Scanner(System.in);
        Mill game = new Mill('W');
        GameSearchAlgorithm algorithm = new AlphaBetaPruning();
        System.out.println("Mill game started! White to move.");

        while (!game.isWinTerminal() && !game.isNonWinTerminal()) {
            System.out.println(game);

            if (game.maximizingTurnNow) {
                System.out.println("Enter move: ");
                boolean valid_move = false;
                while (!valid_move) {
                    try {
                        int row = scanner.nextInt();
                        int col = scanner.nextInt();
                        if (game.board[row][col] == ' ') {
                            game.place_piece(row, col, 'W');
                            valid_move = true;

                            if (game.mill_created(row, col, 'W')) {
                                game.handle_mill_player(false);
                            }

                            game.maximizingTurnNow = false;
                        }
                        else {
                            System.out.println("Square occupied. Enter move again:");
                        }
                    }
                    catch (Exception e) {
                        System.out.println("Invalid move's format. Enter move again:");
                        scanner.nextLine();
                    }
                }

            }
            else {
                System.out.println("AI's move...");
                algorithm.setInitial(game);
                algorithm.execute();

                String bestMove = algorithm.getFirstBestMove();
                boolean valid_move = false;

                for (GameState child : game.generateChildren()) {
                    if (child.getMoveName().equals(bestMove)) {
                        game = (Mill) child;
                        valid_move = true;
                        break;
                    }
                }

                if (valid_move) {
                    System.out.println("AI chose move: " + bestMove);
                    game.maximizingTurnNow = true;
                }
                else {
                    System.out.println("AI zły ruch");
                    break;
                }
            }
        }

        if (game.isWinTerminal()) {
            System.out.println("Game ended! : " + (game.maximizingTurnNow ? "White" : "Black") + " won.");
        }
        else {
            System.out.println("Game ended! Draw.");
        }
    }

}
