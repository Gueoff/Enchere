package client;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import serveur.Objet;
import serveur.Vente;

public class Client extends UnicastRemoteObject  implements Acheteur {

	private static final long serialVersionUID = 1L;
	private static final String adresseServeur = "localhost:8090/enchere";
	
	private Vente serveur;
	private String pseudo;
	private EtatClient etat = EtatClient.ATTENTE;
	private Chrono chrono = new Chrono(60000); // Chrono d'1min
	private VueClient vue;
	private Objet currentObjet;
	

	public Objet getCurrentObjet() {
		return currentObjet;
	}
	
	public Chrono getChrono() {
		return chrono;
	}

	public void setChrono(Chrono chrono) {
		this.chrono = chrono;
	}
	
	public Vente getServeur() {
		return serveur;
	}

	public void setServeur(Vente serveur) {
		this.serveur = serveur;
	}
	
	public void setVue(VueClient vueClient) {
		vue = vueClient;
	}
	
	@Override
	public String getPseudo() throws RemoteException {
		return pseudo;
	}
	


	public Client(String pseudo, Vente serveur) throws Exception {
		super();
		this.pseudo = pseudo;
		this.serveur = serveur;
		this.currentObjet = serveur.getObjet();
		
		// Inscription
		serveur.inscriptionAcheteur(pseudo, this);
		System.out.println(pseudo + " est ajoute a la liste d'acheteurs.");
	}
	
	public Client(String pseudo) throws Exception {
		this.pseudo = pseudo;
		this.serveur = connexionServeur();
		this.currentObjet = serveur.getObjet();
		
		// Inscription
		serveur.inscriptionAcheteur(pseudo, this);
		System.out.println(pseudo + " est ajoute a la liste d'acheteurs.");
	}
	
	public Vente connexionServeur() {
		try {
			this.serveur = (Vente) Naming.lookup("//" + adresseServeur);
			System.out.println("Connexion au serveur " + adresseServeur + " reussi. ");
			return serveur;

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Connexion au serveur " + adresseServeur + " impossible.");
			return null;
		}
	}
	
	@Override
	public void nouvelleSoumission(String nom, String description, int prix) throws RemoteException {
		Objet nouveau = new Objet(nom, description, prix);
		serveur.ajouterObjet(nouveau, this);
	}

	@Override
	public void objetVendu(Client gagnant) throws RemoteException{
		this.etat = EtatClient.TERMINE;
		this.vue.getLblEncherir().setText(gagnant.getPseudo() + "a remporte l'enchere.");
		//vue.setCurrentObjet(serveur.getObjet());
		this.chrono.start();
	}

	@Override
	public void nouveauPrix(int prix) throws RemoteException, Exception {
		if(prix <= this.currentObjet.getPrixCourant()){
			throw new Exception("Prix trop bas, ne soyez pas radin !");
		}
		else if(prix < 0){
			throw new Exception("Prix negatif");
		}
		//if(!chrono.getFini() & etat != EtatClient.ATTENTE) {
			serveur.rencherir(prix, this);
			etat = EtatClient.RENCHERI;
			System.out.println("Vous avez tente de rencherir de " + prix +"€.");
			this.currentObjet = serveur.getObjet();
		//}
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
}
