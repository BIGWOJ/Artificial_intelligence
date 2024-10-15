import sac.graph.BestFirstSearch;
import sac.graph.GraphSearchConfigurator;

public class Main {
    public static void main(String[] args) {
        String sudokuTest = "...9348....78.............9..8.....6.2....7.........4.94.38..7.6....9.2.2.5.6.3.4";
        Sudoku sudoku = new Sudoku(3);

        sudoku.fromString(sudokuTest);

        System.out.println("Sudoku poprawne? " + sudoku.isValid());
        System.out.println("Liczba niewiadomych: " + sudoku.unknown_counter());
        System.out.println("Początkowe sudoku:\n" + sudoku);

        GraphSearchConfigurator configurator = new GraphSearchConfigurator();
        configurator.setWantedNumberOfSolutions(Integer.MAX_VALUE);

        Sudoku.setHFunction(new Heurystyka());
        BestFirstSearch bfs = new BestFirstSearch(sudoku, configurator);
        bfs.setInitial(sudoku);
        bfs.execute();

        int liczba_rozwiazan = bfs.getSolutions().size();
        System.out.println("Liczba rozwiązań: " + liczba_rozwiazan);
        if (liczba_rozwiazan == 1) {
            System.out.println("Rozwiązanie:\n" + bfs.getSolutions());
        }
        else {
            System.out.println("Przykładowe rozwiązanie:\n" + bfs.getSolutions().get(0));
        }

        System.out.println("Liczba stanów w zbiorze Open: " + bfs.getOpenSet().size());
        System.out.println("Liczba stanów w zbiorze Closed: " + bfs.getClosedSet().size());
        System.out.println("Czas pracy: " + bfs.getDurationTime() + "ms");
    }
}
