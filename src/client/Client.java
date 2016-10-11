package client;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Stack;

import serveur.Objet;
import serveur.Vente;
import serveur.VenteImpl;

public class Client extends UnicastRemoteObject  implements Acheteur {

	private static final long serialVersionUID = 1L;
	private static final String adresseServeur = "localhost:8090/enchere";
	
	private Vente serveur;
	private String pseudo;
	private EtatClient etat = EtatClient.ATTENTE;
	private Chrono chrono = new Chrono(30000, this); // Chrono de 30sc
	private VueClient vue;
	private Objet currentObjet;
	

	public Objet getCurrentObjet() {
		return currentObjet;
	}
	
	@Override
	public long getChrono() {
		return chrono.getTemps();
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
	
	public Client(String pseudo) throws Exception {
		super();
		this.pseudo = pseudo;
		//this.serveur = connexionServeur();
		Stack<Objet> test = new Stack<Objet>();
		test.add(new Objet("titre","description", 0));
		this.serveur = new VenteImpl(test);
		this.currentObjet = serveur.getObjet();
		
		inscription();
	}
	
	public void inscription() throws Exception {
		serveur.inscriptionAcheteur(pseudo, this);
		System.out.println(pseudo + " est ajoute a la liste d'acheteurs.");
	}
	
	public static Vente connexionServeur() {
		try {
			Vente serveur = (Vente) Naming.lookup("//" + adresseServeur);
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
		serveur.ajouterObjet(nouveau);
	}

	@Override
	public synchronized void objetVendu(Client gagnant) throws RemoteException{
		notify();
		this.etat = EtatClient.TERMINE;
		if(gagnant != null) {
			this.vue.getLblEncherir().setText(gagnant.getPseudo() + "a remporte l'enchere.");
		}
		currentObjet = serveur.getObjet();
		//vue.actualiserObjet();
		this.chrono.start();
	}

	public synchronized void encherir(int prix) throws RemoteException, Exception {
		if(chrono.getFini()) {
			serveur.rencherir(-1, this);
			etat = EtatClient.RENCHERI;
		}
		else if(prix <= this.currentObjet.getPrixCourant()){
			System.out.println("Prix trop bas, ne soyez pas radin !");
		}
		else if(prix < 0){
			System.out.println("Prix negatif");
		}
		else if(etat != EtatClient.ATTENTE) {
			serveur.rencherir(prix, this);
			etat = EtatClient.RENCHERI;
			System.out.println("Vous avez tenté de rencherir de " + prix +"€.");
			this.currentObjet = serveur.getObjet();
		}
		wait();
	}
	
	@Override
	public synchronized void nouveauPrix(int prix) throws Exception {
		notify();
		this.chrono.start();
		currentObjet.setPrixCourant(prix);
		vue.actualiserPrix();
	}
	
	public static void main(String[] argv) throws Exception{
		try {
			Client c = new Client("toto");
			int cpt = 0;
			c.objetVendu(null);
			while(true) {
				System.out.println( cpt + " " + c.chrono.getFini() + c.getChrono());
				Thread.sleep(1000);
				cpt++;
			}
		} catch (RemoteException | InterruptedException e) {
			e.printStackTrace();
		}
	}
}
