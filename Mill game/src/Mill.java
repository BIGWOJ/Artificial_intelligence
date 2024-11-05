import sac.State;
import sac.StateFunction;
import sac.game.GameStateImpl;
import sac.graph.GraphState;
import sac.graph.GraphStateImpl;


public class Mill extends GameStateImpl {

    public char [][] board = new char[3][8];
    public char player_white = 'W';
    public char player_black = 'B';

    public Mill(){
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = '0';
            }
        }
    }




    @Override
    public String toString() {
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 8; j++) {
                text.append(board[i][j]);
            }
            text.append("\n");
        }
        return text.toString();
    }

}
