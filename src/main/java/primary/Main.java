package primary;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

/**
 * Main Class for full implementation of the QMC Simulator
 * <p>
 *     Culmination of the QMCDriver package and the QMC Controller,
 *     contains only the Stage setup and call of the controller.
 * </p>
 */
public class Main extends Application {
    /**
     * Main Method set up by Java when initializing a new JavaFX application.
     * @param args Used to launch UI
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Void method to start the Controller
     * @param primaryStage Window user will interact with
     * @throws IOException Thrown when the relevant FXML file is not found.
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        URL fxmlUrl = getClass().getResource("/view/QMC.fxml");
        System.out.println("URL" + fxmlUrl);
        if (fxmlUrl == null) {
            throw new IOException("FXML file not found");
        }
        Parent root = FXMLLoader.load(fxmlUrl);

        Scene scene = new Scene(root);
        primaryStage.setTitle("Rodriguez | Tan QMC Simulator");
        Image appicon = new Image(getClass().getResourceAsStream("/imageassets/app_icon.png"));
        //Image appicon = new Image("/imageassets/app_icon.png");
        primaryStage.getIcons().add(appicon);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
