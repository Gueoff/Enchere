package client;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import serveur.Objet;
import serveur.Vente;

public class Client extends UnicastRemoteObject  implements Acheteur {

	private static final long serialVersionUID = 1L;
	private static final String adresseServeur = "172.16.134.145:8090/enchere";
	
	private Vente serveur;
	private String pseudo;
	private EtatClient etat = EtatClient.ATTENTE;
	private Chrono chrono = new Chrono(60000); // Chrono d'1min
	private VueClient vue;

	public Client(String pseudo, Vente serveur) throws Exception {
		super();
		this.pseudo = pseudo;
		this.serveur = serveur;
		
		// Inscription
		serveur.inscriptionAcheteur(pseudo, this);
		System.out.println(pseudo + " est ajouté à la liste d'acheteurs.");
	}
	
	public Client(String pseudo) throws Exception {
		this(pseudo, connexionServeur());
	}

	public static Vente connexionServeur() {
		try {

			Vente serveur = (Vente) Naming.lookup("//" + adresseServeur);
			System.out.println("Connexion au serveur " + adresseServeur + " réussi. ");
			return serveur;

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Connexion au serveur " + adresseServeur + " impossible.");

			e.printStackTrace();

			return null;
		}
	}

	public Vente getServeur() {
		return serveur;
	}

	public void setServeur(Vente serveur) {
		this.serveur = serveur;
	}
	
	@Override
	public String getPseudo() throws RemoteException {
		return pseudo;
	}
	
	@Override
	public void nouvelleSoumission(String nom, String description, int prix) throws RemoteException {
		//serveur.addObjet(new Objet(nom, description, prix));
	}

	@Override
	public void objetVendu(Client gagnant) throws RemoteException{
		etat = EtatClient.TERMINE;
		vue.getLblEncherir().setText(gagnant.getPseudo() + "a remporté l'enchère.");
		vue.setCurrentObjet(serveur.getObjet());
		chrono.start();
	}

	@Override
	public void nouveauPrix(int prix) throws RemoteException, Exception {
		if(!chrono.getFini() & etat != EtatClient.ATTENTE) {
			serveur.rencherir(prix, this);
			etat = EtatClient.RENCHERI;
			System.out.println("Vous avez tente de rencherir de " + prix +"euros.");
		}
	}
	

	public Chrono getChrono() {
		return chrono;
	}

	public void setChrono(Chrono chrono) {
		this.chrono = chrono;
	}
	
	public static void main(String[] argv) throws Exception{
		try {
			Client c = new Client("toto");
			
			
			int cpt = 0;
			while(true) {
				System.out.println( cpt + " " + c.chrono.getFini());
				Thread.sleep(1000);
				cpt++;
			}
		} catch (RemoteException | InterruptedException e) {
			e.printStackTrace();
		}

	}

	public void setVue(VueClient vueClient) {
		vue = vueClient;
	}

}
