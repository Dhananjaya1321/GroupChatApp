import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class ConnectUser {
    Socket socket;
    DataInputStream dataInputStream;
    DataOutputStream dataOutputStream;
    String msg = "";
    ArrayList<ManageClient> manageClients;

    public ConnectUser(Socket socket, DataInputStream dataInputStream, DataOutputStream dataOutputStream, ArrayList<ManageClient> manageClients) {
        this.socket = socket;
        this.dataInputStream = dataInputStream;
        this.dataOutputStream = dataOutputStream;
        this.manageClients = manageClients;
        startThread();
    }

    public ConnectUser() {
    }

    public void startThread() {
        new Thread(() -> {
            try {
                dataInputStream = new DataInputStream(socket.getInputStream());
                dataOutputStream = new DataOutputStream(socket.getOutputStream());
                while (!msg.equals("exit")) {
                    for (ManageClient m : manageClients) {
                        if (m.socket.getPort() != socket.getPort()) {
                            try {
                                m.dataOutputStream.writeUTF(m.dataInputStream.readUTF());
                                m.dataOutputStream.flush();
                            } catch (Exception e) {
                            }
                        }
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
