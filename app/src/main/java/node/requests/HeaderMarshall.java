package node.requests;

import java.io.DataOutputStream;
import java.io.IOException;

import javax.sql.DataSource;

public class HeaderMarshall {

    public static void generateRequestHeaderByteArray(DataOutputStream dout,int requestType, String ipAddress, int portNumber) {
        try {
        dout.writeInt(requestType); //registration request is 1
        byte[] ipAddressBytes = ipAddress.getBytes();
        int ipLength = ipAddressBytes.length;
        dout.writeInt(ipLength);
        dout.write(ipAddressBytes);
        dout.writeInt(portNumber);
        dout.flush();
        }
        catch (IOException e) {
            System.out.println("Issue converting registration request into byte array");
            e.printStackTrace();
        }
    }
}
