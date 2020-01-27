import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class logOfRequests extends Thread {
    String input;
    SimpleDateFormat format;
    Date date;
    String[] token;
    Socket socket;

    public logOfRequests (Socket socket, String input){
        this.socket=socket;
        this.input=input;
        format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        date = new Date();
        token = input.split(" ");
    }
    public void run(){
        while(true){
            System.out.println(format.format(date)+"   "+ token[0]+ token[1]+" "+ socket.getLocalSocketAddress());
            try{
                Thread.sleep(5000);
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

}
