public class Test {
    public static void pdf_example(int example, char first_move) {
        Mill initial_state = new Mill(first_move);
        switch(example) {
            case 1:
                break;

            case 2:
                initial_state.board[0][1] = 'W';
                initial_state.white_pieces_counter = 1;
                initial_state.white_pieces_to_place = 8;
                break;

            case 3:
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
                break;
        }
        for (int depth = 1; depth <= 6; depth++) {
            int totalStates = Mill.calculate_states(initial_state, depth);
            System.out.println("Total states at depth " + depth + ": " + totalStates);
        }
        System.out.println();
    }

    public static void play() {
        Mill initial_state = new Mill('B');
        Mill.setHFunction(new heuristic_class());
        initial_state.play();
    }
}

