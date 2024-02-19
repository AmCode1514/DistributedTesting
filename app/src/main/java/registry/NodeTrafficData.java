package registry;

public class NodeTrafficData {
    public final int requestType;
    public final String ipAddress;
    public final int portNumber;

    public final int numberOfMessagesSent;
    public final long summationOfMessagesSent;
    public final int numberOfMessagesReceived;
    public final long summationOfReceivedMessages;
    public final int numberOfMessagesRelayed;

    public NodeTrafficData(int requestType, String ipAddress, int portNumber, int numberOfMessagesSent, long summationOfMessagesSent, int numberOfMessagesReceived, long summationOfReceivedMessages, int numberOfMessagesRelayed) {
        this.requestType = requestType;
        this.ipAddress = ipAddress;
        this.portNumber = portNumber;
        this.numberOfMessagesSent = numberOfMessagesSent;
        this.summationOfMessagesSent = summationOfMessagesSent;
        this.numberOfMessagesReceived = numberOfMessagesReceived;
        this.summationOfReceivedMessages = summationOfReceivedMessages;
        this.numberOfMessagesRelayed = numberOfMessagesRelayed;
    }
}
