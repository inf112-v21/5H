package inf112.skeleton.app;

public class requestFromClient {

    private final String requestType;
    private String requestMessage;

    public requestFromClient(String requestType) {
        this.requestType = requestType;
    }

    public void setRequestMessage(String message) {
        this.requestMessage = message;
    }
    public String getRequestMessage() {
        return this.requestMessage;
    }
    public String getRequestType() {
        return this.requestType;
    }
}
