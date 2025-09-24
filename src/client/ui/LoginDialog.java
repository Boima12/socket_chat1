package client.ui;

import javax.swing.*;
import java.awt.*;

public class LoginDialog {
	public static String askForUsername(Component parent) {
		JPanel panel = new JPanel(new BorderLayout(8, 8));
		JLabel lbl = new JLabel("Enter username:");
		JTextField tf = new JTextField(20);
		panel.add(lbl, BorderLayout.NORTH);
		panel.add(tf, BorderLayout.CENTER);

		String[] options = { "OK", "Cancel" };
		int option = JOptionPane.showOptionDialog(parent, panel, "Login", JOptionPane.NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
		
		if (option == 0) {
			String name = tf.getText().trim();
			return name.isEmpty() ? null : name;
		} else {
            System.exit(0); 
            return null;    
        }
	}
}