import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
public class GetIpAdress extends Thread {

    private DatagramSocket socket;
    private boolean running;
    private byte[] buf = new byte[1024];

    public GetIpAdress() throws Exception {
        socket = new DatagramSocket(9999);

    }
    public String run2()  {
        InetAddress address = null;
        try{
            running = true;
            while (running)  {
                System.out.println("Start");
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                 address = packet.getAddress();
                System.out.println(address);
                socket.send(packet);
                String.valueOf(address);
                return String.valueOf(address);
            }
            socket.close();
        }catch(IOException e)
        {
            System.out.println(e);
        }
        return String.valueOf(address);
    }
}