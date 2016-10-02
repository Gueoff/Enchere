package client;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import serveur.Objet;
import serveur.Vente;
import serveur.VenteImpl;

public class VueClient extends JFrame implements ActionListener{

	private static final long serialVersionUID = 9070911591784925769L;
	
	// Informations sur de l'état de la vente
	private Vente serveur;
	private Objet currentObjet;
	private Client currentClient;
	
	// Elements SWING
	@SuppressWarnings("unused")
	private JPanel mainPanel;
	
	private JLabel lblPrixObjet;
	private JLabel lblNomObjet;
	private JLabel lblDescriptionObjet;

	private JButton btnEncherir = new JButton("Enchérir");
	private JTextField txtEncherir = new JTextField();
	private JLabel lblEncherir = new JLabel();

	private JLabel lblPseudo = new JLabel("");
	private JButton btnPseudo = new JButton("Inscription");
	private JTextField txtPseudo = new JTextField();
	
	private JButton btnSoumettre = new JButton("Soumettre une enchère");
	private JFrame frmSoumettre = new JFrame("Soumettre une enchère");
	private JTextField txtSoumettreNomObjet = new JTextField();
	private JTextField txtSoumettreDescriptionObjet = new JTextField();
	private JTextField txtSoumettrePrixObjet = new JTextField();
	private JButton btnSoumettreObjet = new JButton("Soumettre");

	public JLabel getLblEncherir() {
		return lblEncherir;
	}

	public VueClient() throws Exception {
		super();
		
		// Définition du layout, du panel et de la taille de la fenêtre
		GridLayout layout = new GridLayout(4,3);
		mainPanel = new JPanel(layout);
		this.add(mainPanel);
		setSize(600,400);
		
		// Connexion au serveur et récupération des informations sur l'objet actuellement en vente
		//serveur = Client.connexionServeur();
		serveur = new VenteImpl(new ArrayList<Acheteur>(), new Objet("Titre", "Description", 20)); // Pour les tests sans serveur
		setCurrentObjet(serveur.getObjet());
		setTitle("Vente de " + currentObjet.getNom());
		
		
		// Impossible d'encherir et de soumettre une enchere tant que le client n'est pas inscrit
		btnEncherir.setEnabled(false);
		txtEncherir.setEnabled(false);
		btnSoumettre.setEnabled(false);
		
		// Ajout des liaison avec les boutons
		btnEncherir.addActionListener(this);
		btnPseudo.addActionListener(this);
		btnSoumettre.addActionListener(this);
		btnSoumettreObjet.addActionListener(this);
		
		//Ajout des éléments à la vue et affichage
		//Ligne 1 : inscription
		mainPanel.add(btnPseudo);
		mainPanel.add(txtPseudo);
		mainPanel.add(lblPseudo);
		//Ligne 2 : l'objet
		mainPanel.add(lblNomObjet);
		mainPanel.add(lblDescriptionObjet);
		mainPanel.add(lblPrixObjet);
		//Ligne 3 : encherir
		mainPanel.add(btnEncherir);
		mainPanel.add(txtEncherir);
		mainPanel.add(lblEncherir);
		//Ligne 4 : soumettre une enchere
		mainPanel.add(btnSoumettre);
		
		setVisible(true);
		
	}
	
	private void setClient(Client client) {
		currentClient = client;
		client.setVue(this);
	}
	
	public void setCurrentObjet(Objet objet) {
		currentObjet = objet;
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

			System.out.println("click");
			if(!txtEncherir.getText().isEmpty()){
				if((currentObjet.getPrixCourant() < Integer.parseInt(txtEncherir.getText()))&&(Integer.parseInt(txtEncherir.getText())>0)){
					try {
						currentClient.nouveauPrix(this.currentObjet.getPrixCourant());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

			}
		
		
		// INSCRIPTION
		else if(arg0.getSource().equals(btnPseudo)) {
			try {
				setClient(new Client(txtPseudo.getText(), serveur));
				lblPseudo.setText("Profil : " + currentClient.getPseudo());
				
				// Il n'est plus possible de s'inscrire
				btnPseudo.setVisible(false);
				txtPseudo.setVisible(false);
				
				// Le client peut maintenant encherir et soumettre un objet
				btnEncherir.setEnabled(true);
				txtEncherir.setEnabled(true);
				btnSoumettre.setEnabled(true);
			} catch (Exception e) {
				System.out.println("Inscription impossible.");
			}
		}
		
		else if(arg0.getSource().equals(btnSoumettre)) {
			soumettre();
		}
		
		else if(arg0.getSource().equals(btnSoumettreObjet)) {
			try {
				currentClient.nouvelleSoumission(txtSoumettreNomObjet.getText(), txtSoumettreDescriptionObjet.getText(), Integer.parseInt(txtSoumettrePrixObjet.getText()));
			} catch (NumberFormatException | RemoteException e) {
				System.out.println("Impossible de faire la soumettre cet objet.");
			}
			frmSoumettre.dispose();
		}
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
	
	public static void main(String[] args) throws Exception {
		new VueClient();
	}

}
