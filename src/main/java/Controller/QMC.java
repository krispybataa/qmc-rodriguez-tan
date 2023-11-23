package Controller;
import QMCDriver.CompuDriver;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.text.TextAlignment;
import java.awt.*;

/**
 * Controller class for the UI implementation
 * <p>
 *     Contains relevant methods and variables for the UI
 *     of the Quine McCluskey simulator/
 * </p>
 * @author Clark Rodriguez
 * @author Jamilene Tan
 */
public class QMC{
    /**
     * Relevant instance variables with @FXML tag where necessary
     */
    @FXML
    private Label welcomeText, minlabel, dcprompt, dcreprompt;
    @FXML
    private TextField mininput, dcInput;
    @FXML
    private TextArea longans;
    @FXML
    private Button start, dcbuttonYes, dcbuttonNo, solve, exit, reset;
    private boolean dontcare;
    private String minttext, donttext, emptydont;
    private CompuDriver qmccore;

    /**
     * Method to initialize program UI
     */
    @FXML
    public void initialize() {
        // Set up initial visibility
        minlabel.setVisible(false);
        mininput.setVisible(false);
        dcprompt.setVisible(false);
        dcbuttonYes.setVisible(false);
        dcbuttonNo.setVisible(false);
        dcInput.setVisible(false);
        solve.setVisible(false);
        dcreprompt.setVisible(false);
        longans.setVisible(false);
        longans.setWrapText(true);
        exit.setVisible(false);
        reset.setVisible(false);

        welcomeText.setTextAlignment(TextAlignment.CENTER);

        start.setOnAction(event -> handleStartButton(welcomeText));

        mininput.setOnAction(event -> handleMinInput());

        dcInput.setOnAction(event-> handleDCInput());

        dcbuttonYes.setOnAction(event -> handleDcButtonYes());

        dcbuttonNo.setOnAction(event -> handleDcButtonNo());

        solve.setOnAction(event -> handleSolveButton());

        exit.setOnAction(event -> handleExitButton());

        reset.setOnAction(event -> handleResetButton());
    }

    /**
     * Method to handle welcoming the user
     * @param welcomeText Greeting Text
     */
    @FXML
    private void handleStartButton(Label welcomeText) {
        welcomeText.setText("");
        start.setVisible(false);
        minlabel.setVisible(true);
        mininput.setVisible(true);
    }

    /**
     * Void method to handle user minterm input.
     */
    @FXML
    private void handleMinInput() {
        // Update visibility based on mininput
        minttext = mininput.getText();
        dcprompt.setVisible(true);
        dcbuttonYes.setVisible(true);
        dcbuttonNo.setVisible(true);
    }

    /**
     * Void method to handle the interaction with the 'Yes' Button
     */
    @FXML
    private void handleDcButtonYes() {
        // Update visibility based on dcbuttonYes
        dontcare = true;
        dcbuttonNo.setVisible(false);
        dcInput.setVisible(true);
        dcreprompt.setVisible(true);
    }

    /**
     * Void method to handle user Don't Care input
     */
    @FXML
    private void handleDCInput(){
        solve.setVisible(true);
        donttext = dcInput.getText();
    }

    /**
     * Void method to handle interaction with the 'No' Button.
     */
    @FXML
    private void handleDcButtonNo() {
        // Update visibility based on dcbuttonNo
        dontcare = false;
        dcInput.setVisible(false);
        dcbuttonYes.setVisible(false);
        solve.setVisible(true);
        emptydont = " ";
    }

    /**
     * Void method to handle interaction with the 'Solve' Button.
     */
    @FXML
    private void handleSolveButton(){
        if(dontcare){
            qmccore = new CompuDriver(minttext, donttext);
        }else{
            qmccore = new CompuDriver(minttext, emptydont);
        }

        qmccore.solveQMC1();

        solve.setVisible(false);
        longans.setVisible(true);
        longans.setText(qmccore.printResults());
        exit.setVisible(true);
        reset.setVisible(true);

    }

    /**
     * Void method to handle interaction with the 'Exit' Button.
     */
    @FXML
    private void handleExitButton(){
        System.exit(0);
    }

    /**
     * Void method to handle interaction with the 'Reset' Button
     */
    @FXML
    private void handleResetButton(){
        mininput.clear();
        dcInput.clear();
        dcprompt.setVisible(false);
        dcbuttonYes.setVisible(false);
        dcbuttonNo.setVisible(false);
        dcInput.setVisible(false);
        solve.setVisible(false);
        dcreprompt.setVisible(false);
        longans.setVisible(false);
        exit.setVisible(false);
        reset.setVisible(false);
    }
}
