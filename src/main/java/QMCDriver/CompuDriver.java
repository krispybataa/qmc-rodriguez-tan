package QMCDriver;
import java.util.*;

/**
 * Main driver class for implementing the Quine McCluskey Method to simplify Boolean Functions.
 * <p>
 *     Contains relevant methods for implementing the solvihng technique within the Java programming language.
 *     Remark: Implements the QMC technique with the assistance of the Petrick Method.
 * </p>
 * @author Clark Rodriguez
 * @author Jamilene Tan
 */
public class CompuDriver {
    /**
     * Inherited implementation of the comparator to compare two minterms
     * <p>
     *     Returns prescribed values for given circumstances. Compares the ones returned
     *     for each instance and sorts accordingly. Smaller '1s' take presedence over larger '1s',
     *     if their value equal, natural order takes precedence.
     * </p>
     */
    private class compareOnes implements Comparator<Translations> {
        @Override
        public int compare(Translations a, Translations b) {
            if (a == null && b == null) {
                return 0; // both are null, consider them equal
            } else if (a == null) {
                return 1; // a is null, b is considered greater
            } else if (b == null) {
                return -1; // b is null, a is considered greater
            } else {
                return a.getOnes() - b.getOnes();
            }
        }
    }
    /**
     * Term Compilation (Minterms & Don't Cares)
     */
    private Translations[] terms;
    /**
     * ArrayList for Minterms
     */
    private ArrayList<Integer> minterms;
    /**
     * ArrayList for Don't Cares
     */
    private ArrayList<Integer> dontcares;
    /**
     * 2D ArrayList for Procedural Variables (First Step)
     */
    public ArrayList<ArrayList<Translations>[]> stepone;
    /**
     * Step 1
     */
    public ArrayList<HashSet<String>> taken_step1;
    /**
     * 2D ArrayList for Procedural Variables (Second Step)
     */
    public ArrayList<String[][]> steptwo;
    /**
     * 2D ArrayList for Procedural Variables (Three Step)
     */
    public ArrayList<String> stepthree;
    /**
     * ArrayList to Hold the steps for the Petrick Process
     */
    public ArrayList<String> petkeys;
    /**
     * ArrayList that holds the solution
     */
    private ArrayList<String>[] solution;
    /**
     * ArrayList that holds the Prime Implicants
     */
    private ArrayList<String> primeimplicants;
    /**
     * ArrayList that holds the final batch of terms
     */
    private ArrayList<Translations> finalterms;
    /**
     * Int variable for iterations
     */
    private int maxlen; 

    /**
     * Constructor class for the implementation of the Quine McCluskey Method
     * <p>
     *     Conducts a run-through of the Quine McCluskey solving method under the notion
     *     that there are Don't Cares to account for.
     * </p>
     * @param stringminterms User Input, the values of the minterms for simplification
     * @param stringdontcares User Input or Predetermined, the values of the Don't Cares to account for.
     */
    public CompuDriver(String stringminterms, String stringdontcares){
        //Sort 2 Input Arrays
        int[] minarr = checkValidity(stringminterms);
        int[] dontarr = checkValidity(stringdontcares);
        if(!check(minarr, dontarr)){
            throw new RuntimeException("Error, Invalid Input: Please Adjust.");
        }

        Arrays.sort(dontarr);
        Arrays.sort(minarr);

        //Calculate the maximum possible length of prime implicants
        maxlen = Integer.toBinaryString(minarr[minarr.length - 1]).length();

        //Initialize Instance Variables
        this.dontcares = new ArrayList<Integer>();
        this.minterms = new ArrayList<Integer>();

        primeimplicants = new ArrayList<String>();
        stepone = new ArrayList<ArrayList<Translations>[]>();
        taken_step1 = new ArrayList<HashSet<String>>();
        steptwo = new ArrayList<String[][]>();
        stepthree = new ArrayList<String>();
        petkeys = new ArrayList<String>();

        //Combine Minterms and Don't Cares in one array
        Translations[] temp = new Translations[minarr.length + dontarr.length];
        int k = 0; //Intex for iterations
        for(int i = 0; i < minarr.length; i++){
            temp[k++] = new Translations(minarr[i], maxlen);
            this.minterms.add(minarr[i]);
        }

        for(int i = 0; i < dontarr.length; i++){
            if(Integer.toBinaryString(dontarr[i]).length() > maxlen){
                break;
            }
            temp[k++] = new Translations(dontarr[i], maxlen);
            this.dontcares.add(dontarr[i]);
        }
        terms = new Translations[k];

        for(int i = 0; i < k; i++){
            terms[i] = temp[i];
        }

        //Sort Arrays according to occurence of 1s
        Arrays.sort(terms, new compareOnes());
    }

