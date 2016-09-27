package client;

import javax.swing.JButton;
import javax.swing.JFrame;

public class VueClient extends JFrame {

	private static final long serialVersionUID = 9070911591784925769L;

	public VueClient() {
		super("Rend-Chérie");
		setSize(200,100);
		setVisible(true);
		JButton bouton = new JButton("Enchérir");
	}
	
	public static void main(String[] args) {
		JFrame frame = new VueClient();
	}
	
}
