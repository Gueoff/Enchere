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
	private Objet objet;
	private Client client;
	private Vente serveur;
	
	//Elements SWING
	private JLabel prixObjet;
	private JLabel nomObjet;
	private JLabel descriptionObjet;
	// Encherir
	private JTextField nouveauPrix;
	private JButton btnEncherir;
	// Inscription
	private JButton btnInscription;
	private JTextField txtPseudo;

	
	public VueClient() throws Exception {
		super();
		/*serveur = Client.connexionServeur();
		objet = serveur.getObjet();*/
		objet = new Objet("Titre", "Description", 20);
		setTitle("Vente de " + objet.getNom());
		
		//Creation des elements swing
		btnEncherir = new JButton("Enchérir");
		btnEncherir.addActionListener(this);
		prixObjet = new JLabel("Prix courant : " + this.objet.getPrixCourant());
		if(this.objet.isDisponible()){
			nomObjet = new JLabel(this.objet.getNom() + "(disponible)");
		}else{
			nomObjet = new JLabel(this.objet.getNom() + "(vendu)");
		}
		
		descriptionObjet = new JLabel(this.objet.getDescription());
		nouveauPrix = new JTextField();
		GridLayout layout = new GridLayout(2,3);
		JPanel vue = new JPanel(layout);
		
		//Ajout a la vue
		//Ligne 1
		vue.add(nomObjet);
		vue.add(descriptionObjet);
		//Ligne 2
		vue.add(prixObjet);
		vue.add(nouveauPrix);
		//Ligne 3
		vue.add(btnEncherir);
		
		
		this.add(vue);
		setSize(600,400);
		setVisible(true);
		
		// INSCRIPTION
		btnInscription = new JButton("Inscription");
		txtPseudo = new JTextField();
		vue.add(btnInscription);
		vue.add(txtPseudo);

	}
	
	@Override
	public synchronized void actionPerformed(ActionEvent arg0) {	
		// ENCHERIR			
		if(arg0.getSource().equals(this.btnEncherir)){
			if(this.objet.getPrixCourant() < Integer.parseInt(this.nouveauPrix.getText())){
				//TODO appeler encherir
				System.out.println("ok");
			}
		}
		
		// INSCRIPTION
		else if(arg0.getSource().equals(btnInscription)) {
			try {
				client = new Client(txtPseudo.getText(), serveur);
			} catch (Exception e) {
				System.out.println("Inscription impossible");
			}
		}
	}
	
	public static void main(String[] args) throws Exception {
		JFrame frame = new VueClient();
	}


	
}
