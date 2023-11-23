/**
 * Primary project module for the QMC Simulator
 * @author Clark Rodriguez
 * @author Jamilene Tan
 */
module qmc.rodriguez.tan {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    opens Controller to javafx.fxml;
    exports Controller;
    exports primary to javafx.graphics;
}
