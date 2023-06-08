package lk.jise.liveChatApp.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;

import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientController implements Initializable {

    public AnchorPane logInPane;
    public Label UserName;
    public Button btnLogIn;
    public ImageView imgUser;
    public Button btnChoosePhotos;
    public VBox vBox;

    public Label lblUserName;
    public TextField txtUserName;
    public VBox emoji;
    public Label lblMsgError;
    public Label lblError;
    @FXML
    private AnchorPane pane;

    @FXML
    private TextField txtMsg;

    @FXML
    private Button btnSend;
    Socket socket;
    DataInputStream dataInputStream;
    DataOutputStream dataOutputStream;
    String msg = "";


    public void btnSendOnAction(ActionEvent actionEvent) throws IOException {
        if (!txtMsg.getText().equals("") && !txtMsg.getText().equals(" ") && !txtMsg.getText().equals(null)){
            vBox.setAlignment(Pos.BOTTOM_RIGHT);
            Text text = new Text("Me : " + txtMsg.getText());
            text.setTextAlignment(TextAlignment.RIGHT);
            HBox hBox = new HBox(10);
            hBox.setAlignment(Pos.BOTTOM_LEFT);
            hBox.setAlignment(Pos.CENTER_RIGHT);
            hBox.getChildren().add(text);


            Platform.runLater(() -> vBox.getChildren().addAll(hBox));

            dataOutputStream.writeUTF(txtMsg.getText().trim());
            dataOutputStream.flush();
            txtMsg.setText("");
        }
    }

    ArrayList labels = new ArrayList();

    public void setEmoji() {
        String[] emojis = new String[]{
                "\uD83D\uDE00", "\uD83D\uDE03", "\uD83D\uDE04", "\uD83D\uDE01", "\uD83D\uDE06", "\uD83E\uDD23",
                "\uD83D\uDE02", "\uD83D\uDE42", "\uD83D\uDE43", "\uD83D\uDE09", "\uD83D\uDE0A", "\uD83D\uDE31",
                "\uD83D\uDE07", "\uD83E\uDD70", "\uD83D\uDE0D", "\uD83E\uDD29", "\uD83D\uDE18", "\uD83D\uDE17",
                "\uD83D\uDE0F", "\uD83D\uDE12", "\uD83D\uDE1E", "\uD83D\uDE14", "\uD83D\uDE1F", "\uD83D\uDE15",
                "\uD83D\uDE41", "\uD83D\uDE23", "\uD83D\uDE16", "\uD83D\uDE2B", "\uD83D\uDE29", "\uD83D\uDE22",
                "\uD83D\uDE2D", "\uD83D\uDE2E", "\uD83D\uDE2F", "\uD83D\uDE32", "\uD83D\uDE33", "\uD83D\uDE35",
                "\uD83D\uDE28", "\uD83D\uDE30", "\uD83D\uDE25", "\uD83D\uDE34", "\uD83D\uDE37", "\uD83E\uDD12",
                "\uD83E\uDD15", "\uD83E\uDD22", "\uD83E\uDD2E", "\uD83E\uDD27", "\uD83E\uDD75", "\uD83E\uDD76",
                "\uD83E\uDD74", "\uD83E\uDD2F", "\uD83E\uDD1F", "\uD83D\uDE20", "\uD83D\uDE21", "\uD83E\uDD2C",
                "\uD83D\uDE08", "\uD83D\uDC7F", "\uD83D\uDC80", "\uD83D\uDCA9", "\uD83E\uDD21", "\uD83D\uDC79",
                "\uD83D\uDC7A"};
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
                vBox.setAlignment(Pos.BOTTOM_RIGHT); // Align VBox content to the right
                Text text = new Text("Me : " + finalLabel.getText());
                Label labelSender = new Label();
                Label labelMsg = new Label();
                labelMsg.setStyle("-fx-font-size: " + fontSize + "px;");
                labelSender.setText("Me : ");
                labelMsg.setText(finalLabel.getText());
                text.setTextAlignment(TextAlignment.RIGHT); // Align text to the right
                HBox hBox = new HBox(10);
                hBox.setAlignment(Pos.BOTTOM_LEFT);
                hBox.setAlignment(Pos.CENTER_RIGHT);
                hBox.getChildren().add(labelSender);
                hBox.getChildren().add(labelMsg);
                Platform.runLater(() -> vBox.getChildren().addAll(hBox));
                try {
                    dataOutputStream.writeUTF("emj " + finalLabel.getText());
                    dataOutputStream.flush();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            });
            labels.add(label);
           addEmojis();
        }

    }

    public void addEmojis() {
        HBox box1 = new HBox();
        HBox box2 = new HBox();
        HBox box3 = new HBox();
        for (int j = 0; j <labels.size(); j++) {
            if (j<=15){
                box1.getChildren().add((Node) labels.get(j));
            }else if (j<=30){
                box2.getChildren().add((Node) labels.get(j));
            }else if (j<=45){
                box3.getChildren().add((Node) labels.get(j));
            }

        }
//            emoji.setAlignment(Pos.TOP_LEFT);
        emoji.getChildren().addAll(box1,box2,box3);

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
                    } else if (substrings[2].equals("emj")) {
                        vBox.setAlignment(Pos.BOTTOM_RIGHT); // Align VBox content to the right
                        text = new Text();
                        Label labelSender = new Label();
                        Label labelMsg = new Label();
                        labelMsg.setStyle("-fx-font-size: 28px;");
                        labelSender.setText(substrings[0] + substrings[1]);
                        labelMsg.setText(substrings[3]);
                        text.setTextAlignment(TextAlignment.LEFT); // Align text to the right
                        HBox hBox = new HBox(10);
                        hBox.setAlignment(Pos.BOTTOM_LEFT);
                        hBox.setAlignment(Pos.CENTER_LEFT);
                        hBox.getChildren().add(labelSender);
                        hBox.getChildren().add(labelMsg);
                        Platform.runLater(() -> vBox.getChildren().addAll(hBox));
                    } else {
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
        String pattern = "^[a-zA-Z0-9_]{4,16}$";
        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(txtUserName.getText());


        if (matcher.matches()){
            lblUserName.setText(txtUserName.getText());
            dataOutputStream.writeUTF(txtUserName.getText().trim());
            dataOutputStream.flush();
            logInPane.setVisible(false);
        }else {
            txtUserName.setStyle("-fx-border-color: red;-fx-border-radius: 10;");
            lblError.setText("Invalid userName");
        }
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
            hBox.setAlignment(Pos.BOTTOM_RIGHT);
            vBox.setAlignment(Pos.BOTTOM_LEFT);
            hBox.setAlignment(Pos.CENTER_RIGHT);
            hBox.getChildren().add(imageView);
            Platform.runLater(() -> vBox.getChildren().addAll(hBox));
            dataOutputStream.flush();

        } catch (IOException ignored) {

        }
    }
}
