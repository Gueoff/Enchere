package client;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import serveur.Donnees;
import serveur.Objet;
import serveur.Vente;

public class VueClient extends JFrame implements ActionListener{

	private static final long serialVersionUID = 9070911591784925769L;
	
	// Informations sur de l'état de la vente
	private Vente serveur;
	private Objet currentObjet;
	private Client currentClient;
	
	// Elements SWING
	private JPanel mainPanel;
	
	private JLabel lblPrixObjet;
	private JLabel lblNomObjet;
	private JLabel lblDescriptionObjet;

	private JButton btnEncherir = new JButton("Enchérir");
	private JTextField txtPrix = new JTextField();

	private JButton btnInscription = new JButton("Inscription");
	private JTextField txtPseudo = new JTextField();

	
	public VueClient() throws Exception {
		super();
		
		// Définition du layout, du panel et de la taille de la fenêtre
		GridLayout layout = new GridLayout(2,3);
		JPanel mainPanel = new JPanel(layout);
		this.add(mainPanel);
		setSize(600,400);
		
		// Connexion au serveur et récupération des informations sur l'objet actuellement en vente
		/*serveur = Client.connexionServeur();
		objet = serveur.getObjet();*/
		currentObjet = new Objet("Titre", "Description", 20); // Pour test sans serveur
		setTitle("Vente de " + currentObjet.getNom());
		
		this.nouvelObjet();
		
		// Ajout des liaison avec les boutons
		btnEncherir.addActionListener(this);
		btnInscription.addActionListener(this);
		
		//Ajout des éléments à la vue et affichage
		//Ligne 1
		mainPanel.add(lblNomObjet);
		mainPanel.add(lblDescriptionObjet);
		//Ligne 2
		mainPanel.add(lblPrixObjet);
		mainPanel.add(txtPrix);
		//Ligne 3
		mainPanel.add(btnEncherir);
		// INSCRIPTION
		mainPanel.add(btnInscription);
		mainPanel.add(txtPseudo);
		setVisible(true);
		
	}
	
	private void nouvelObjet() {
		lblPrixObjet = new JLabel("Prix courant : " + currentObjet.getPrixCourant());
		lblDescriptionObjet = new JLabel(currentObjet.getDescription());
		if (currentObjet.isDisponible()) {
			lblNomObjet = new JLabel(currentObjet.getNom() + "(disponible)");
		} 
		else {
			lblNomObjet = new JLabel(currentObjet.getNom() + "(vendu)");
		}
	}
	
	@Override
	public synchronized void actionPerformed(ActionEvent arg0) {	
		// ENCHERIR			
		if(arg0.getSource().equals(this.btnEncherir)){
			if(this.currentObjet.getPrixCourant() < Integer.parseInt(this.txtPrix.getText())){
				//TODO appeler encherir
				System.out.println("ok");
			}
		}
		
		// INSCRIPTION
		else if(arg0.getSource().equals(btnInscription)) {
			try {
				currentClient = new Client(txtPseudo.getText(), serveur);
			} catch (Exception e) {
				System.out.println("Inscription impossible");
			}
		}
	}
	
	public static void main(String[] args) throws Exception {
		JFrame frame = new VueClient();
	}
}
