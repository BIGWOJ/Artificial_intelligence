import sac.graph.BestFirstSearch;
import sac.graph.GraphSearchConfigurator;

public class Main {
    public static void main(String[] args) {
        //Testing sudoku
        String sudokuTest = "...9348....78.............9..8.....6.2....7.........4.94.38..7.6....9.2.2.5.6.3.4";
        //Creating new sudoku board
        Sudoku sudoku = new Sudoku(3);

        sudoku.fromString(sudokuTest);

        System.out.println("Is sudoku correct? " + sudoku.isValid());
        System.out.println("Empty tiles: " + sudoku.unknown_counter());
        System.out.println("Starting sudoku:\n" + sudoku);

        //Setting number of solutions returning from searching algorithm
        GraphSearchConfigurator configurator = new GraphSearchConfigurator();
        configurator.setWantedNumberOfSolutions(Integer.MAX_VALUE);

        //Giving sudoku to BFS algorithm to solve it
        Sudoku.setHFunction(new Heurystyka());
        BestFirstSearch bfs = new BestFirstSearch(sudoku, configurator);
        bfs.setInitial(sudoku);
        bfs.execute();

        int solution_numbers = bfs.getSolutions().size();
        System.out.println("Number of solutions: " + solution_numbers);
        if (solution_numbers == 1) {
            System.out.println("Solution:\n" + bfs.getSolutions());
        }
        else {
            System.out.println("Example solution:\n" + bfs.getSolutions().get(0));
        }

        System.out.println("Number of states in Open: " + bfs.getOpenSet().size());
        System.out.println("Number of states in Closed: " + bfs.getClosedSet().size());
        System.out.println("Working time: " + bfs.getDurationTime() + "ms");
    }
}
