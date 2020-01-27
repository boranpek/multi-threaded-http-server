import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ConnectionHandler extends Thread{
    ServerSocket server;
    Socket socket;
    BufferedReader buffer;
    FileOutputStream output;
    String documentRoot;
    String tempRoot;
    String input;
    logOfRequests logs;
    int tcpPort;
    String[] token;

    public ConnectionHandler(int tcpPort,BufferedReader buffer,ServerSocket server,Socket socket, String documentRoot, FileOutputStream output,String tempRoot,String input) throws IOException {
        this.tcpPort=tcpPort;
        this.buffer=buffer;
        this.server=server;
        this.socket=socket;
        this.documentRoot=documentRoot;
        this.output=output;
        this.tempRoot = tempRoot;
        this.input=input;
        logs = new logOfRequests(socket,input);
        logs.start();
    }
    public void run() {
        try {
            try{
                token = input.split(" ");
                this.documentRoot = tempRoot + token[1];
                File f = new File(this.documentRoot);
                boolean exists = f.exists();
                if (!exists){
                    buffer.close();
                    socket.close();
                    this.output.close();
                    f=new File(this.tempRoot+"/index.html");
                    System.out.println("File does not exist!");
                }
                if (token[1].equals("/"))
                    f=new File(this.tempRoot+"/index.html");
                Path p = f.toPath();
                byte[] bytes = Files.readAllBytes(p);
                this.documentRoot = this.tempRoot;
                String response = "HTTP/0.9 200 OK\r\n\r\n";
                byte[] bytesResponse = response.getBytes();
                this.output.write(bytesResponse);
                this.output.write(bytes);
                this.output.flush();
            }
            catch (SocketException e){
                this.output.close();
            }

        }
        catch (IOException e){
            e.printStackTrace();
            try {
                this.output.close();
                this.socket.close();
                this.buffer.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        }
    }

}
