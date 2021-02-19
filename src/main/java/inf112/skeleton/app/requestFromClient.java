package inf112.skeleton.app;

public class requestFromClient {

    private String requestType;
    private String requestMessage;


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
