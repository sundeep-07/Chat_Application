import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Server implements ActionListener {

    JTextField text;
    JPanel p2;
    static Box vertical = Box.createVerticalBox();
    static JFrame f = new JFrame();
    static DataOutputStream dout;

    static Color bgMain = new Color(18, 18, 25);
    static Color header = new Color(25, 25, 35);
    static Color bubble = new Color(60, 130, 230);
    static Color button = new Color(60, 140, 255);

    Server() {
        f.setLayout(null);
        vertical.setBackground(bgMain);
        vertical.setOpaque(true);

        // Header Panel
        JPanel p1 = new JPanel();
        p1.setBackground(header);
        p1.setBounds(0, 0, 450, 70);
        p1.setLayout(null);
        f.add(p1);

        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("Icons/3.png"));
        Image i2 = i1.getImage().getScaledInstance(25, 25, Image.SCALE_DEFAULT);
        JLabel back = new JLabel(new ImageIcon(i2));
        back.setBounds(5, 20, 25, 25);
        p1.add(back);
        back.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                System.exit(0);
            }
        });

        ImageIcon i4 = new ImageIcon(ClassLoader.getSystemResource("Icons/1.png"));
        Image i5 = i4.getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT);
        JLabel profile = new JLabel(new ImageIcon(i5));
        profile.setBounds(40, 10, 50, 50);
        p1.add(profile);

        ImageIcon i7 = new ImageIcon(ClassLoader.getSystemResource("Icons/video.png"));
        Image i8 = i7.getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT);
        JLabel video = new JLabel(new ImageIcon(i8));
        video.setBounds(300, 20, 30, 30);
        p1.add(video);

        ImageIcon i10 = new ImageIcon(ClassLoader.getSystemResource("Icons/phone.png"));
        Image i11 = i10.getImage().getScaledInstance(35, 30, Image.SCALE_DEFAULT);
        JLabel phone = new JLabel(new ImageIcon(i11));
        phone.setBounds(360, 20, 35, 30);
        p1.add(phone);

        ImageIcon i13 = new ImageIcon(ClassLoader.getSystemResource("Icons/3icon.png"));
        Image i14 = i13.getImage().getScaledInstance(10, 25, Image.SCALE_DEFAULT);
        JLabel morevert = new JLabel(new ImageIcon(i14));
        morevert.setBounds(420, 20, 10, 25);
        p1.add(morevert);

        JLabel name = new JLabel("Ganesh");
        name.setBounds(110, 15, 100, 18);
        name.setForeground(Color.WHITE);
        name.setFont(new Font("Segoe UI", Font.BOLD, 18));
        p1.add(name);

        JLabel status = new JLabel("Active Now");
        status.setBounds(110, 35, 100, 18);
        status.setForeground(new Color(150, 255, 150));
        status.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        p1.add(status);

        // Chat panel
        p2 = new JPanel();
        p2.setBounds(5, 75, 440, 570);
        p2.setBackground(bgMain);
        p2.setLayout(new BoxLayout(p2, BoxLayout.Y_AXIS));
        p2.setOpaque(true);
        f.add(p2);

        // Message text field
        text = new JTextField();
        text.setBounds(5, 655, 310, 40);
        text.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        text.setForeground(Color.WHITE);
        text.setBackground(new Color(40, 40, 55));
        text.setCaretColor(Color.WHITE);
        text.setBorder(BorderFactory.createLineBorder(new Color(70, 70, 90), 1, true));
        text.addActionListener(this);
        f.add(text);

        // Send button
        JButton send = new JButton("Send");
        send.setBounds(320, 655, 123, 40);
        send.setBackground(button);
        send.setForeground(Color.WHITE);
        send.setFocusPainted(false);
        send.setBorder(BorderFactory.createEmptyBorder());
        send.setFont(new Font("Segoe UI", Font.BOLD, 16));
        send.addActionListener(this);
        f.add(send);

        // Frame settings
        f.setSize(450, 700);
        f.setLocation(200, 50);
        f.setUndecorated(true);
        f.getContentPane().setBackground(bgMain);
        f.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        try {
            String out = text.getText().trim();
            if (out.isEmpty()) return;

            JPanel p3 = formatLabel(out);

            JPanel right = new JPanel(new BorderLayout());
            right.setBackground(bgMain);
            p3.setBackground(bgMain);
            right.add(p3, BorderLayout.LINE_END);

            vertical.add(right);
            vertical.add(Box.createVerticalStrut(10));
            p2.add(vertical, BorderLayout.PAGE_START);

            dout.writeUTF(out);
            text.setText("");

            f.repaint();
            f.invalidate();
            f.validate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static JPanel formatLabel(String out) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(bgMain);

        JLabel output = new JLabel("<html><p style=\"width:180px\">" + out + "</p></html>");
        output.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        output.setBackground(bubble);
        output.setForeground(Color.WHITE);
        output.setOpaque(true);
        output.setBorder(new EmptyBorder(10, 15, 10, 15));
        panel.add(output);

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        JLabel time = new JLabel(sdf.format(cal.getTime()));
        time.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        time.setForeground(new Color(180, 180, 180));
        panel.add(time);

        return panel;
    }

    public static void main(String args[]) {
        new Server();

        try {
            ServerSocket skt = new ServerSocket(6001);
            while (true) {
                Socket s = skt.accept();
                DataInputStream din = new DataInputStream(s.getInputStream());
                dout = new DataOutputStream(s.getOutputStream());

                while (true) {
                    String msg = din.readUTF();
                    JPanel p = formatLabel(msg);

                    JPanel left = new JPanel(new BorderLayout());
                    left.setBackground(bgMain);
                    p.setBackground(bgMain);
                    left.add(p, BorderLayout.LINE_START);
                    vertical.add(left);
                    f.validate();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
