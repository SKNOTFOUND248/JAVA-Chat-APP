import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Vector;
import javax.swing.*;
import java.net.*;

public class FrontEnd extends JFrame {
    private final JTextArea chatArea;
    private final JTextField messageField;
    private final JTextField friend;
    private final JTextField address;
    private JButton sendButton = null;
    private final JButton clear;
    private final JButton Save;
    private final JButton fetch;
    private final JButton init;
    private JComboBox<String> cb;
    private final JTabbedPane tb;
    private Boolean b = true,mode = true;
    private PrintWriter p;
    private DataOutputStream out;

    public FrontEnd() {
        setTitle("Chat Application");
        setSize(490, 530);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Chat display area
        chatArea = new JTextArea();
        Save = new JButton("Save");
        Save.setFont(new Font("Arial", Font.BOLD, 20));
        fetch = new JButton("Fetch Recivers");
        fetch.setFont(new Font("Arial", Font.BOLD, 20));
        init = new JButton("Init Chat");
        init.setFont(new Font("Arial", Font.BOLD, 20));
        chatArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(chatArea);

        sendButton = new JButton("Send");
        clear = new JButton("Clear");

        tb = new JTabbedPane();

        // Input panel
        messageField = new JTextField(18);
        messageField.setFont(new Font("Arial", Font.BOLD, 20));

        friend = new JTextField(15);

        friend.setFont(new Font("Arial", Font.BOLD, 20));

        address = new JTextField(15);
        address.setFont(new Font("Arial", Font.BOLD, 20));
        address.setText("Please Enter Valid IP");
        address.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (b) {
                    address.setText("");
                }
                b = false;

            }
        });

        JLabel b1 = new JLabel("Name :");
        b1.setFont(new Font("Consolas", Font.BOLD, 24));
        b1.setHorizontalAlignment(JLabel.CENTER);

        JLabel b2 = new JLabel("IP Address :");
        b2.setFont(new Font("Consolas", Font.BOLD, 24));
        b2.setHorizontalAlignment(JLabel.CENTER);

        JPanel p1 = new JPanel(new GridLayout(2, 1));
        JPanel MainP1 = new JPanel(null);
        JPanel MainP2 = new JPanel(new BorderLayout());
        JPanel MainP3 = new JPanel(new BorderLayout());
        JPanel MainP4 = new JPanel(new BorderLayout());
        JPanel p = new JPanel(new BorderLayout());
        JPanel senddata = new JPanel();
        senddata.add(messageField);
        senddata.add(sendButton);
        senddata.add(clear);
        JPanel Com = new JPanel();

        JToggleButton toggleButton = new JToggleButton("Enter Choice");

        toggleButton.setFocusable(false);

        toggleButton.setFont(new Font("Consolas", Font.BOLD, 36));
        MainP4.add(toggleButton,BorderLayout.CENTER);
        MainP4.add(init,BorderLayout.SOUTH);

        MainP3.add(scrollPane, BorderLayout.CENTER);
        MainP3.add(senddata, BorderLayout.SOUTH);

        p.add(Com, BorderLayout.CENTER);
        p.add(fetch, BorderLayout.SOUTH);

        MainP2.add(p);
        MainP1.setSize(getWidth() - 10, getHeight() - 10);

        JPanel p3 = new JPanel();
        p3.add(b1);
        p3.add(friend);

        JPanel p4 = new JPanel();
        p4.add(b2);
        p4.add(address);

        p1.add(p3);
        p1.add(p4);

        Save.setBounds(MainP1.getWidth() - 240, MainP1.getHeight() - 160, 100, 50);

        p1.setBounds(0, 0, MainP1.getWidth(), MainP1.getHeight() - 160);

        MainP1.add(p1);
        MainP1.add(Save);
        tb.addTab("Recever DATA", MainP1);
        tb.addTab("Fetch Reciver Data", MainP2);
        tb.addTab("Act AS", MainP4);
        tb.addTab("Chat", MainP3);

        sendButton.addActionListener(_->sendMessage());
        clear.addActionListener(_->chatArea.setText(""));
        Save.setFocusable(false);
        Save.addActionListener(_ -> {
            this.SendDATA();
        });
        init.addActionListener(_->{
            JOptionPane.showConfirmDialog(this, "Chat INITED You can chat now", "Info", JOptionPane.CLOSED_OPTION,
                JOptionPane.INFORMATION_MESSAGE);
            if(mode){
                chatArea.setText("");
                Interaction(true, 5000, null,"");
            }
            else{
                try {
                    String IP = BackEnd.Fetch_IP((String) cb.getSelectedItem());
                    Interaction(false, 5000, (String) cb.getSelectedItem(),IP);
                } catch (Exception e) {
                    JOptionPane.showConfirmDialog(this, "Sender Name is NONE", "Info", JOptionPane.CLOSED_OPTION,
                    JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        fetch.setFocusable(false);

        fetch.addActionListener(_ -> {
            try {
                cb = new JComboBox<>(BackEnd.Fetch_Info());
                cb.setFont(new Font("Consolas", Font.BOLD, 36));
                cb.setVisible(true);
                Com.add(cb);
                revalidate();
                fetch.setEnabled(false);
            } catch (ClassNotFoundException | SQLException e) {
                JOptionPane.showConfirmDialog(this, "DataBase Error", "Error", JOptionPane.CANCEL_OPTION,
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        toggleButton.addActionListener(_ -> {
            if (toggleButton.isSelected()) {
                toggleButton.setText("Reciver");
                tb.setEnabledAt(1, false);
                mode = true;
            } else {
                toggleButton.setText("Sender");
                tb.setEnabledAt(1, true);
                mode = false;
              
            }
        });

        messageField.addActionListener(_ -> {

            sendMessage();

        });

        getContentPane().add(tb);

        setVisible(true);
        setLocationRelativeTo(null);
    }

    private void SendDATA() {
        Vector<String> vcc = new Vector<>();
        if (!friend.getText().isEmpty() && !address.getText().isEmpty()) {
            vcc.add(friend.getText());
            vcc.add(address.getText());
            try {
                BackEnd.Connect(vcc);
            } catch (ClassNotFoundException | SQLException e) {
                JOptionPane.showConfirmDialog(this, "DataBase Error", "Error", JOptionPane.CANCEL_OPTION,
                        JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showConfirmDialog(this, "Enter the Data", "Info", JOptionPane.CLOSED_OPTION,
                    JOptionPane.INFORMATION_MESSAGE);
        }

    }

    private void sendMessage() {
        try {
            String message = messageField.getText();
            out.writeUTF(message);
            out.flush();
            SwingUtilities.invokeLater(() -> chatArea.append("\nYou: " + message + "\n"));
            messageField.setText(""); 
        } catch (IOException | NullPointerException e) {
        }
    }

    private void Interaction(boolean flag, int port, String name,String ip) {
        if (flag) {

            new Thread(() -> {
                try (ServerSocket serverSocket = new ServerSocket(port)) {
                    SwingUtilities.invokeLater(() -> chatArea.setText("Server started on port " + "\nWating for Client to respond on" + port + "...."));
                    Socket con = serverSocket.accept();
                    DataOutputStream ds = new DataOutputStream(con.getOutputStream());
                    try {
                        SwingUtilities.invokeLater(() -> chatArea.append("\nNew client connected."));

                        try (DataInputStream rd = new DataInputStream(con.getInputStream())) {
                            out = new DataOutputStream(con.getOutputStream());
                            out.writeUTF("Enter your Name:= ");
                            String n = rd.readUTF();
                            String m;
                            while (true) {
                                try {
                                    m = rd.readUTF();
                                    if (m.equalsIgnoreCase("exit")) {
                                        SwingUtilities.invokeLater(() -> chatArea.append("\nClient disconnected."));
                                        break;
                                    }
                                    String finalM = m;
                                    SwingUtilities.invokeLater(() -> chatArea.append("\n" + n + ": " + finalM));
                                } catch (IOException e) {
                                    SwingUtilities.invokeLater(
                                            () -> chatArea.append("\nChat Ended! Have a nice Day "));
                                    break;
                                }
                            }
                        }
                    } catch (IOException e) {
                        SwingUtilities.invokeLater(() -> chatArea.append("\nConnection Error : " + e.getMessage()));
                    }
                } catch (Exception e) {

                }

            }).start();

        } else {
            new Thread(() -> {
                SwingUtilities.invokeLater(() -> chatArea.setText("Wating for server to respond on  " + port + "..."));
                try (Socket con = new Socket(ip,port)) {
                    SwingUtilities.invokeLater(() -> chatArea.setText("Connected to Server on port " + port + "..."));
                    DataOutputStream ds = new DataOutputStream(con.getOutputStream());
                    try {
                        try (DataInputStream rd = new DataInputStream(con.getInputStream())) {
                            out = new DataOutputStream(con.getOutputStream());
                            String m;
                            while (true) {
                                try {
                                    m = rd.readUTF();
                                    if (m.equalsIgnoreCase("exit")) {
                                        SwingUtilities.invokeLater(() -> chatArea.append("\nServer disconnected."));
                                        break;
                                    }
                                    String finalM = m;
                                    SwingUtilities.invokeLater(() -> chatArea.append("\n" + name + ": " + finalM));
                                } catch (IOException e) {
                                    SwingUtilities.invokeLater(
                                            () -> chatArea.append("\nChat Ended! Have a nice Day"));
                                    break;
                                }
                            }
                        }
                    } catch (IOException e) {
                        SwingUtilities.invokeLater(() -> chatArea.append("\nConnection Error : " + e.getMessage()));
                    }
                } catch (Exception e) {

                }

            }).start();
        }

    }
}
