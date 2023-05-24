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
    Socket socket1;
    DataInputStream dataInputStream1;
    DataOutputStream dataOutputStream1;
    Socket socket2;
    DataInputStream dataInputStream2;
    DataOutputStream dataOutputStream2;
    String msg1 = "";
    String msg2 = "";
    String n1 = "0", n2 = "0";


    public void btnSendOnAction(ActionEvent actionEvent) throws IOException {
        dataOutputStream1.writeUTF(txtMsg.getText().trim());
        dataOutputStream1.flush();
        dataOutputStream2.writeUTF(txtMsg.getText().trim());
        dataOutputStream2.flush();
    }

    Socket socketNew;
    Socket socketOld;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ArrayList<ManageClient> manageClients=new ArrayList<>();
        try {
            ServerSocket serverSocket = new ServerSocket(5000);
            new Thread(() -> {
                try {
                    while (true) {
                        socketOld=socketNew;
                        socketNew = serverSocket.accept();
                        System.out.println(socketNew.getPort());
                        if (socketNew.getPort()!=socketOld.getPort()){
                            socket1=socketNew;
                            dataInputStream1 = new DataInputStream(socket1.getInputStream());
                            dataOutputStream1 = new DataOutputStream(socket1.getOutputStream());
                            ManageClient manageClient=new ManageClient(socket1,dataInputStream1,dataOutputStream1);
                            manageClients.add(manageClient);
                            ConnectUser connectUser=new ConnectUser(socket1,dataInputStream1,dataOutputStream1,manageClients);
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
