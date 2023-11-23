package QMCDriver;
import java.util.ArrayList;
/**
 * Class for implementing boolean terms within the constants of the Java language.
 * <p>
 *     Contains relevant methods for extracting minterms from user input to be used in the
 *     Quine McCluskey solution for simplifying Boolean Functions.
 * </p>
 * @author Clark Rodriguez
 * @author Jamilene Tan
 */
public class Translations {
    /**
     * String representation of the term.
     * (0, 1, -)
     * Example: 0101, 00-10, --10
     */
    private String term;
    /**
     * Presence of 1s in the Binary representation of the term.
     */
    private int ones;
    /**
     * Number represented by the term stored in ArrayList
     */
    private ArrayList<Integer> numbers;

    /**
     * Constructor class for the minterms in the QMC method
     * <p>
     *     Initializes term by taking the value of the term and adjusting it via the
     *     length parameter for the amount of padding done.
     * </p>
     * @param value The integer/decimal value of the minterm
     * @param length Adjustment parameter for term generation
     */
    public Translations(int value, int length){
        String binaries = Integer.toBinaryString(value);

        StringBuffer temp = new StringBuffer(binaries);
        while(temp.length() != length){
            temp.insert(0,0);
        }
        this.term = temp.toString();

        numbers = new ArrayList<>();
        numbers.add(value);

        ones = 0;
        for(int i = 0; i < term.length(); i++){
            if(term.charAt(i) == '1'){
                ones++;
            }
        }
    }

    /**
     * Constructor class for combined minterms in the QMC method.
     * <p>
     *     Initializes a new term by combining Term 1 and Term 2, assumes
     *     the combination is possible.
     * </p>
     * @param t1 First Term
     * @param t2 Second Term
     */
    public Translations(Translations t1, Translations t2){
        StringBuffer temp = new StringBuffer();

        for(int i = 0; i < t1.getTerm().length(); i++){
            if(t1.getTerm().charAt(i) != t2.getTerm().charAt(i)){
                temp.append("-");
            }else{
               temp.append(t1.getTerm().charAt(i));
            }
        }

        this.term = temp.toString();

        ones = 0;
        for(int i = 0; i < term.length(); i++){
            if(this.term.charAt(i) == '1'){
                ones++;
            }
        }

        numbers = new ArrayList<>();

        numbers.addAll(t1.getNumbers());

        numbers.addAll(t2.getNumbers());
    }

    /**
     * Method to return the String representation of the term
     * @return String value of the minterm.
     */
    public String getTerm() {
        return term;
    }

    /**
     * Method to return the number of 1s present in the binary representation.
     * @return 1s present in the Binary Representaion.
     */
    public int getOnes() {
        return ones;
    }

    /**
     * Method to return the numerical representation of the term.
     * @return Numbers represented by the term.
     */
    public ArrayList<Integer> getNumbers() {
        return numbers;
    }
}
