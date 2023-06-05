package lk.jise.liveChatApp.controller;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class ClientHandler extends Thread {

    String msg = "", name = "0";
    private ArrayList<ClientHandler> clients;
    private Socket socket;
    DataInputStream dataInputStream;
    DataOutputStream dataOutputStream;


    public ClientHandler(Socket socket, ArrayList<ClientHandler> clients) {
        try {
            this.socket = socket;
            this.clients = clients;
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {

            while (!msg.equals("exit")) {
                msg = dataInputStream.readUTF();
                if (name.equals("0")) {
                    name = msg;
                    System.out.println(name);
                } else {
                    for (ClientHandler cl : clients) {
                        if (cl.socket.getPort() != socket.getPort()) {
                            System.out.println(name + " : " + msg);
                            cl.dataOutputStream.writeUTF(name + " : " + msg);
                            dataOutputStream.flush();
                        }
                    }
                }
            }
        } catch (SocketException ignore) {
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                dataOutputStream.close();
                dataInputStream.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
