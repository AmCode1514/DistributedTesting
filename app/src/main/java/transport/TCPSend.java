package transport;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;

import node.Event;
import transport.Connection;

public class TCPSend {
    public TCPSend() {
        
    }
    public synchronized boolean sendEvent(Event eventToSend, Connection tcpConnection) {

        byte[] payload = eventToSend.reMarshallToBasic();
        System.out.println("TCPSEND closed connection status: " + tcpConnection.getConnectionSocket().isClosed());
        try {
            DataOutputStream dout = tcpConnection.getOutputStream();
            dout.writeInt(payload.length);
            dout.write(payload);
            dout.flush();
            return true;
        }
        catch(UnknownHostException e) {
            System.out.println("Host not found");
            return false;
        }
        catch(IOException e) {
            System.out.println("Socket unexepectedly closed, this could be the result of a connection failure or interrupt");
            e.printStackTrace();
            return false;
        }
    }
}
