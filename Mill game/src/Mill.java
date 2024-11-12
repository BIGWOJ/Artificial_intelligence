import java.util.ArrayList;
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
    public int pieces_counter = 18;
    public int pieces_palced = 0;
    public int white_pieces_counter = 9;
    public int black_pieces_counter = 9;

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

//    public void place_piece(int row, int col, char player){
//        if (board[row][col] != ' '){
//            do {
//                System.out.println("Position already occupied. Place piece elsewhere.");
//                Scanner scanner = new Scanner(System.in);
//                System.out.println("Enter square: ");
//                row = scanner.nextInt();
//
//                System.out.println("Enter column: ");
//                col = scanner.nextInt();
//
//            }
//            while (board[row][col] != ' ');
////            System.out.println("asd");
//            board[row][col] = player;
//
//        }
//        else{
//            board[row][col] = player;
//        }
//    }

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

                    }
                    while ((new_col != (new_col + 1) % 8) || (new_col != (new_col - 1) % 8) || (new_row != (new_row + 1) % 3) || (new_row != (new_row - 1) % 3));

                }

            }

            else if ((col % 2 == 0) && (new_row != row)) {
                do {
                    System.out.println("Invalid move. Place piece elsewhere.");
                    new_row = invalid_move()[0];
                    new_col = invalid_move()[1];
                }
                while (new_row != row);

            }

            board[new_row][new_col] = player;
            board[row][col] = ' ';

        }
    }

    public boolean mill_created(int row, int col, char player) {
        //j parzyste - j+/-2 mod 8, jeżeli są trzy jedynki, to jest młynek
        //j nieparzyste - j+/-1 mod 8 ORAZ i+-1/2 j takie samo, jeżeli są trzy jedynki, to jest młynek
        if (col % 2 == 0) {
            if (board[row][(col + 1) % 8] == player && board[row][(col + 2) % 8] == player) {
                return true;
            }
            else if (board[row][(col + 1) % 8] == player && board[row][(col + 6) % 8] == player) {
                return true;
            }
            else if (board[(row + 1)][col] == player && board[(row + 2)][col] == player) {
                return true;
            }
            else if (board[(row - 1)][col] == player && board[(row - 2)][col] == player) {
                return true;
            }
        }

        else {
            if (board[row][(col + 1) % 8] == player && board[row][(col + 2) % 8] == player) {
                return true;
            }
            else if (board[row][(col + 7) % 8] == player && board[row][(col + 6) % 8] == player) {
                return true;
            }

            else if (board[(row + 1)][col] == player && board[(row + 2)][col] == player) {
                return true;
            }



        }

        return false;
    }


    @Override
    public List<GameState> generateChildren() {
        char player = maximizingTurnNow ? 'W' : 'B';
        char enemy = maximizingTurnNow ? 'B' : 'W';
        if (pieces_palced != 18) {
            //pierwsza faza

            List<GameState> children = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 8; j++) {
                    if (board[i][j] == ' ') {
                        Mill child = new Mill(this);
                        if (mill_created(i, j, player)) {
                            if (player == 'W') {
                                child.black_pieces_counter--;
                            }

                            else {
                                child.white_pieces_counter--;
                            }

                            for (int k= 0; k < 3; k++) {
                                for (int z = 0; z < 8; z++) {
                                    if ((child.board[i][j] == enemy) && (mill_created(k, z, enemy))) {
                                        if (mill_created(k, z, enemy)) {
                                            if (player == 'W') {

                                            }

                                            else {

                                            }
                                        }
                                    }
                                }
                            }
                        }
                        //child.place_piece(i, j, player);
                        children.add(child);
                    }
                }
                return children;
            }

        }

        else {
            //druga, trzecia faza
            //trzecia faza to można przeniść na dowolne puste pole
            if (maximizingTurnNow) {
                Mill child = new Mill(this);
                List<GameState> children = new ArrayList<>();
                for ()
                place_piece();

            }

            else {

            }
        }


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

