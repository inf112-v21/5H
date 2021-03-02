package inf112.skeleton.app.networkTests;

import inf112.skeleton.app.net.RequestToClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class NetworkRequestToClientTest {
    private String requestType;
    private Object requestData;
    private RequestToClient requestToClient;

    @BeforeEach
    public void setUp(){
        requestType = "Move";
        requestData = "Testcase";
        requestToClient = new RequestToClient();
    }

    @Test
    public void requestTypeSetGetTest(){
        assertNotEquals(requestType,requestToClient.getRequestType());
        requestToClient.setRequestType(requestType);
        assertEquals(requestType, requestToClient.getRequestType());
    }

    @Test
    public void requestDataSetGetTest(){
        assertNotEquals(requestData,requestToClient.getRequestData());
        requestToClient.setRequestData(requestData);
        assertEquals(requestData, requestToClient.getRequestData());
    }
}
