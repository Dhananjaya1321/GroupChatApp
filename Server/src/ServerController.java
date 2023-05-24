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
import java.util.ArrayList;
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
    private Socket socket, socketNew, socketOld;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private ArrayList<ManageClient> manageClients = new ArrayList<>();

    public void btnSendOnAction(ActionEvent actionEvent) throws IOException {

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            ServerSocket serverSocket = new ServerSocket(5000);
            new Thread(() -> {
                try {
                    while (true) {
                        socketOld = socketNew;
                        socketNew = serverSocket.accept();
                        System.out.println(socketNew.getPort());
                        if (socketNew.getPort() != socketOld.getPort()) {
                            socket = socketNew;
                            dataInputStream = new DataInputStream(socket.getInputStream());
                            dataOutputStream = new DataOutputStream(socket.getOutputStream());
                            ManageClient manageClient = new ManageClient(socket, dataInputStream, dataOutputStream);
                            manageClients.add(manageClient);
                            ConnectUser connectUser = new ConnectUser(socket, dataInputStream, dataOutputStream, manageClients);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
