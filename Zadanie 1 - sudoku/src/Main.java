import java.util.Random;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        Sudoku sudoku_test = new Sudoku(3);

        //Losowe liczby w sudoku
//        StringBuilder random_numbers = new StringBuilder();
//        Random random_number = new Random();
//        for (int i = 0; i < 81; i++){
//            random_numbers.append(random_number.nextInt(1, 10));
//        }
//
//        sudoku_test.fromString(random_numbers.toString());
//        String sudoku_numbers = sudoku_test.toString();
//        System.out.println(sudoku_numbers);
//        System.out.println(sudoku_test.isValid() + "\n");


        //Poprawne liczby w sudoku
        String correct_sudoku = "123456789234567891345678912456789123567891234678912345789123456891234567912345678";
        sudoku_test.fromString(correct_sudoku);
        String correct_sudoku_numbers = sudoku_test.toString();
        System.out.println(correct_sudoku_numbers);
        System.out.println(sudoku_test.isValid() + "\n");

        System.out.println("Liczba niewiadomych: " + sudoku_test.unknown_counter());

    }
}