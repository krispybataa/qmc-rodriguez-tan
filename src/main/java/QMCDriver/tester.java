package QMCDriver;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Class for testing the raw program
 * <p>
 *     Contains simplified direct implementation of the Quine-McCluskey Simulation.
 *     Collects relevant user input and displays the full process of the simulator,
 *     useful for debugging and verifying solutions.
 * </p>
 * @author Clark Rodriguez
 * @author Jamilene Tan
 */

public class tester {
    /**
     * Main method to test the program
     * <p>
     *     Requests user input, then conducts the full process of the Quine-McCluskey
     *     technique with solutions present.
     * </p>
     * @param args Not Used in this scenario
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Minterms:");
        String minterms = sc.nextLine();

        CompuDriver s;
        String dontcare;

        System.out.println();
        System.out.println("Dont Cares if any: ");
        dontcare = sc.nextLine();
        s = new CompuDriver(minterms, dontcare);


        System.out.println();
        s.solveQMC1();
        String res = s.printResults();

        ArrayList<ArrayList<Translations>[]> s1 = s.stepone;

        for (int i = 0; i < s1.size(); i++) {
            System.out.println("Step " + (i + 1));
            for (int j = 0; j < s1.get(i).length; j++) {
                for (int k = 0; k < s1.get(i)[j].size(); k++) {
                    System.out.print(s1.get(i)[j].get(k).getTerm());
                    if (s.taken_step1.size() > i) {
                        if (s.taken_step1.get(i).contains(s1.get(i)[j].get(k).getTerm())) {
                            System.out.print(" taken");
                        }
                    }
                    System.out.println();
                }
                System.out.println("---------------------------");
            }
            System.out.println("\n");
        }

        for (int k = 0; k < s.steptwo.size(); k++) {
            String[][] step2 = s.steptwo.get(k);

            for (int i = 0; i < step2.length; i++) {
                for (int j = 0; j < step2[0].length; j++) {
                    System.out.print(step2[i][j] + "  ");
                }
                System.out.println();
            }
            System.out.println();
        }

        for (int i = 0; i < s.petkeys.size(); i++) {
            System.out.println(s.petkeys.get(i));
        }

        for (int i = 0; i < s.stepthree.size(); i++) {
            System.out.println(s.stepthree.get(i));
        }
    }
}
