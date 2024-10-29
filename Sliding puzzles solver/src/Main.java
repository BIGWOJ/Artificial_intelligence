import sac.graph.BestFirstSearch;
import sac.graph.GraphSearchConfigurator;
import sac.graph.GraphState;

public class Main {
    public static void main(String[] args) {
        Puzzle puzzle = new Puzzle(3);
        System.out.println("Starting puzzle:\n" + puzzle);

        int test_number = 100, shuffle_number = 1000;

        double sum_working_time_manhattan = 0;
        int sum_states_open_manhattan = 0, sum_states_closed_manhattan = 0, sum_path_length_manhattan = 0;

        double sum_working_time_mis_tiles = 0;
        int sum_states_open_mis_tiles = 0, sum_states_closed_mis_tiles = 0, sum_path_length_mis_tiles = 0;


        for (int i = 0; i < test_number; i++) {
            GraphState shuffled_puzzle = puzzle.shuffle(shuffle_number);

            //Manhattan distance heuristic
            puzzle.setHFunction(new heuristic_class_manhattan());
            BestFirstSearch bfs = new BestFirstSearch(shuffled_puzzle);
            bfs.execute();

            sum_working_time_manhattan += bfs.getDurationTime();
            sum_states_open_manhattan += bfs.getOpenSet().size();
            sum_states_closed_manhattan += bfs.getClosedSet().size();
            sum_path_length_manhattan += bfs.getSolutions().get(0).getPath().size();

            //Misplaced tiles heuristic
            puzzle.setHFunction(new heuristic_class_misplaced());
            bfs = new BestFirstSearch(shuffled_puzzle);
            bfs.execute();

            sum_working_time_mis_tiles += bfs.getDurationTime();
            sum_states_open_mis_tiles += bfs.getOpenSet().size();
            sum_states_closed_mis_tiles += bfs.getClosedSet().size();
            sum_path_length_mis_tiles += bfs.getSolutions().get(0).getPath().size();
        }

        double average_working_time = sum_working_time_manhattan / test_number;
        int average_states_open = sum_states_open_manhattan / test_number;
        int average_states_closed = sum_states_closed_manhattan/ test_number;
        int average_path_length = sum_path_length_manhattan / test_number;

        System.out.println("Manhattan distance heuristic:");
        System.out.println(String.format("Average working time: %.3f ms", average_working_time));
        System.out.println("Average number of states in Open: " + average_states_open);
        System.out.println("Average number of states in Closed: " + average_states_closed);
        System.out.println("Average path length: " + average_path_length);

        average_working_time = sum_working_time_mis_tiles / test_number;
        average_states_open = sum_states_open_mis_tiles / test_number;
        average_states_closed = sum_states_closed_mis_tiles/ test_number;
        average_path_length = sum_path_length_mis_tiles / test_number;

        System.out.println("\n\nMisplaced tiles heuristic:");
        System.out.println(String.format("Average working time: %.3f ms", average_working_time));
        System.out.println("Average number of states in Open: " + average_states_open);
        System.out.println("Average number of states in Closed: " + average_states_closed);
        System.out.println("Average path length: " + average_path_length);

    }
}
