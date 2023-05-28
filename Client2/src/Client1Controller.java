import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.nio.file.Files;
import java.util.ResourceBundle;

public class Client1Controller implements Initializable {

    public AnchorPane logInPane;
    public Label UserName;
    public TextField txtUserName;
    public Button btnLogIn;
    public ImageView imgUser;
    public Button btnChoosePhotos;
    public VBox vBox;
    @FXML
    private AnchorPane pane;

    @FXML
    private TextField txtMsg;

    @FXML
    private Button btnSend;
    Socket socket;
    DataInputStream dataInputStream;
    DataOutputStream dataOutputStream;
    String msg = "",imageName;

    public void btnSendOnAction(ActionEvent actionEvent) throws IOException {
        txtArea.appendText("\nMe : " + txtMsg.getText());
        dataOutputStream.writeUTF(txtMsg.getText().trim());
        dataOutputStream.flush();
        txtMsg.setText("");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        new Thread(() -> {
            try {
                socket = new Socket("localhost", 5000);
                dataInputStream = new DataInputStream(socket.getInputStream());
                dataOutputStream = new DataOutputStream(socket.getOutputStream());

                while (!msg.equals("exit")) {
                    msg = dataInputStream.readUTF();
                    txtArea.appendText("\n" + msg);


                    // Read the image data length
                    int imageDataLength = Integer.parseInt(dataInputStream.readUTF());

                    // Read the image data
                    byte[] imageData = new byte[imageDataLength];
                    dataInputStream.readFully(imageData);

                    // Convert the received data into an image
                    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageData);
                    BufferedImage image = ImageIO.read(byteArrayInputStream);
                    txtArea.appendText(String.valueOf(image));

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
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter(
                "Images", "*.jpg", "*.jpeg", "*.png", "*.gif");

        fileChooser.getExtensionFilters().addAll(extFilterPNG);
        File file = fileChooser.showOpenDialog(null);
        imageName = file.getAbsolutePath();
        System.out.println(imageName);
        try {
            File imageFile = new File(imageName);
            byte[] imageData = Files.readAllBytes(imageFile.toPath());

            // Create a DataOutputStream to send data
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

            // Send the image data length
            dataOutputStream.writeInt(imageData.length);

            // Send the image data
            dataOutputStream.writeUTF(String.valueOf(imageData));


        } catch (IOException ignored) {

        }


    }
}
