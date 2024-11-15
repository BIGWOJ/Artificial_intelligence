import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import sac.StateFunction;
import sac.game.AlphaBetaPruning;
import sac.game.GameSearchAlgorithm;
import sac.game.GameState;
import sac.game.GameStateImpl;

//ilość stanów dodatkowych gdy jest stworzony młynek to ilość pionków możliwych do zbicia
//jeżeli wszystkie pozostałe są w młynku to można zbić dowolny





public class Mill extends GameStateImpl {
//public class Mill {

    public char [][] board = new char[3][8];
    public static final char white_player = 'W';
    public static final char black_player = 'B';
    //Może dać, pieces_counter jako white_pieces_counter + black_pieces_counter ??
    public int pieces_counter = 18;
    public int pieces_palced = 0;
    public int white_pieces_counter = 0;
    public int black_pieces_counter = 0;
    public int whitePiecesToPlace = 9;
    public int blackPiecesToPlace = 9;
    int milled = 0;

    public Mill(){
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = ' ';

//                StringBuilder position = new StringBuilder();
//                position.append(i);
//                position.append(j);
//                board[i][j] = position.toString().charAt(0);
//                board[i][j] = position.toString().charAt(1);

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
            System.out.println("asd");
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
    System.out.println(row + " " + col);
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




    public void handle_mill() {
        boolean pieceRemoved = false;
        Scanner scanner = new Scanner(System.in);
        while (!pieceRemoved) {
            System.out.println("Mill created! Remove an opponent's piece.");
            System.out.println("Enter row of the piece to remove: ");
            int row = scanner.nextInt();
            System.out.println("Enter column of the piece to remove: ");
            int col = scanner.nextInt();
            System.out.println(row + ", " + col);
            if (board[row][col] == (maximizingTurnNow ? black_player : white_player)) {
                if (maximizingTurnNow) {
                    black_pieces_counter--;
                } else {
                    white_pieces_counter--;
                }
                pieces_counter--;
                board[row][col] = ' ';
                pieceRemoved = true;

            } else {
                System.out.println("Invalid piece. Try again.");
            }
        }
    }

    @Override
    public List<GameState> generateChildren() {
        List<GameState> children = new ArrayList<>();
//        maximizingTurnNow=false;
        System.out.println( maximizingTurnNow ? 'W' : 'B');
        // Determine the current phase of the game
        boolean isPlacementPhase = (whitePiecesToPlace > 0 || blackPiecesToPlace > 0);
        boolean isJumpingPhase = (white_pieces_counter <= 3 || black_pieces_counter <= 3);

        if (isPlacementPhase) {
            // Placement phase logic
            for (int square = 0; square < 3; square++) {
                for (int position = 0; position < 8; position++) {
                    if (board[square][position] == ' ') {
                        // Place a piece and create a new GameState
                        Mill child = new Mill(this);

                        child.board[square][position] = maximizingTurnNow ? 'W' : 'B';
                        if (maximizingTurnNow) {
                            child.whitePiecesToPlace--;
                            child.white_pieces_counter++;
                        }
                        else if (!maximizingTurnNow) {
                            child.blackPiecesToPlace--;
                            child.black_pieces_counter++;
                        }
                        if (square == 1 && position == 7) {
                            System.out.println("STOP");
                        }
                        if (child.mill_created(square, position, maximizingTurnNow ? 'W' : 'B')) {
                            System.out.println(square + " " + position);
                            System.out.println("BLACK: " + black_pieces_counter);
                            for (int mill_counter = 0; mill_counter < black_pieces_counter; mill_counter++) {
                                System.out.println("MILL COUNTER: " + mill_counter);
                                Mill mill_child = new Mill(child);
                                System.out.println("PRZED:\n "+mill_child);
                                mill_child.handle_mill();
                                System.out.println("PO: \n"+mill_child);
                                children.add(mill_child);
                            }
                            //children.add(mill_child);
                            //children.addAll(mill_child.generateChildren());
//                            System.out.print("Mill created fgdjihgdfipugdfh");
                        }
                        //System.out.println(maximizingTurnNow);
                        child.toggleTurn();
                        children.add(child);
                    }
                }

           // Jumping phase logic
//            for (int i = 0; i < board.length; i++) {
//                if (board[i] == (maximizingTurnNow ? WHITE : BLACK)) {
//                    for (int j = 0; j < board.length; j++) {
//                        if (board[j] == EMPTY) {
//                            GameStateImpl child = this.clone();
//                            child.board[i] = EMPTY;
//                            child.board[j] = maximizingTurnNow ? WHITE : BLACK;
//                            if (child.formsMill(j)) {
//                                child.handle_mill();
//                            }
//                            child.toggleTurn();
//                            children.add(child);
//                        }
//                    }
//                }
//            }
//        } else {
//            // Movement phase logic
//            for (int i = 0; i < board.length; i++) {
//                if (board[i] == (maximizingTurnNow ? WHITE : BLACK)) {
//                    for (int neighbor : neighbors[i]) {
//                        if (board[neighbor] == EMPTY) {
//                            Mill child = new Mill(this);
//                            child.board[i] = ' ';
//                            child.board[neighbor] = maximizingTurnNow ? WHITE : BLACK;
//                            if (child.formsMill(neighbor)) {
//                                child.handle_mill();
//                            }
//                            child.toggleTurn();
//                            children.add(child);
//                        }
//                    }
//                }
//            }
            }

        }
        System.out.println(milled);
        System.out.println("KONIEC generatedChildren()");
        return children;
    }

//        else {
//            //druga, trzecia faza
//            //trzecia faza to można przeniść na dowolne puste pole
//            if (maximizingTurnNow) {
//                Mill child = new Mill(this);
//                List<GameState> children = new ArrayList<>();
//                for ()
//                place_piece();
//
//            }
//
//            else {
//
//            }
//        }

  public static List<GameState> generateChildrenForDepth(GameState state, int depth) {
    if (depth == 0) {
        return Collections.singletonList(state);
    }
    List<GameState> children = state.generateChildren();
    List<GameState> allChildren = new ArrayList<>(children);
    for (GameState child : children) {
        allChildren.addAll(generateChildrenForDepth(child, depth - 1));
    }
    return allChildren;
}

    private void toggleTurn() {
        maximizingTurnNow = !maximizingTurnNow;
    }

    //Copy constructor while creating children
    public Mill(Mill state) {
        this.board = new char[3][8];
        this.pieces_counter = state.pieces_counter;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 8; j++) {
                this.board[i][j] = state.board[i][j];
            }
        }
    }

    class heuristic_class extends StateFunction {
        public double evaluate(Mill state) {
            int white_pieces = 0;
            int black_pieces = 0;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 8; j++) {
                    if (state.board[i][j] == state.white_player) {
                        white_pieces++;
                    }
                    else if (state.board[i][j] == state.black_player) {
                        black_pieces++;
                    }
                }
            }

            int pieces_diff = white_pieces - black_pieces;

            //Dodać brak możliwości ruchu także jako warunek zwycięstwa
            if (black_pieces <= 2) {
                return Double.POSITIVE_INFINITY;
            }
            else if (white_pieces <= 2) {
                return Double.NEGATIVE_INFINITY;
            }

            return pieces_diff;
        }
    }

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

    public void play() {
        GameState game = new Mill();
        GameSearchAlgorithm algorithm = new AlphaBetaPruning();

        String turn;
        //Human turn
        //boolean maximizing_turn_flag = maximizingTurnNow;
        maximizingTurnNow = true;
        //sac.game.maximizingTurnNow = maximizing_turn_flag;
        //maximizing_turn_flag = false;
        while(!game.isWinTerminal() && !game.isNonWinTerminal()) {
            List<GameState> children = game.generateChildren();
            for (GameState child : children) {
                Scanner scanner = new Scanner(System.in);
                turn = scanner.nextLine();
                if(turn.equals(child.getMoveName())) {
                    game = child;
                    break;
                }

                //W razie podanie błędnego ruchu powtórz wczytanie
                else {
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
        }

        //Computer turn
        //maximizing_turn_flag = true;
        while(!game.isWinTerminal() && !game.isNonWinTerminal()) {
            List<GameState> children = game.generateChildren();
            algorithm.setInitial(game);
            algorithm.execute();
            turn = algorithm.getFirstBestMove();
            for (GameState child : children) {
                if(turn.equals(child.getMoveName())) {
                    game = child;
                    break;
                }

                else {
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

