import sac.graph.GraphState;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        Sudoku sudoku_test = new Sudoku(3);

        //Losowe liczby w sudoku
//        StringBuilder random_numbers = new StringBuilder();
//        Random random_number = new Random();
//        for (int i = 0; i < 81; i++){
//            //random_numbers.append(random_number.nextInt(1,10));
//            random_numbers.append(random_number.nextInt(9)+1);
//        }
//
//        sudoku_test.fromString(random_numbers.toString());
//        String sudoku_numbers = sudoku_test.toString();
//        System.out.println(sudoku_numbers);


        //Poprawne liczby w sudoku
//        String correct_sudoku =
//                "123456789" +
//                        "234567891" +
//                        "345678912" +
//                        "456789123" +
//                        "567891234" +
//                        "678912345" +
//                        "789123456" +
//                        "891234567" +
//                        "912345678";
//
//        sudoku_test.fromString(correct_sudoku);
//        String correct_sudoku_numbers = sudoku_test.toString();
//        System.out.println(correct_sudoku_numbers);


        String test = "7.9..4.....4958..........5.34..1.....2...3....91.7.84.....82.......4.3..81...7..2";
        sudoku_test.fromString(test);

        System.out.println("Sudoku poprawne?: " + sudoku_test.isValid() + "\n");
        System.out.println(sudoku_test.toString());
        System.out.println("Liczba niewiadomych: " + sudoku_test.unknown_counter());
        //System.out.println(sudoku_test.generateChildren());




    }
}