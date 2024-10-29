import sac.graph.BestFirstSearch;
import sac.graph.GraphSearchConfigurator;
import sac.graph.GraphState;

public class Main {
    public static void main(String[] args) {
        Puzzle puzzle = new Puzzle(3);
        System.out.println(puzzle);
        System.out.println(puzzle.misplaced_tiles());
        System.out.println(puzzle.manhattan_distance());


//        String test = "510247863";
//        puzzle.from_string(test);

//        puzzle.board[0][0] = 5;
//        puzzle.board[0][1] = 1;
//        puzzle.board[0][2] = 0;
//
//        puzzle.board[1][0] = 2;
//        puzzle.board[1][1] = 4;
//        puzzle.board[1][2] = 7;
//
//        puzzle.board[2][0] = 8;
//        puzzle.board[2][1] = 6;
//        puzzle.board[2][2] = 3;

        System.out.println("Can puzzle be solved? " + puzzle.isSolution());
        System.out.println("Starting puzzle:\n" + puzzle);

        //Giving puzzle to BFS algorithm to solve it
        puzzle.setHFunction(new Heurystyka());
        BestFirstSearch bfs = new BestFirstSearch(puzzle);
        GraphState shuffled_puzzle = puzzle.shuffle(10);
        bfs.setInitial(shuffled_puzzle);
        bfs.execute();

        System.out.println("Number of states in Open: " + bfs.getOpenSet().size());
        System.out.println("Number of states in Closed: " + bfs.getClosedSet().size());




        //ilość odwiedzonych stanów, czas, closed, open





//
//        int solution_numbers = bfs.getSolutions().size();
//        System.out.println("Number of solutions: " + solution_numbers);
//        if (solution_numbers == 1) {
//            System.out.println("Solution:\n" + bfs.getSolutions());
//        }
//        else {
//            System.out.println("Example solution:\n" + bfs.getSolutions().get(0));
//        }
//
//        System.out.println("Number of states in Open: " + bfs.getOpenSet().size());
//        System.out.println("Number of states in Closed: " + bfs.getClosedSet().size());
//        System.out.println("Working time: " + bfs.getDurationTime() + "ms");
    }

}
