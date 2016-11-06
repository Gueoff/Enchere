package serveur;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import client.Acheteur;

public class VenteImpl extends UnicastRemoteObject implements Vente{
	
	private static final long serialVersionUID = 1L;
	private List<Acheteur> listeAcheteurs = new ArrayList<Acheteur>();
	private List<Acheteur> fileAttente = new ArrayList<Acheteur>();
	private Map<Acheteur, Integer> enchereTemp = new HashMap<Acheteur, Integer>();
	private Objet objetCourant;
	private Stack<Objet> listeObjets;
	private Acheteur acheteurCourant;
	private EtatVente etatVente;
	
	
	protected VenteImpl() throws RemoteException {
		super();
		this.etatVente = EtatVente.ATTENTE;
	}
	
	public VenteImpl(Stack<Objet> listeObjets) throws RemoteException {
		super();
		this.listeObjets = listeObjets;
		this.objetCourant = listeObjets.pop();
		this.etatVente = EtatVente.ATTENTE;
	}


	@Override
	public synchronized boolean inscriptionAcheteur(String login, Acheteur acheteur) throws Exception{
		for(Acheteur each : listeAcheteurs){
			if(each.getPseudo().equals(login) || each.getPseudo().equals(acheteur.getPseudo())){
				throw new Exception("Login deja pris");
			}
		}			
		this.fileAttente.add(acheteur);

		if(this.fileAttente.size() >= 2 && (this.etatVente == EtatVente.ATTENTE || this.etatVente == EtatVente.TERMINE)){
			this.etatVente = EtatVente.ENCHERISSEMENT;	
			
			for(Acheteur each : this.fileAttente){
				this.listeAcheteurs.add(each);
				each.objetVendu(null);
			}
			this.fileAttente.clear();
			return true;
		}
		
		return false;
	}

	
	@Override
	public synchronized int rencherir(int nouveauPrix, Acheteur acheteur) throws Exception{
		System.out.println("On recoit une enchere");
		this.enchereTemp.put(acheteur, nouveauPrix);
		
		System.out.println("On est à "+this.enchereTemp.size()+" reponses sur "+this.listeAcheteurs.size());
		
		//On a recu toutes les encheres
		if(this.enchereTemp.size() == this.listeAcheteurs.size()){
			System.out.println("on a toutes les demandes");
			
			//Fin des encheres, on clean
			if(fini()){
				enchereSuivante();
			}
			
			//On encheri sur le meme objet
			else{
				Set<Acheteur> cles = this.enchereTemp.keySet();
				Iterator<Acheteur> it = cles.iterator();
				
				while (it.hasNext()){
					Acheteur cle = it.next();
					Integer valeur = this.enchereTemp.get(cle);
					
					if(valeur > this.objetCourant.getPrixCourant() || (valeur == this.objetCourant.getPrixCourant() && cle.getChrono() < acheteurCourant.getChrono())){
						this.objetCourant.setPrixCourant(valeur);
						this.acheteurCourant = cle;	
				   }
				}
				
				this.enchereTemp.clear();
				//On renvoie le resultat du tour
				for(Acheteur each : this.listeAcheteurs){
					each.nouveauPrix(this.objetCourant.getPrixCourant(), this.acheteurCourant);
				}
			}
		}else{
			System.out.println("en attente de reponse...");
		}
		
		return objetCourant.getPrixCourant();
	}

	
	/**
	 * Permet de passer à l'objet suivant avec les bons acheteurs et bons objets.
	 * @throws RemoteException
	 * @throws InterruptedException
	 */
	public void enchereSuivante() throws RemoteException, InterruptedException{
		this.enchereTemp.clear();
		this.objetCourant.setDisponible(false);
		this.etatVente = EtatVente.TERMINE;
		
		for(Acheteur each : this.listeAcheteurs){
			each.objetVendu(this.acheteurCourant.getPseudo());
		}
		
		Thread.sleep(100);
		this.enchereTemp.clear();
		this.acheteurCourant = null;
		this.objetCourant = this.listeObjets.pop();
		this.listeAcheteurs.addAll(this.fileAttente);
		this.fileAttente.clear();
		this.etatVente = EtatVente.ENCHERISSEMENT;
		
		for(Acheteur each : this.listeAcheteurs){
			each.objetVendu(null);
		}
	}
	
	
	/**
	 * méthode utilitaire qui permet de savoir si les encheres sont finis.
	 * @return true si on a reçu que des -1, donc si l'enchere est finie, sinon false.
	 */
	public boolean fini(){	
		Set<Acheteur> cles = this.enchereTemp.keySet();
		Iterator<Acheteur> it = cles.iterator();
		
		while (it.hasNext()){
			Acheteur cle = it.next();
			Integer valeur = this.enchereTemp.get(cle);
			
			if(valeur != -1){
				return false;	
		   }
		}
		return true;
	}
	
	
	@Override
	public int tempsEcoule(Acheteur acheteur) throws RemoteException {
		//long chrono = acheteur.getChrono();
		
		return 0;
	}

	@Override
	public void ajouterObjet(Objet objet) throws RemoteException {
		try {
			this.listeObjets.push(objet);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public Objet getObjet() throws RemoteException {
		return this.objetCourant;
	}

	public List<Acheteur> getListeAcheteurs() {
		return listeAcheteurs;
	}

	public void setListeAcheteurs(List<Acheteur> listeAcheteurs) {
		this.listeAcheteurs = listeAcheteurs;
	}

	public Objet getObjetCourant() {
		return objetCourant;
	}

	public void setObjetCourant(Objet objetCourant) {
		this.objetCourant = objetCourant;
	}

	public Stack<Objet> getListeObjets() {
		return listeObjets;
	}

	public void setListeObjets(Stack<Objet> listeObjets) {
		this.listeObjets = listeObjets;
	}

	public Acheteur getAcheteurCourant() {
		return acheteurCourant;
	}

	public void setAcheteurCourant(Acheteur acheteurCourant) {
		this.acheteurCourant = acheteurCourant;
	}

	public EtatVente getEtatVente() {
		return etatVente;
	}

	public void setEtatVente(EtatVente etatVente) {
		this.etatVente = etatVente;
	}

	


}
