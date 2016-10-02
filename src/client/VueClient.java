package client;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import serveur.Vente;

public class VueClient extends JFrame implements ActionListener{

	private static final long serialVersionUID = 9070911591784925769L;
	
	// Informations sur de l'Etat de la vente
	private Vente serveur;
	private Client currentClient;
	
	// Elements SWING
	private JPanel mainPanel = new JPanel();
	private JPanel inscriptionPanel = new JPanel();
	
	private JLabel lblPrixObjet = new JLabel();
	private JLabel lblNomObjet = new JLabel();
	private JLabel lblDescriptionObjet = new JLabel();

	private JButton btnEncherir = new JButton("Encherir");
	private JTextField txtPrix = new JTextField();

	private JButton btnInscription = new JButton("Inscription");
	private JTextField txtPseudo = new JTextField();

	

	public VueClient() throws Exception {
		super();

		//Definition de la fenetre
		this.setSize(600,400);
		this.setTitle("Encheres2ouf");
		
		// Definition de la fenetre d inscription
		inscriptionPanel.setLayout(new GridBagLayout());
	    txtPseudo.setPreferredSize(new Dimension(400, 40));   
	    btnInscription.setPreferredSize(new Dimension(100,40));
		GridBagConstraints gbc = new GridBagConstraints();

	    gbc.gridx = 0;
	    gbc.gridy = 2;
	    gbc.gridheight = 1;
	    gbc.gridwidth = 3;
		inscriptionPanel.add(txtPseudo, gbc);
		
	    gbc.gridx = 4;
	    gbc.gridy = 2;
	    gbc.gridheight = 1;
	    gbc.gridwidth = 1;
		inscriptionPanel.add(btnInscription, gbc);

			
		// Definition de la fenetre de vente
		mainPanel.setLayout(new GridBagLayout());
		mainPanel.setPreferredSize(new Dimension(600,400));
		lblDescriptionObjet.setPreferredSize(new Dimension(500,300));
		txtPrix.setPreferredSize(new Dimension(300,40));
		btnEncherir.setPreferredSize(new Dimension(100,40));		
				
		//1ere ligne
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridheight = 1;
		gbc.gridwidth = 3;
		mainPanel.add(lblNomObjet, gbc);
				
		gbc.gridx = 3;
		gbc.gridheight = 1;
		mainPanel.add(lblPrixObjet, gbc);
		
		//2eme ligne
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridheight = 2;
		gbc.gridwidth = 6;
		mainPanel.add(lblDescriptionObjet, gbc);
		
		//3eme ligne
		gbc.gridy = 3;
		gbc.gridheight = 1;
		gbc.gridwidth = 3;
		mainPanel.add(txtPrix, gbc);
		
		gbc.gridx = 4;
		gbc.gridwidth = 1;
		mainPanel.add(btnEncherir, gbc);
		
		// Ajout des liaison avec les boutons
		btnEncherir.addActionListener(this);
		btnInscription.addActionListener(this);

		this.setContentPane(inscriptionPanel);
		this.setVisible(true);
	}
	
	
	private void actualiserObjet() {
		lblPrixObjet.setText("Prix courant : " + this.currentClient.getCurrentObjet().getPrixCourant() + "€");
		lblDescriptionObjet.setText(this.currentClient.getCurrentObjet().getDescription());
		txtPrix.setText("");
		if (this.currentClient.getCurrentObjet().isDisponible()) {
			lblNomObjet.setText(this.currentClient.getCurrentObjet().getNom() + "(disponible)");
		} 
		else {
			lblNomObjet.setText(this.currentClient.getCurrentObjet().getNom() + "(vendu)");
		}
	}
	
	@Override
	public synchronized void actionPerformed(ActionEvent arg0) {	
		// ENCHERIR			
		if(arg0.getSource().equals(this.btnEncherir)){

			if(!txtPrix.getText().isEmpty()){
				try {
					currentClient.nouveauPrix(Integer.parseInt(txtPrix.getText()));
					actualiserObjet();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		
		// INSCRIPTION
		else if(arg0.getSource().equals(btnInscription)) {
			try {
				currentClient = new Client(txtPseudo.getText(), serveur);
				changerGUI(this.mainPanel);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Inscription impossible");
			}
		}
	}
	
	/**
	 * Methode servant a changer l affichage pour le panel passe en parametre.
	 * @param vue le JPanel a afficher.
	 */
	public void changerGUI(JPanel vue){
		if(this.currentClient.getCurrentObjet() != null){
			actualiserObjet();
		}
		this.getContentPane().removeAll();
		this.setContentPane(vue);
		this.getContentPane().revalidate();
		this.getContentPane().repaint();
	}

	public static void main(String[] args) throws Exception {
		JFrame frame = new VueClient();
	}

}
