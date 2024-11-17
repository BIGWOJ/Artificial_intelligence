public class Main {
    public static void main(String[] args) {

        Mill initial_state = new Mill('W');

        //Przykład II z pdfa
        //initial_state.board[0][1] = 'W';


        //Przykład III z pdfa
        initial_state.board[0][0] = 'W';
        initial_state.board[0][3] = 'W';
        initial_state.board[1][4] = 'W';
        initial_state.board[1][5] = 'W';
        initial_state.board[1][6] = 'W';
        initial_state.board[2][7] = 'W';
        initial_state.board[2][3] = 'W';

        initial_state.board[0][1] = 'B';
        initial_state.board[0][5] = 'B';
        initial_state.board[1][0] = 'B';
        initial_state.board[2][5] = 'B';
        initial_state.black_pieces_counter = 4;
        initial_state.white_pieces_counter = 7;
        initial_state.white_pieces_to_place = 0;
        initial_state.black_pieces_to_place = 0;


        System.out.println("Initial State:");
        System.out.println(initial_state);




        for (int depth = 1; depth <= 2; depth++) {
            int totalStates = Mill.calculate_states(initial_state, depth);
            System.out.println("Total states at depth " + depth + ": " + totalStates);
        }




    }
}
