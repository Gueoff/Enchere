package client;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import serveur.Objet;

public class VueClient extends JFrame implements ActionListener{

	private static final long serialVersionUID = 9070911591784925769L;
	
	// Informations sur de l'Etat de la vente
	private Client currentClient;
	
	// Elements SWING
	private JPanel mainPanel = new JPanel();
	private JPanel inscriptionPanel = new JPanel();
	private JPanel attentePanel = new JPanel();
	
	private JLabel lblPrixObjet = new JLabel();
	private JLabel lblNomObjet = new JLabel();
	private JLabel lblDescriptionObjet = new JLabel();
	private JLabel lblPseudo = new JLabel();
	private JLabel lblEncherir = new JLabel();
	private JLabel lblAttente = new JLabel();

	private JButton btnEncherir = new JButton("Encherir");
	private JButton btnPseudo = new JButton("Inscription");
	private JButton btnSoumettre = new JButton("Soumettre une enchere");
	private JButton btnSoumettreObjet = new JButton("Soumettre");
	
	private JTextField txtEncherir = new JTextField();
	private JTextField txtPseudo = new JTextField();
	private JTextField txtSoumettreNomObjet = new JTextField();
	private JTextField txtSoumettreDescriptionObjet = new JTextField();
	private JTextField txtSoumettrePrixObjet = new JTextField();
	
	private JFrame frmSoumettre = new JFrame("Soumettre une enchere");

	public JLabel getLblEncherir() {
		return lblEncherir;
	}

	public VueClient() throws Exception {
		super();

		//Definition de la fenetre
		this.setSize(800,400);
		this.setTitle("Encheres2ouf");
		Font fontBtn = new Font("Serif", Font.PLAIN, 10); // par exemple 
		
		//PANEL ATTENTE
		lblAttente.setText("En attente d'une nouvelle enchère...");
		attentePanel.add(lblAttente);
		
		// PANEL INSCRIPTION
		inscriptionPanel.setLayout(new GridBagLayout());
	    txtPseudo.setPreferredSize(new Dimension(400, 40));   
	    btnPseudo.setPreferredSize(new Dimension(100,40));
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
		inscriptionPanel.add(btnPseudo, gbc);

			
		// PANEL VENTE
		mainPanel.setLayout(new GridBagLayout());
		mainPanel.setPreferredSize(new Dimension(800,400));
		lblDescriptionObjet.setPreferredSize(new Dimension(500,300));
		txtEncherir.setPreferredSize(new Dimension(300,40));
		btnEncherir.setPreferredSize(new Dimension(100,40));
		btnEncherir.setFont(fontBtn);
		btnSoumettre.setPreferredSize(new Dimension(100,40));
		btnSoumettre.setFont(fontBtn);
				
		//1ere ligne
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridheight = 1;
		gbc.gridwidth = 2;
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
		mainPanel.add(txtEncherir, gbc);
		
		gbc.gridx = 4;
		gbc.gridwidth = 1;
		mainPanel.add(btnEncherir, gbc);
		
		gbc.gridx=5;
		gbc.gridwidth=1;
		mainPanel.add(btnSoumettre, gbc);
		
		// Ajout des liaison avec les boutons
		btnEncherir.addActionListener(this);
		btnPseudo.addActionListener(this);
		btnSoumettre.addActionListener(this);
		btnSoumettreObjet.addActionListener(this);

		this.setContentPane(inscriptionPanel);
		this.setVisible(true);
	}
	
	public void actualiserPrix() {
		int prix = currentClient.getCurrentObjet().getPrixCourant();
		lblPrixObjet.setText("Prix courant : " + prix + " euros");
		this.repaint();
	}
	
	public void actualiserObjet() {
		Objet objet = currentClient.getCurrentObjet();
		lblPrixObjet.setText("Prix courant : " + objet.getPrixCourant() + " euros");
		lblDescriptionObjet.setText(objet.getDescription());
		txtEncherir.setText("");
		if (objet.isDisponible()) {
			lblNomObjet.setText(objet.getNom() + "(disponible)");
		}
		else{
			lblNomObjet.setText(objet.getNom() + "(vendu)");
		}
	}
	
	private void setClient(Client client) {
		currentClient = client;
		client.setVue(this);
	}
	
	
	
	@Override
	public synchronized void actionPerformed(ActionEvent arg0) {
		// ENCHERIR			
		if(arg0.getSource().equals(this.btnEncherir)){
			if(!txtEncherir.getText().isEmpty()){
				try {
					currentClient.encherir(Integer.parseInt(txtEncherir.getText()));
					actualiserObjet();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		
		// INSCRIPTION
		else if(arg0.getSource().equals(btnPseudo)) {
			try {
				setClient(new Client(txtPseudo.getText()));
				currentClient.inscription();
				changerGUI(this.mainPanel);
				//changerGUI(this.attentePanel);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Inscription impossible");
			}
		}
		
		else if(arg0.getSource().equals(btnSoumettre)) {
			soumettre();
		}
		
		else if(arg0.getSource().equals(btnSoumettreObjet)) {
			try {
				currentClient.nouvelleSoumission(txtSoumettreNomObjet.getText(), txtSoumettreDescriptionObjet.getText(), Integer.parseInt(txtSoumettrePrixObjet.getText()));
			} catch (NumberFormatException e) {
				System.out.println("Impossible de faire la soumettre cet objet.");
			}
			frmSoumettre.dispose();
		}
	}
	

	/**
	 * Methode servant a changer l affichage pour le panel passe en parametre.
	 * @param vue le JPanel a afficher.
	 * @throws RemoteException 
	 */
	public void changerGUI(JPanel vue) throws RemoteException{
		if(this.currentClient.getCurrentObjet() != null){
			actualiserObjet();
		}
		System.out.println("1");
		this.getContentPane().removeAll();
		System.out.println("2");
		this.setContentPane(vue);
		System.out.println("3");
		this.getContentPane().revalidate();
		System.out.println("4");
		this.getContentPane().repaint();
		System.out.println("5");
	}

	private void soumettre() {
		frmSoumettre.setSize(400,300);
		JPanel pnlSoumettre = new JPanel(new GridLayout(3,3));
		frmSoumettre.add(pnlSoumettre);
		
		pnlSoumettre.add(new JLabel("Nom de l'objet"));
		pnlSoumettre.add(new JLabel("Une description de l'objet"));
		pnlSoumettre.add(new JLabel("Prix initial"));

		pnlSoumettre.add(txtSoumettreNomObjet);
		pnlSoumettre.add(txtSoumettreDescriptionObjet);
		pnlSoumettre.add(txtSoumettrePrixObjet);
		
		pnlSoumettre.add(btnSoumettreObjet);
		
		frmSoumettre.setVisible(true);
	}
	
	public JPanel getMainPanel() {
		return mainPanel;
	}

	public JPanel getAttentePanel() {
		return attentePanel;
	}

	public static void main(String[] args) throws Exception {
		new VueClient();
	}

}
