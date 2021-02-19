package inf112.skeleton.app;

/**
 * A class that is used to send a request from server to client
 */
public class requestToClient {

    private String requestType; // Holds the type of request e.g. "Move".
    private String requestMessage; // Holds a message that comes with the request, optional field


    public void setRequestMessage(String message) {
        this.requestMessage = message;
    }
    public String getRequestMessage() {
        return this.requestMessage;
    }
    public String getRequestType() {
        return this.requestType;
    }

    public void setRequestType(String request) {
        requestType = request;
    }
}