    /**
     * Method to compare the input for repetition
     * <p>
     *     Boolean method that take the two input arrays and compares the two
     *     to check for repeated minterms. Returns false if no duplicates are detected.
     * </p>
     * @param m User input, the Minterms array
     * @param d User input or predetermined, the Don't Cares array
     * @return False if no repetitions, True if repetitions are present.
     */
    boolean check(int[] m, int[] d){
        HashSet<Integer> temp = new HashSet<>();
        for (int i = 0; i < m.length; i++) {
            temp.add(m[i]);
        }
        for (int i = 0; i < d.length; i++) {
            if (temp.contains(d[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * Method for converting input and checking validity
     * <p>
     *     Int array method that converts the user input into an int array of terms for
     *     the program to process. Alongside this, checks the input for validity and throws the
     *     appropriate exception.
     * </p>
     * @param s User input, the value entered by the user (E.g. 13,4,5,19,20)
     * @return Int Array containing validated terms for further processing by the program.
     */
    private int[] checkValidity(String s){
        s = s.replace(",", " ");
        if(s.trim().equals("")){
            return new int[] {};
        }

        String[] a = s.trim().split(" +");

        int[] t = new int[a.length];
        for(int i = 0; i < t.length; i++) {
            try {
                int temp = Integer.parseInt(a[i]);
                t[i] = temp;
            } catch (Exception e) {
                throw new RuntimeException("Error, Input is invalid. Please Adjust");
            }
        }
        HashSet<Integer> duplicates = new HashSet<>();

        for (int j : t) {
            if (duplicates.contains(j)) {
                throw new RuntimeException("Error, Input is invalid. Please Adjust");
            }
            duplicates.add(j);
        }
        return t;
    }

    /**
     * Method to group terms in prescribed order.
     * <p>
     *     ArrayList method that generates a 2D array and groups the minterms in
     *     accordance with the occurrence of 1s present.
     * </p>
     * @param terms Processed terms now under the Translations class.
     * @return 2D Array containing sorted terms.
     */
    private ArrayList<Translations>[] segregateOnes(Translations[] terms) {
        // Find the maximum number of ones in any term
        int maxOnes = 0;
        for (Translations term : terms) {
            if (term != null && term.getOnes() > maxOnes) {
                maxOnes = term.getOnes();
            }
        }

        // Initialize groups array
        ArrayList<Translations>[] groups = new ArrayList[maxOnes + 1];
        for (int i = 0; i < groups.length; i++) {
            groups[i] = new ArrayList<>();
        }

        // Populate groups array
        for (int i = 0; i < terms.length; i++) {
            Translations term = terms[i];
            if (term != null) {
                int re = term.getOnes();
                groups[re].add(term);
            }
        }

        return groups;
    }

    /**
     * Method for the first portion in implementing QMC
     * <p>
     *     Void method that tracks the untaken, initial, and resultant terms present in iterations.
     *     Loops through the results while its not empty and having a length greater than one.
     *     Ends with the resultant minterms copied into a new array alongside the unused terms.
     * </p>
     */
    public void solveQMC1(){
        //Noting non-usurped terms.
        ArrayList<Translations> clean = new ArrayList<>();

        //Grouping initial terms
        ArrayList<Translations>[] lists = segregateOnes(terms);
        //Store resultant of each iteration
        ArrayList<Translations>[] resultant;

        stepone.add(lists);

        //Loop while results are not empty and length > 1
        boolean used;
        do{
            //Store used terms
            HashSet<String> tekken = new HashSet<>();

            //Set results array to an empty one
            resultant = new ArrayList[lists.length - 1];

            ArrayList<String> temp;
            used = false;

            //Iterate over both groups
            for(int i = 0; i < lists.length-1; i++){
                resultant[i] = new ArrayList<>();

                //Track added terms in resultant to avoid duplicants
                temp = new ArrayList<>();

                //Iterate over each element in group 1 with group 2
                for(int j = 0; j < lists[i].size(); j++){
                    //Loop over group 2
                    for(int k = 0; k < lists[i + 1].size(); k++){
                        //Check for valid combinations
                        if(checkCombo(lists[i].get(j), lists[i + 1].get(k))){
                            //Append terms to used
                            tekken.add(lists[i].get(j).getTerm());
                            tekken.add(lists[i + 1].get(k).getTerm());

                            Translations n = new Translations(lists[i].get(j), lists[i + 1].get(k));

                            //Check if resultant is already in resultant variable, ignore if so.
                            if(!temp.contains(n.getTerm())){
                                resultant[i].add(n);
                                used = true;
                            }
                            temp.add(n.getTerm());
                        }
                    }
                }
            }
            //If resultants is not empty
            if(used){
                //NOTE ORIGINAL LOOP
                for(int i = 0; i < lists.length; i++){
                    for(int j = 0; j < lists[i].size(); j++){
                        if(!tekken.contains(lists[i].get(j).getTerm())){
                            //Add unused terms to untaken
                            clean.add(lists[i].get(j));
                        }
                    }
                }
                lists = resultant;

                //Add results and taken for displaying steps post runtime
                stepone.add(lists);
                taken_step1.add(tekken);
            }
        }while(used && lists.length > 1);

        //Deep copy resultant minterms into new array list along with unused terms.
        finalterms = new ArrayList<>();
        for (int i = 0; i < lists.length; i++) {
            for (int j = 0; j < lists[i].size(); j++) {
                finalterms.add(lists[i].get(j));
            }
        }
        for (int i = 0; i < clean.size(); i++) {
            finalterms.add(clean.get(i));
        }

        solveQMC2();
    }

    /**
     * Method that implements the second half of the QMC method.
     * <p>
     *     Void method that identifies the Prime Implicants, Row Dominance, and Column Dominance.
     *     Calls itself until all minterms have been used or if the Petrick method is necessary.
     * </p>
     */
    public void solveQMC2(){
        populateTable();
        if(!identifyPrimes()){
            if(!dominateRows()){
                if(!dominateColumns()){
                    //If none succeed populate Petricks
                    employPetrick();
                    return;
                }
            }
        }
        //If any minterms remain, call function again
        if(minterms.size() != 0){
            solveQMC2();
        }else{
            //Display steps
            populateTable();
            solution = new ArrayList[1];
            solution[0] = primeimplicants;

        }
    }

    /**
     * Implementation of the Petrick Technique
     * <p>
     *     Void method that constructs the Petrick table in a temporary araay of terms.
     *     It then multiplies these terms and stores them in a solution array, then proceeds to identify
     *     the minimum length of terms as a result of the Petrick method. These terms are then added in the
     *     solution array.
     * </p>
     * <p>
     *     The method performs Petrick's technique and readies the result in a product of sums which is then stored
     *     in the proper arrays.
     * </p>
     */
    void employPetrick(){
        HashSet<String>[] temp = new HashSet[minterms.size()];
        //Construct Petrick table
        for(int i = 0; i < minterms.size(); i++){
            temp[i] = new HashSet<>();
            for(int j = 0; j <finalterms.size(); j++){
                if(finalterms.get(j).getNumbers().contains(minterms.get(i))){
                    char term = (char) ('a' + j);
                    petkeys.add(term + ": " + finalterms.get(j).getTerm());
                    temp[i].add(term + "");
                }
            }
        }

        //Multiply Petrick Terms
        HashSet<String> finalresult = multiply(temp, 0);

        //Display Steps
        HashSet<String>[] step = new HashSet[1];
        step[0] = finalresult;
        stepPetrick(step, 0);
        stepthree.add("\nMin");

        //Identify minimum terms in Petrick's expansion
        int min = -1;
        int count = 0;
        for(Iterator<String> t = finalresult.iterator(); t.hasNext();){
            String m = t.next();
            if(min == -1 || m.length() < min){
                min = m.length();
                count = 1;
            }else if(min == m.length()){
                count++;
            }
        }

        //Add minimum terms in Petrick to solution presentation.
        solution = new ArrayList[count];
        int k = 0; //Iteration variable
        for(Iterator<String> t = finalresult.iterator(); t.hasNext();){
            String c = t.next();
            if(c.length() == min){
                solution[k] = new ArrayList<>();
                stepthree.add(c);
                for(int i = 0; i < c.length(); i++){
                    solution[k].add(finalterms.get((int) c.charAt(i) - 'a').getTerm());
                }
                for(int i = 0; i < primeimplicants.size(); i++){
                    solution[k].add(primeimplicants.get(i));
                }
                k++;
            }
        }
    }

    /**
     * Method to multiply terms
     * <p>
     *     Simple method that multiplies the terms in order to properly represent.
     *     (E.g. ABC * BCD = ABCD)
     * </p>
     * @param m1 First term for multiplication
     * @param m2 Second term for multiplication
     * @return String result of term multiplication
     */
    String mixTerms(String m1, String m2){
        HashSet<Character> r = new HashSet<>();
        for(int i = 0; i < m1.length(); i++){
            r.add(m1.charAt(i));
        }
        for(int i = 0; i < m2.length(); i++){
            r.add(m2.charAt(i));
        }
        String result = "";
        for(Iterator<Character> i = r.iterator(); i.hasNext();){
            result += i.next();
        }
        return result;
    }

    /**
     * Method to display the Petrick process
     * <p>
     *     Void method that displays the result of the Petrick process in
     *     the appropirate form.
     * </p>
     * @param p Hashset representing a term in the Petrick process
     * @param k indexing variable
     */
    void stepPetrick(HashSet<String>[] p, int k) {
        StringBuilder s3 = new StringBuilder();
        for (int i = k; i < p.length; i++) {
            if (p.length != 1)
                s3.append("(");
            for (Iterator<String> g = p[i].iterator(); g.hasNext();) {
                s3.append(g.next());
                if (g.hasNext()) {
                    s3.append(" + ");
                }
            }
            if (p.length != 1)
                s3.append(")");
        }
        stepthree.add(s3.toString());
    }

    /**
     * Method to multiply Petrick expressions.
     * <p>
     *     Recursive Hashset method that multiplies terms in the input set p, and indexing this via the
     *     variable k. It checks for the base case where k >= (p.length-1) and calls the previous method
     *     stepPetrick for processing the terms. After this, it multiplies the terms iteratively in the array
     *     's'. Updates the index variable and does the remainder of the recursive process.
     * </p>
     * @param p Hashset representing a term in the Petrick process
     * @param k indexing variable
     * @return HashSet result.
     */
    HashSet<String> multiply(HashSet<String>[] p, int k){
        if(k >= p.length -1){
            return p[k];
        }

        stepPetrick(p,k);

        HashSet<String> s = new HashSet<>();
        for(Iterator<String> t = p[k].iterator(); t.hasNext();){
            String temp2 = t.next();
            for(Iterator<String> g = p[k + 1].iterator(); g.hasNext();){
                String temp3 = g.next();
                s.add(mixTerms(temp2,temp3));
            }
        }
        p[k + 1] = s;
        return multiply(p, k + 1);
    }

    /**
     * Method to verify column dominance
     * <p>
     *     Boolean method that constructs a table to then populate and then identify and
     *     dominating columns. The method then returns the state of the flag for the other methods to process.
     * </p>
     * @return True - if any dominance was verified; False - if no dominance was verified.
     */
    private boolean dominateColumns(){
        boolean flag = false;
        //Build table
        ArrayList<ArrayList<Integer>> columns = new ArrayList<>();
        for(int i = 0; i < minterms.size(); i++){
            columns.add(new ArrayList<Integer>());
            for(int j = 0; j < finalterms.size(); j++){
                if(finalterms.get(j).getNumbers().contains(minterms.get(i))){
                    columns.get(i).add(j);
                }
            }
        }
        //Identify column dominance and remove them
        for(int i = 0; i < columns.size() - 1; i++){
            for(int j = i + 1; j < columns.size(); j++){
                if(columns.get(j).containsAll(columns.get(i)) && columns.get(j).size() > columns.get(i).size()){
                    columns.remove(j);
                    minterms.remove(j);
                    j--;
                    flag = true;
                }else if(columns.get(i).containsAll(columns.get(j)) && columns.get(i).size() > columns.get(j).size()){
                    columns.remove(i);
                    minterms.remove(i);
                    i--;
                    flag = true;
                    break;
                }
            }

        }
        return flag;
    }

    /**
     * Method to verify row dominance
     * <p>
     *     Boolean method that constructs a table to then populate and then identify and
     *     dominating rows. The method then returns the state of the flag for the other methods to process.
     * </p>
     * @return True - if any dominance was verified; False - if no dominance was verified.
     */
    private boolean dominateRows(){
        boolean flag = false;
        //Identify row dominance and delete them
        for(int i = 0; i < finalterms.size() - 1; i++){
            for(int j = 1 +1; j < finalterms.size(); j++){
                if(contains(finalterms.get(i), finalterms.get(j))){
                    finalterms.remove(j);
                    j--;
                    flag = true;
                }else if(contains(finalterms.get(j), finalterms.get(i))){
                    finalterms.remove(i);
                    i--;
                    flag = true;
                    break;
                }
            }
        }
        return flag;
    }

    /**
     * Method to identify Prime Implicants.
     * <p>
     *     Boolean method that iterates over a generated array and roots out any Prime Implicants.
     *     It then cross-checks them with the representative minterms and updates the flag variable
     *     accordingly. Upon identifying Prime Implicants it updates its list and removes the correspondent
     *     term from the final term of the solution.
     * </p>
     * @return True - if any primes were verified and processed; False - if no primes were found.
     */
    private boolean identifyPrimes(){
        ArrayList<Integer>[] cols = new ArrayList[minterms.size()];
        for (int i = 0; i < minterms.size(); i++) {
            cols[i] = new ArrayList();
            for (int j = 0; j < finalterms.size(); j++) {
                if (finalterms.get(j).getNumbers().contains(minterms.get(i))) {
                    cols[i].add(j);
                }
            }
        }
        boolean flag = false;
        for (int i = 0; i < minterms.size(); i++) {
            if (cols[i].size() == 1) {
                flag = true;
                ArrayList<Integer> del = finalterms.get(cols[i].get(0)).getNumbers();

                for (int j = 0; j < minterms.size(); j++) {
                    if (del.contains(minterms.get(j))) {
                        dontcares.add(minterms.get(j));
                        minterms.remove(j);
                        j--;
                    }
                }
                // for displaying steps
                steptwo.get(steptwo.size() - 1)[cols[i].get(0).intValue() + 1][0] = "T";

                primeimplicants.add(finalterms.get(cols[i].get(0)).getTerm());
                finalterms.remove(cols[i].get(0).intValue());
                break;
            }
        }

        return flag;
    }

    /**
     * Method to display solving steps
     * <p>
     *     Void method that displays the available steps in the solution for the user's
     *     reference if they so desire.
     * </p>
     */
    void populateTable(){
        String[][] table = new String[finalterms.size() +1 ][minterms.size() + maxlen +1];
        for(int i = 0; i < maxlen; i++){
            table[0][i + 1] = String.valueOf((char) ('A' + i));
        }

        for(int i = maxlen; i < minterms.size() + maxlen; i++){
            table[0][i + 1] = String.valueOf(minterms.get(i - maxlen));
        }

        for(int i = 1; i < finalterms.size() + 1; i++){
            for(int j = 0; j < maxlen; j++){
                table[i][j + 1] = String.valueOf(finalterms.get(i - 1).getTerm().charAt(j));
            }
        }

        for(int i = 1; i < finalterms.size() + 1; i++){
            for(int j = maxlen; j < minterms.size() + maxlen; j++){
                if(finalterms.get(i - 1).getNumbers().contains(minterms.get(j - maxlen))){
                    table[i][j + 1] = "X";
                }else {
                    table[i][j + 1] = " ";
                }
            }
        }

        for(int i = 0; i < finalterms.size() + 1; i++){
            table[i][0] = " ";
        }

        steptwo.add(table);
    }

    /**
     * Method that checks if two terms are a valid combination.
     * @param t1 First Term
     * @param t2 Second Term
     * @return True - if a combination of the terms is possible; False - if no combinations are possible.
     */
    boolean checkCombo(Translations t1, Translations t2){
        //If T1 != T2 return false
        if(t1.getTerm().length() != t2.getTerm().length()){
            return false;
        }

        //If there is a dash mismatch, return false
        int k = 0;
        for(int i = 0; i < t1.getTerm().length(); i++){
            if (t1.getTerm().charAt(i) == '-' && t2.getTerm().charAt(i) != '-')
                return false;
            else if (t1.getTerm().charAt(i) != '-' && t2.getTerm().charAt(i) == '-')
                return false;
            else if (t1.getTerm().charAt(i) != t2.getTerm().charAt(i))
                k++;
            else
                continue;
        }

        if(k != 1){
            return false;
        }else{
            return true;
        }
    }

    /**
     * Method that compares two terms.
     * <p>
     *     Boolean method that checks if the two parameters contain the same minterms.
     * </p>
     * @param t1 First Term
     * @param t2 Second Term
     * @return True - if parameters have the same minterms; False - if otherwise.
     */
    boolean contains(Translations t1, Translations t2){
        if (t1.getNumbers().size() <= t2.getNumbers().size()) {
            return false;
        }
        ArrayList<Integer> a = t1.getNumbers();
        ArrayList<Integer> b = t2.getNumbers();
        b.removeAll(dontcares);

        if (a.containsAll(b))
            return true;
        else
            return false;
    }

    /**
     * Method to translate to Symbolic notation
     * <p>
     *     String method that takes an input strieng and translates it
     *     to the notation form (-01- = B'C) and returns this.
     * </p>
     * @param s Input notation
     * @return Translated string value.
     */
    String toSymbolic(String s){
        StringBuilder r = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '-') {
                continue;
            } else if (s.charAt(i) == '1') {
                r.append((char) ('A' + i));
            } else {
                r.append((char) ('A' + i));
                r.append('\'');
            }
        }
        if (r.toString().isEmpty()) {
            r.append("1");
        }
        return r.toString();
    }

    /**
     * Method to print the final result in the appropriate form.
     * @return StringBuilder containing the resultant function of the QMC Method.
     */
    public String printResults() {
        StringBuilder resultBuilder = new StringBuilder();

        for (int i = 0; i < solution.length; i++) {
            resultBuilder.append("Solution no. ").append(i + 1).append(":\n");
            resultBuilder.append("(");
            for (int j = 0; j < solution[i].size(); j++) {
                resultBuilder.append(toSymbolic(solution[i].get(j)));
                if (j != solution[i].size() - 1) {
                    resultBuilder.append(" + ");
                }
            }
            resultBuilder.append(")\n\n");
        }
        return resultBuilder.toString();
    }
}
