import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class Client {

    private static String serverName;
    private static int portNumber;
    static int ptrue = 0;
    public static Socket socket = null;
    public static ArrayList<MyFile> downloadedFile = new ArrayList<>();
    public static ArrayList<MyFile> allFiles = new ArrayList<>();

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        final File[] fileToSend = new File[1];
        int[] size = new int[1];
        size[0] = 0;

        FrameElement clientFrame =new FrameElement();
        JFrame jFrame = clientFrame.getJFrame("Client GUI");
        JLabel jlTitle = clientFrame.getJLabel("I am Client");
         //Panel 1
        JPanel jPanel = clientFrame.getJPanelType1(10,0,10,0);
        JLabel jIplable = clientFrame.getJLabel("Enter IP : ");
        jIplable.setAlignmentX(Component.LEFT_ALIGNMENT);
        JTextField jIp = clientFrame.getJTextField("localhost");
        jIp.setAlignmentX(Component.LEFT_ALIGNMENT);
        jPanel.add(jIplable);
        jPanel.add(jIp);
        //Panel 2
        JPanel jPanel2 = clientFrame.getJPanelType1(3,0,0,0);
        JLabel jPort = clientFrame.getJLabel("Enter Port : ");
        jPort.setAlignmentX(Component.LEFT_ALIGNMENT);
        JTextField jPo = clientFrame.getJTextField("0000");
        jPanel2.add(jPort);
        jPanel2.add(jPo);
        //Panel 2
        JPanel jPanel3 = clientFrame.getJPanelType1(3,0,0,0);
        JButton jConnect = clientFrame.getButton("Connect");
        jConnect.setPreferredSize(new Dimension(150,50));
        jPanel3.add(jConnect);

        JLabel jlFileName = clientFrame.getJLabel("Choose a file to send to the server");
        jlFileName.setBorder(new EmptyBorder(50, 0, 0, 0));
        // Panel 3.1
        JPanel jpButton = clientFrame.getJPanelType1(30,0,10,0);
        JButton jbUploadFile = clientFrame.getButton1("Upload File");
        JButton jbChooseFile = clientFrame.getButton1("Choose File");
        jpButton.add(jbUploadFile);
        jpButton.add(jbChooseFile);
        //Panel 4
        JPanel jPanel4 = clientFrame.getJPanelType1(10,0,0,0);
        JButton jDownload = clientFrame.getButton("Download from server");
        jPanel4.add(jDownload);

        jConnect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                serverName = jIp.getText();
                portNumber= Integer.parseInt(jPo.getText());
                ClientConnection clientConnection=new ClientConnection();
                socket = clientConnection.getClientConnected(serverName,portNumber,allFiles,ptrue,jFrame);
                ptrue=clientConnection.getPTrue();
            }
        });

        jbChooseFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jFileChooser = new JFileChooser();
                jFileChooser.setDialogTitle("Choose a file to send");
                if(jFileChooser.showOpenDialog(null)== JFileChooser.APPROVE_OPTION){
                    fileToSend[0] = jFileChooser.getSelectedFile();
                    jlFileName.setText("The file you want to send is "+ fileToSend[0].getName());
                }
            }
        });
        jbUploadFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(fileToSend[0]== null){
                    jlFileName.setText("Please choose a file first.");
                    JOptionPane.showMessageDialog(jFrame,"Please choose a file first","Choose First",JOptionPane.WARNING_MESSAGE);
                } else {
                    FileUploader fileUploader=new FileUploader();
                    fileUploader.uploadFile(fileToSend[0],socket);
                }
            }
        });

        jDownload.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(ptrue==1) {
                    ShowFiles.showAvailableFiles();
                }
                else{
                    System.out.println("Please Connect to the server, First");
                    JOptionPane.showMessageDialog(jFrame,"Please Connect to the server, First","Connect First",JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        jFrame.add(jlTitle);
        jFrame.add(jPanel);
        jFrame.add(jPanel2);
        jFrame.add(jPanel3);
        jFrame.add(jlFileName);
        jFrame.add(jpButton);
        jFrame.add(jPanel4);
        jFrame.setVisible(true);

    }
    public static MouseListener getMyMouseListener() {
        return new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JPanel jPanel = (JPanel) e.getSource();
                int fileId = Integer.parseInt(jPanel.getName());
                for (MyFile myFile : downloadedFile) {
                    if (myFile.getId() == fileId) {
                        JFrame jfPreview = FileDownloadByClient.createFrame(myFile.getName(), myFile.getData(), FileExtension.getExtension(myFile.getName()));
                        jfPreview.setVisible(true);
                    }
                }
            }
            @Override
            public void mousePressed(MouseEvent e) { }
            @Override
            public void mouseReleased(MouseEvent e) { }
            @Override
            public void mouseEntered(MouseEvent e) { }
            @Override
            public void mouseExited(MouseEvent e) { }
        };
    }
}