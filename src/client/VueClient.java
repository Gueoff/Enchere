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

public class VueClient extends JFrame implements ActionListener{

	private static final long serialVersionUID = 9070911591784925769L;
	private Objet objet;
	private Client theClient;
	
	//Elements SWING
	private JButton bouton;
	private JLabel prixObjet;
	private JLabel nomObjet;
	private JLabel descriptionObjet;
	JTextField nouveauPrix;
	
	
	public VueClient(Objet objet, Client c) throws RemoteException {
		super("Vente de " + objet.getNom());	
		this.objet = objet;
		this.theClient = c;
		
		//Creation des elements swing
		bouton = new JButton("Enchérir");
		bouton.addActionListener(this);
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
		vue.add(bouton);
		
		
		this.add(vue);
		setSize(600,400);
		setVisible(true);
	}
	
	public static void main(String[] args) throws RemoteException 
		{
			try {
				Donnees d = new Donnees();
				d.initObjets();
				Objet objet = d.getListeObjets().get(0);
				
				Client theClient = new Client("toto");
				
				JFrame frame = new VueClient(objet, theClient);
				
				int cpt = 0;
				while(true) {
					System.out.println(cpt);
					Thread.sleep(1000);
					cpt++;
				}
			} catch (RemoteException | InterruptedException e) {
				e.printStackTrace();
				
			}
	}

	public synchronized void actionPerformed(ActionEvent arg0){	
		if(arg0.getSource().equals(this.bouton)){
			System.out.println("click");
			if(!this.nouveauPrix.getText().isEmpty()){
				if((this.objet.getPrixCourant() < Integer.parseInt(this.nouveauPrix.getText()))&&(Integer.parseInt(this.nouveauPrix.getText())>0)){
					try {
						theClient.nouveauPrix(this.objet.getPrixCourant());
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}
		}
	}
	
}
