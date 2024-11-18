public class Main {
    public static void main(String[] args) {

        Mill initial_state = new Mill('W');

//        int white_pieces_counter = 4;
//        int black_pieces_counter = 4;
//        boolean is_jumping_phase = (white_pieces_counter <= 3 || black_pieces_counter <= 3);
//        System.out.println(is_jumping_phase);
//        System.exit(0);





        //Przykład II z pdfa
//       initial_state.board[0][1] = 'W';
//       initial_state.white_pieces_counter = 1;
//       initial_state.white_pieces_to_place = 8;

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


          //Dodatkowo, aby powstał młynek dla czarnych
//        initial_state.board[1][2] = 'B';
//        initial_state.black_pieces_to_place++;


        //System.out.println("Initial State:");
        //System.out.println(initial_state);




        for (int depth = 1; depth <= 5; depth++) {
            int totalStates = Mill.calculate_states(initial_state, depth);
            System.out.println("Total states at depth " + depth + ": " + totalStates);
        }




        //Testowanie get_neighbours
//        for (int square = 0; square < 3; square++) {
//            for (int position = 0; position < 8; position++) {
//                System.out.println(square + ", " + position);
//                for (int[] neighbor : initial_state.get_neighbors(square, position)) {
//                    System.out.println("\t" + neighbor[0] + ", " + neighbor[1]);
//                }
//                System.out.println();
//            }
//        }

        //Testowanie get_available_jumps
//        StringBuilder a = new StringBuilder();
//        for (int square = 0; square < 3; square++) {
//            for (int position = 0; position < 8; position++) {
//                //System.out.println(square + ", " + position);
//                for (int[] jump : initial_state.get_available_jumps()) {
//                    //System.out.println("\t" + jump[0] + ", " + jump[1]);
//                    a.append(jump[0]);
//                    a.append(jump[1]);
//                }
//                //System.out.println();
//            }
//        }
//        //System.out.println(a);



    }
}
