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
		this.objetCourant = listeObjets.pop();
		this.etatVente = EtatVente.ATTENTE;
	}


	
	
	@Override
	public void inscriptionAcheteur(String login, Acheteur acheteur) throws Exception{
		for(Acheteur each : listeAcheteurs){
			if(each.getPseudo().equals(login) || each.getPseudo().equals(acheteur.getPseudo())){
				throw new Exception("Login deja pris");
			}
		}
		
		this.listeAcheteurs.add(acheteur);
		
		if(this.listeAcheteurs.size() >= 2){
			this.etatVente = EtatVente.ENCHERISSEMENT;	

			for(Acheteur each : this.listeAcheteurs){
				each.objetVendu(null);
			}
		}
	}

	
	@Override
	public synchronized int rencherir(int nouveauPrix, Acheteur acheteur) throws Exception{
		System.out.println("On recoit une enchere");
		this.enchereTemp.put(acheteur, nouveauPrix);
		
		//On a recu toutes les encheres
		if(this.enchereTemp.size() == this.listeAcheteurs.size()){
			System.out.println("on a toutes les demandes");
			Set<Acheteur> cles = this.enchereTemp.keySet();
			Iterator<Acheteur> it = cles.iterator();
			
			while (it.hasNext()){
				Acheteur cle = it.next();
				Integer valeur = this.enchereTemp.get(cle);
				
				if(valeur > this.objetCourant.getPrixCourant()){
					this.objetCourant.setPrixCourant(valeur);
					this.acheteurCourant = cle;	
			   }else if(valeur == this.objetCourant.getPrixCourant()){
				   	if(cle.getChrono() < acheteurCourant.getChrono()){
						this.acheteurCourant = cle;	
				   	}
			   }
			}
			this.enchereTemp.clear();
			this.enchereTemp.put(this.acheteurCourant, this.objetCourant.getPrixCourant());
			System.out.println(this.acheteurCourant.getPseudo()+" a gagné la bataille, mais pas la guerre");
			
			//On renvoie le resultat du tour
			for(Acheteur each : this.listeAcheteurs){
				System.out.println("nouveauPrix() pour "+each.getPseudo());
				each.nouveauPrix(this.objetCourant.getPrixCourant(), this.acheteurCourant);
			}
			
		}else{
			System.out.println("en attente de reponse...");
		}
		
		return objetCourant.getPrixCourant();
	}

	
	
	@Override
	public synchronized int tempsEcoule(Acheteur acheteur) throws RemoteException {
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
