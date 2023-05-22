import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class ServerController implements Initializable {

    @FXML
    private AnchorPane pane;

    @FXML
    private TextArea txtArea;

    @FXML
    private TextField txtMsg;

    @FXML
    private Button btnSend;



    public void btnSendOnAction(ActionEvent actionEvent) throws IOException {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
