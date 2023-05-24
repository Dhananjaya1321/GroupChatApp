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
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
            new Thread(() -> {
                try {
                    txtArea.appendText("Server Started!");
//                    socket1 = serverSocket.accept();
                    System.out.println(socket1.getPort());
                    txtArea.appendText("client  1");

                    dataInputStream1 = new DataInputStream(socket1.getInputStream());
                    dataOutputStream1 = new DataOutputStream(socket1.getOutputStream());


                    while (!msg1.equals("exit")) {
                      /*  msg1 = dataInputStream1.readUTF();
                        txtArea.appendText("\n 1" + msg1);*/
                        if (n1.equals("0")) {
                            n1 = dataInputStream1.readUTF();
                            System.out.println(n1);
                        } else {
                            dataOutputStream2.writeUTF(dataInputStream1.readUTF());
                            dataOutputStream2.flush();
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
            new Thread(() -> {
                try {
                    txtArea.appendText("Server Started!");
//                    socket2 = serverSocket.accept();
                    txtArea.appendText("client  2");
                    System.out.println(socket1.getPort());
                    dataInputStream2 = new DataInputStream(socket1.getInputStream());
                    dataOutputStream2 = new DataOutputStream(socket1.getOutputStream());


                    while (!msg2.equals("exit")) {
                        /*msg2 = dataInputStream2.readUTF();
                        txtArea.appendText("\n 2" + msg2);*/
                        if (n2.equals("0")) {
                            n2 = dataInputStream2.readUTF();
                        } else {
                            dataOutputStream1.writeUTF(dataInputStream2.readUTF());
                            dataOutputStream1.flush();
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
