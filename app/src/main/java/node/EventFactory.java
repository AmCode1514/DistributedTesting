package node;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import node.requests.*;

public class EventFactory {

    private EventFactory() {
    }

    //Unmarshall message into basic event, this standardizes message format and is extensible through the subClassEvent method.
    //The goal is to separate protocols which happen at every step from specialized functions.
    public Event generateEvent(byte[] data) {
        int requestType;
        String ipAddress;
        int portNumber;
        byte[] remainingData;

        ByteArrayInputStream baInputStream = new ByteArrayInputStream(data);
        DataInputStream din = new DataInputStream(new BufferedInputStream(baInputStream));

        try{
        requestType = din.readInt();
        int ipLength = din.readInt();
        byte[] byteAddress = new byte[ipLength];
        din.readFully(byteAddress);
        ipAddress = new String(byteAddress);
        portNumber = din.readInt();
        remainingData = din.readAllBytes();
        System.out.println("factory request requestType:" + requestType);
        System.out.println("factory request ip:" + ipAddress);
        System.out.println("factory request port:" + portNumber);
        System.out.println("factory request remainingData size:" + remainingData.length);
        din.close();
        return subClassEvent(requestType, ipAddress, portNumber, remainingData);
        }
        catch(IOException e) {
            System.out.println("byte array malformed or non-existent. Failed to generate basic event");
            e.printStackTrace();
            return null;
        }
    }
    private Event subClassEvent(int requestType, String ipAddress, int portNumber, byte[] remainingData) {
        switch(requestType) {
            case 1: return new RegistrationRequest(requestType, ipAddress, portNumber);
            case 2: return new RegistrationResponse(requestType, ipAddress, portNumber, remainingData);
            case 3: return new MessagingNodesListRequest(requestType, ipAddress, portNumber, remainingData);
        }
        return null;
    }
    public static EventFactory getInstance() {
        return new EventFactory();
    }
}
