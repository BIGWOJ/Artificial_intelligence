
public class Main {
    public static void main(String[] args) {
        Puzzle puzzle = new Puzzle(3);
        System.out.println(puzzle);
        System.out.println(puzzle.misplaced_tiles());

        System.out.println(puzzle);
        System.out.println(puzzle.misplaced_tiles());
    }
}