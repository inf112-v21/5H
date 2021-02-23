package inf112.skeleton.app.net;

/**
 * A class that is used to send a request from server to client
 */
public class RequestToClient {

    private String requestType; // Holds the type of request e.g. "Move".
    private Object requestData; // Holds a message that comes with the request, optional field


    public void setRequestMessage(Object data) {
        requestData = data;
    }
    public Object getRequestData() {
        return requestData;
    }
    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String request) {
        requestType = request;
    }
}
