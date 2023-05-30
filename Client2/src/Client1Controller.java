import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Client1Controller implements Initializable {

    public AnchorPane logInPane;
    public Label UserName;
    public TextField txtUserName;
    public Button btnLogIn;
    public ImageView imgUser;
    public Button btnChoosePhotos;
    public VBox vBox;
    public HBox emoji;
    public Label lblName;
    @FXML
    private AnchorPane pane;

    @FXML
    private TextField txtMsg;

    @FXML
    private Button btnSend;
    Socket socket;
    DataInputStream dataInputStream;
    DataOutputStream dataOutputStream;
    String msg = "", imageName;

    public void btnSendOnAction(ActionEvent actionEvent) throws IOException {
        vBox.setAlignment(Pos.BOTTOM_RIGHT); // Align VBox content to the right
        Text text = new Text("Me : " + txtMsg.getText());
        text.setTextAlignment(TextAlignment.RIGHT); // Align text to the right
        HBox hBox = new HBox(10);
        hBox.setAlignment(Pos.BOTTOM_LEFT);
        hBox.setAlignment(Pos.CENTER_RIGHT);
        hBox.getChildren().add(text);


        Platform.runLater(() -> vBox.getChildren().addAll(hBox));

        dataOutputStream.writeUTF(txtMsg.getText().trim());
        dataOutputStream.flush();
        txtMsg.setText("");
    }

    public void setEmoji() {
        String[] emojis = new String[]{"\uD83D\uDE00", "\uD83D\uDE03", "\uD83D\uDC4B", "\uD83E\uDD1A", "\uD83D\uDE04", "\uD83D\uDE01", "\uD83D\uDE06"};
        Label label;
        String y;
        for (int i = 0; i < emojis.length; i++) {
            label = new Label();
            y = emojis[i];
            int fontSize = 28;
            label.setStyle("-fx-font-size: " + fontSize + "px;");
            label.setText(y);
            Label finalLabel = label;
            label.setOnMouseClicked(e -> {
                System.out.println(finalLabel.getText());
                vBox.setAlignment(Pos.BOTTOM_RIGHT); // Align VBox content to the right
                Text text = new Text("Me : " + finalLabel.getText());
                text.setTextAlignment(TextAlignment.RIGHT); // Align text to the right
                HBox hBox = new HBox(10);
                hBox.setAlignment(Pos.BOTTOM_LEFT);
                hBox.setAlignment(Pos.CENTER_RIGHT);
                hBox.getChildren().add(text);
                Platform.runLater(() -> vBox.getChildren().addAll(hBox));
                try {
                    dataOutputStream.writeUTF(txtMsg.getText().trim());
                    dataOutputStream.flush();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            });
            emoji.setAlignment(Pos.CENTER_LEFT);
            emoji.getChildren().add(label);
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        /*
         */
/* label.setOnMouseClicked(e -> {
            System.out.println("Label clicked!");
        });

        label = new Label();
        String y = "\uD83D\uDC4B \uD83D\uDE03 \uD83D\uDC4B \uD83E\uDD1A";
        label.setFont(Font.font("Aller Light", 28));
        label.setText(y);
*//*

        // Create a VBox container
//        VBox box = new VBox();
//        box.setAlignment(Pos.CENTER_LEFT);
*/

        setEmoji();
        new Thread(() -> {
            try {
                socket = new Socket("localhost", 5000);
                dataInputStream = new DataInputStream(socket.getInputStream());
                dataOutputStream = new DataOutputStream(socket.getOutputStream());

                while (!msg.equals("exit")) {
                    msg = dataInputStream.readUTF();
                    Text text = null;
                    String[] substrings = msg.split(" ");
                    if (substrings[2].equals("img")) {
                        String newMsg = (substrings[3]);
                       /* if (substrings.length>2) {
                            for (int i = 2; i < substrings.length; i++) {
                                newMsg=newMsg+" "+substrings[i];
                            }
                        }*/
                        System.out.println(substrings[0]);
                        File file = new File(newMsg);
                        Image image = new Image(file.toURI().toString());

                        ImageView imageView = new ImageView(image);

                        imageView.setFitWidth(70);
                        imageView.setFitHeight(70);

                        HBox hBox = new HBox(10);
                        hBox.setAlignment(Pos.BOTTOM_RIGHT);
                        vBox.setAlignment(Pos.BOTTOM_LEFT);
                        hBox.setAlignment(Pos.CENTER_LEFT);
                        hBox.getChildren().add(imageView);

                        Platform.runLater(() -> vBox.getChildren().addAll(hBox));
                    } else {
//                        vBox.setAlignment(Pos.BOTTOM_LEFT);
                        text = new Text(msg);
                        text.setTextAlignment(TextAlignment.LEFT);
                        HBox hBox = new HBox(10);
                        hBox.setAlignment(Pos.BOTTOM_RIGHT);
                        vBox.setAlignment(Pos.BOTTOM_LEFT);
                        hBox.setAlignment(Pos.CENTER_LEFT);
                        hBox.getChildren().add(text);

                        Platform.runLater(() -> vBox.getChildren().addAll(hBox));

                    }


                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void btnLogInOnAction(ActionEvent actionEvent) throws IOException {
        dataOutputStream.writeUTF(txtUserName.getText().trim());
        dataOutputStream.flush();
        logInPane.setVisible(false);
    }

    public void btnChoosePhotosOnAction(ActionEvent actionEvent) {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Image File");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
            File selectedFile = fileChooser.showOpenDialog(null);
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataOutputStream.writeUTF("img " + selectedFile.getPath());


            File file = new File(selectedFile.getPath());
            Image image = new Image(file.toURI().toString());
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(70);
            imageView.setFitHeight(70);
            HBox hBox = new HBox(10);
            hBox.setAlignment(Pos.BOTTOM_LEFT);
            vBox.setAlignment(Pos.TOP_LEFT);
            hBox.setAlignment(Pos.CENTER_RIGHT);
            hBox.getChildren().add(imageView);

            Platform.runLater(() -> vBox.getChildren().addAll(hBox));
            dataOutputStream.flush();
        } catch (IOException ignored) {

        }
    }
}
