import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class ManageClient {
    Socket socket;
    DataInputStream dataInputStream;
    DataOutputStream dataOutputStream;

    public ManageClient(Socket socket, DataInputStream dataInputStream, DataOutputStream dataOutputStream) {
        this.socket = socket;
        this.dataInputStream = dataInputStream;
        this.dataOutputStream = dataOutputStream;
    }
}
