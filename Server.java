import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.NoSuchFileException;
import java.util.Scanner;

public class Server {
    int tcpPort;
    String documentRoot;
    ServerSocket server;
    BufferedReader buffer;
    FileOutputStream output;
    Socket socket;

    public Server(int tcpPort, String documentRoot) throws IOException {
        this.documentRoot = documentRoot;
        this.tcpPort = tcpPort;
    }
    public void runServer() throws IOException {
        this.server = new ServerSocket(tcpPort);
        socket=new Socket();
        System.out.println("Server is up!");

        while (true){
            try {
                try {
                    try {
                        socket = server.accept();
                        System.out.println("Connected");
                        buffer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        output = (FileOutputStream) socket.getOutputStream();
                        String input = buffer.readLine();
                        String tempRoot = this.documentRoot;
                        new Thread(new ConnectionHandler(tcpPort,buffer,server,socket, documentRoot, output, tempRoot, input)).start();
                    }catch (NoSuchFileException e){
                        e.printStackTrace();
                    }
                }catch (NullPointerException e){
                    e.printStackTrace();
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("TCP Port: ");
        int tcpPort = scanner.nextInt();
        System.out.print("Document Root: ");
        String documentRoot = scanner.next(); // ../Desktop/a
        Server server = new Server(tcpPort,documentRoot);
        server.runServer();
    }

}
