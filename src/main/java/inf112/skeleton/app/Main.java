package inf112.skeleton.app;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {
    private static final String IPV4_PATTERN = "^(([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])(\\.(?!$)|$)){4}$";

    public static void main(String[] args) {
        NetworkSettings settings = setup();
        //check connection
        System.out.println(settings.getState());
        //Application setup
        Lwjgl3ApplicationConfiguration cfg = new Lwjgl3ApplicationConfiguration();
        cfg.setTitle(settings.getState());
        cfg.setWindowedMode(900, 900);
        cfg.setResizable(true);
        cfg.useVsync(true);
        cfg.setIdleFPS(60);
        new Lwjgl3Application(new Game(settings), cfg);
        System.exit(42069);
    }

    public static NetworkSettings setup(){
        HashMap<String, String> selection = new HashMap<>();
        String result = serverClientSelection();
        selection.put("state", result);
        if(result.equals("Client")){
            String ip = ipConfig();
            selection.put("ip", ip);
        }
        else{
            selection.put("ip", "localhost");
        }
        ArrayList<String> ports = portConfig();
        selection.put("tcp", ports.get(0));
        selection.put("udp", ports.get(1));

        //Create networkSettings
        return new NetworkSettings(selection.get("state"), selection.get("ip"), Integer.parseInt(selection.get("tcp")), Integer.parseInt(selection.get("udp")));
    }

    /**
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
     * Function for asking user to select either server or client
     * @return Connection state, either "Server" or "Client"
     */
    public static String serverClientSelection() {
        Object[] possibilities = {"Server", "Client"};
        String prompt = "Please select if you want to Host Server or join as a Client";

        Object result = JOptionPane.showInputDialog(
                null,
                prompt,
                "",
                JOptionPane.PLAIN_MESSAGE,
                null,
                possibilities,
                possibilities[1]);
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
