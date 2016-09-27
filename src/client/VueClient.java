package client;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class VueClient extends JFrame {

	private static final long serialVersionUID = 9070911591784925769L;

	public VueClient() {
		super("Rend-Chérie");

		JButton bouton = new JButton("Enchérir");
		JTextArea text1 = new JTextArea();
		JLabel prix = new JLabel();
		BorderLayout layout = new BorderLayout();
		JPanel vue = new JPanel(layout);
		

		this.add(vue);
		
		setSize(200,100);
		setVisible(true);
	}
	
	public static void main(String[] args) {
		JFrame frame = new VueClient();
		
		

	}
	
}
