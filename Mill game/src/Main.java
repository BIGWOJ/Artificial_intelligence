import java.util.List;
import sac.game.GameState;
import sac.game.GameStateImpl;

public class Main {
    public static void main(String[] args) {

        Mill initialState = new Mill();
        //initialState.initializeBoard();

        initialState.board[0][0] = 'W';
        initialState.board[0][3] = 'W';
        initialState.board[1][4] = 'W';
        initialState.board[1][5] = 'W';
        initialState.board[1][6] = 'W';
        initialState.board[2][7] = 'W';
        initialState.board[2][3] = 'W';

        initialState.board[0][1] = 'B';
        initialState.board[0][5] = 'B';
        initialState.board[1][0] = 'B';
        initialState.board[2][5] = 'B';
        initialState.black_pieces_counter = 4;
        initialState.white_pieces_counter = 7;




        System.out.println("Initial State:");
        System.out.println(initialState);
        int glebokosc = 1;

        List<GameState> children = initialState.generateChildren();
        System.out.println("Liczba stanów: " + children.size());







    }
}
