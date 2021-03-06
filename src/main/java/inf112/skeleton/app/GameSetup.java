package inf112.skeleton.app;
import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;

public class GameSetup {
    private static final String IPV4_PATTERN;

    static {
        IPV4_PATTERN = "^(([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])(\\.(?!$)|$)){4}$";
    }

    private static String playerCount;
    private static String board;
    /**
     * Function for querying the user for connection settings.
     * @return NetworkSettings object containing connection information
     */
    public static HashMap<String, String> setup(){
        HashMap<String, String> selection = new HashMap<>();
        String result = serverClientSelection();
        selection.put("state", result);
        if(result.equals("client")){
            String ip = ipConfig();
            selection.put("ip", ip);
        }
        else{
            selection.put("ip", "localhost");
            playerCount = selectPlayerCount();
            board = selectBoard();
        }
        ArrayList<String> ports = portConfig();
        selection.put("tcp", ports.get(0));
        selection.put("udp", ports.get(1));
        selection.put("playerCount", playerCount);
        selection.put("board", board);

        //Create networkSettings
        return selection;
    }

    /**
     * Query the Server user for player count.
     * @return the amount of players in the game
     */
    private static String selectPlayerCount() {
        Object[] possibilities = {"1","2", "3", "4"};
        String prompt = "Please select the amount of players for this game";

        Object result = JOptionPane.showInputDialog(
                null,
                prompt,
                "",
                JOptionPane.PLAIN_MESSAGE,
                null,
                possibilities,
                possibilities[0]);
        if(result == null){
            System.err.println("Program ended by user.");
            System.exit(-1);
        }
        return result.toString();
    }

    /**
     * Query the Server user for board number.
     * @return the board name as string
     */
    private static String selectBoard() {
        Object[] possibilities = {"1", "2", "3"};
        String prompt = "Please select the board you want to play on.";

        Object result = JOptionPane.showInputDialog(
                null,
                prompt,
                "",
                JOptionPane.PLAIN_MESSAGE,
                null,
                possibilities,
                possibilities[0]);
        if(result == null){
            System.err.println("Program ended by user.");
            System.exit(-1);
        }
        return result.toString();
    }

    /**
     * Query user for tcp and udp ports.
     * @return List of ports as String, List.get(0) is the tcp port and list.get(1) is the udp port.
     */
    private static ArrayList<String> portConfig() {
        ArrayList<String> ports = new ArrayList<>();
        boolean validPorts = false;
        JTextField tcpField = new JTextField(6);
        JTextField udpField = new JTextField(6);
        JPanel jPanel = new JPanel();
        jPanel.add(new JLabel("tcp:"));
        jPanel.add(tcpField);
        jPanel.add(Box.createHorizontalStrut(15)); //A spacer
        jPanel.add(new JLabel("udp:"));
        jPanel.add(udpField);
        while(!validPorts){
            int result = JOptionPane.showConfirmDialog(null, jPanel,
                    "Please Enter TCP and UDP Values", JOptionPane.OK_CANCEL_OPTION);
            //If user pressed the OK button.
            if (result == JOptionPane.OK_OPTION) {
                if(tcpField.getText().matches("[0-9]+") && udpField.getText().matches("[0-9]+")){
                    validPorts = true;
                    ports.add(tcpField.getText());
                    ports.add(udpField.getText());
                    System.out.println("Ports selected successfully");
                }
                else{
                    System.out.println("Not valid ports! Please try again.");
                }
            }
            //If user cancels the connection, closes the program.
            else if(result == JOptionPane.CANCEL_OPTION || result == JOptionPane.CLOSED_OPTION){
                System.err.println("Program ended by user.");
                System.exit(-1);
            }
        }
        return ports;
    }

    /**
     * Query user to select either server or client
     * @return Connection state, either "server" or "client"
     */
    public static String serverClientSelection() {
        Object[] possibilities = {"server", "client"};
        String prompt = "Please select if you want to be the server or join as a client";

        Object result = JOptionPane.showInputDialog(
                null,
                prompt,
                "",
                JOptionPane.PLAIN_MESSAGE,
                null,
                possibilities,
                possibilities[0]);
        if(result == null){
            System.err.println("Program ended by user.");
            System.exit(-1);
        }
        return result.toString();
    }

    /**
     * Prompts user to select an ip to connect to.
     * @return IP as String
     */
    public static String ipConfig(){
        boolean validIP = false;
        String ip = "";
        while(!validIP){
            ip = JOptionPane.showInputDialog("Enter IP:");
            if(ip == null){
                System.err.println("Program ended by user.");
                System.exit(-1);
            }
            if(ip.matches(IPV4_PATTERN) || ip.equals("localhost")){
                validIP = true;
            }
            else{
                System.out.println("Invalid IP, please try again");
            }
        }
        return ip;
    }
}

