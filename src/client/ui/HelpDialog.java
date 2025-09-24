package client.ui;

import javax.swing.*;
import java.awt.*;

public class HelpDialog {
	public static void show(Component parent) {
		String help = "There are two ways to connect:\n\n" + "1) localhost - local host for testing.\n"
				+ "2) <IP address> - connect via LAN using IPv4 address.\n\n" + "Use port 1610 by default.";
		JTextArea ta = new JTextArea(help);
		ta.setEditable(false);
		ta.setOpaque(false);
		ta.setLineWrap(true);
		ta.setWrapStyleWord(true);
		JScrollPane sp = new JScrollPane(ta);
		sp.setPreferredSize(new Dimension(460, 200));
		JOptionPane.showMessageDialog(parent, sp, "Help", JOptionPane.PLAIN_MESSAGE);
	}
}