package lk.jise.liveChatApp.controller;

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
import java.io.UTFDataFormatException;
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
    Socket socket3;
    DataInputStream dataInputStream3;
    DataOutputStream dataOutputStream3;
    String msg1 = "", msg2 = "", msg3 = "";
    String name1 = "0", name2 = "0", name3 = "0";


    public void btnSendOnAction(ActionEvent actionEvent) throws IOException {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            ServerSocket serverSocket = new ServerSocket(5000);
            new Thread(() -> {
                try {
                    txtArea.appendText("Server Started!");
                    socket1 = serverSocket.accept();
                    txtArea.appendText("client  1");

                    dataInputStream1 = new DataInputStream(socket1.getInputStream());
                    dataOutputStream1 = new DataOutputStream(socket1.getOutputStream());

                    while (!msg1.equals("exit")) {
                        msg1 = dataInputStream1.readUTF();
                        if (name1.equals("0")) {
                            name1 = msg1;
                            System.out.println(name1);
                        } else {
                            System.out.println(name1 + " : " + msg1);
                            dataOutputStream2.writeUTF(name1 + " : " + msg1);
                            dataOutputStream2.flush();
                            dataOutputStream3.writeUTF(name1 + " : " + msg1);
                            dataOutputStream3.flush();
                        }
                    }

                } catch (UTFDataFormatException e){

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
            new Thread(() -> {
                try {
                    txtArea.appendText("Server Started!");
                    socket2 = serverSocket.accept();
                    txtArea.appendText("client  2");

                    dataInputStream2 = new DataInputStream(socket2.getInputStream());
                    dataOutputStream2 = new DataOutputStream(socket2.getOutputStream());

                    while (!msg2.equals("exit")) {
                        msg2 = dataInputStream2.readUTF();
                        if (name2.equals("0")) {
                            name2 = msg2;
                            System.out.println(name2);
                        } else {
                            System.out.println(name2 + " : " + msg2);
                            dataOutputStream1.writeUTF(name2 + " : " + msg2);
                            dataOutputStream1.flush();
                            dataOutputStream3.writeUTF(name2 + " : " + msg2);
                            dataOutputStream3.flush();
                        }
                    }

                } catch (UTFDataFormatException e){

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
            new Thread(() -> {
                try {
                    txtArea.appendText("Server Started!");
                    socket3 = serverSocket.accept();
                    txtArea.appendText("client  3");

                    dataInputStream3 = new DataInputStream(socket3.getInputStream());
                    dataOutputStream3 = new DataOutputStream(socket3.getOutputStream());

                    while (!msg3.equals("exit")) {
                        msg3 = dataInputStream3.readUTF();
                        if (name3.equals("0")) {
                            name3 = msg3;
                            System.out.println(name3);
                        } else {
                            System.out.println(name3 + " : " + msg3);
                            dataOutputStream1.writeUTF(name3 + " : " + msg3);
                            dataOutputStream1.flush();
                            dataOutputStream2.writeUTF(name3 + " : " + msg3);
                            dataOutputStream2.flush();
                        }
                    }

                } catch (UTFDataFormatException e){

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}