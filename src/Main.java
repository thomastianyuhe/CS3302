import javax.swing.*;
import java.awt.*;

public class Main {
    private static final int LENGTH_OF_DATA = 720;
    private static JButton goButton;
    private static JLabel errorProbabilityLabel;
    private static JTextField errorProbabilityField;
    public static void main(String[] args){
        JFrame frame = new JFrame();
        Container container = frame.getContentPane();
        container.setLayout(new BoxLayout(container, BoxLayout.PAGE_AXIS));
        errorProbabilityLabel = new JLabel("Enter the p: ");
        errorProbabilityField = new JTextField();
        goButton = new JButton("Go");
        container.add(errorProbabilityLabel);
        container.add(errorProbabilityField);
        container.add(goButton);
        frame.setSize(400, 400);
        frame.setVisible(true);
        String data = Util.generateRandomDataString(720);

    }
}
