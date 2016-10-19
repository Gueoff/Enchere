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
		this.serveur = connexionServeur();
		this.currentObjet = serveur.getObjet();
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
	public synchronized void objetVendu(String gagnant) throws RemoteException{
		notify();
		this.etat = EtatClient.TERMINE;
		currentObjet = serveur.getObjet();
		//vue.actualiserObjet();
		if(gagnant != null) {
			//this.vue.getLblEncherir().setText(gagnant + "a remporte l'enchere.");
		}
		this.chrono.start();
		System.out.println("on a lancé le chrono");
	}

	public synchronized void encherir(int prix) throws RemoteException, Exception {
		System.out.println("Vous avez tente de rencherir de " + prix +" euros.");
		
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
			chrono.interrupt();
			etat = EtatClient.RENCHERI;
			serveur.rencherir(prix, this);
			this.currentObjet = serveur.getObjet();
		}
		wait();
	}
	
	@Override
	public synchronized void nouveauPrix(int prix, Acheteur gagnant) throws RemoteException {
		try{
			notify();
			System.out.println("le nouveau prix : " + prix);
			this.currentObjet.setPrixCourant(prix);
			this.vue.actualiserPrix();
			this.vue.actualiserObjet();
			
			//Vous etes le gagnant du moment
			if(this.equals(gagnant)){
				System.out.println("vous etes le gagnant du moment ... attendez...");
				wait();
			}
			
			this.chrono.start();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] argv) throws Exception{
		try {
			Client c = new Client("toto");
			c.inscription();
			System.out.println("normalement je suis inscrit");
			c.encherir(200);
			System.out.println("j'ai encheri de 200 -> je perd la premiere");
			c.encherir(600);
			System.out.println("j'ai encheri de 600-> je gagne la deuxieme");

		} catch (RemoteException | InterruptedException e) {
			e.printStackTrace();
		}
	}
}
