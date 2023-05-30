import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class d extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Create a VBox container
        VBox box = new VBox();
        box.setAlignment(Pos.CENTER_LEFT);

        // Create a Label
        Label label = new Label();
        String y = "\uD83D\uDC4B \uD83D\uDE03 \uD83D\uDC4B \uD83E\uDD1A";
        int fontSize = 28;
        label.setStyle("-fx-font-size: " + fontSize + "px;");
        label.setText(y);

        // Create a mouse listener for the Label
        label.setOnMouseClicked(e -> {
            System.out.println("Label clicked!");
        });

        // Add the Label to the VBox
        box.getChildren().add(label);

        // Create a Scene with the VBox as the root node
        Scene scene = new Scene(box, 300, 200);

        // Set the Scene and show the Stage
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
