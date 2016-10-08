package serveur;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import client.Acheteur;

public class Donnees {

	private List<Acheteur> listeAcheteurs = new ArrayList<Acheteur>();
	private List<Objet> listeObjets = new ArrayList<Objet>();
	
	
	
	public List<Acheteur> getListeAcheteurs() {
		return listeAcheteurs;
	}

	public void setListeAcheteurs(List<Acheteur> listeAcheteurs) {
		this.listeAcheteurs = listeAcheteurs;
	}

	public List<Objet> getListeObjets() {
		return listeObjets;
	}

	public void setListeObjets(List<Objet> listeObjets) {
		this.listeObjets = listeObjets;
	}

	
	//TODO a virer par la suite
	public void initObjets(){
		Objet obj1 = new Objet("jarre","jarre de ramses 3", 250);
		Objet obj2 = new Objet("herisson","herisson des bois", 100);
		
		listeObjets.add(obj1);
		listeObjets.add(obj2);
	}
	
	
	
	/**
	 * Methode permettant a un client de s'inscrire. Ajoute le client dans la liste des acheteurs.
	 * @param login
	 * @param acheteur
	 * @throws Exception
	 */
	public void inscription(String login, Acheteur acheteur) throws Exception{
		
		for(Acheteur each : listeAcheteurs){
			if(each.getPseudo().equals(login)){
				throw new Exception("Ce nom est deja  utilise par un autre acheteur");
			}
			if(each.getPseudo().equals(acheteur.getPseudo())){
				throw new Exception("Ce nom est deja  utilise par un autre acheteur");
			}
		}
		
		listeAcheteurs.add(acheteur);
	}
	
	public boolean estInscrit(String login) throws RemoteException{
		for (Acheteur each : listeAcheteurs){
			if(each.getPseudo().equals(login)){
				return true;
			}
		}
		return false;
	}
	
	
	/**
	 * Methode permettant l'ajout d'un nouvel objet aux enchere. Ajoute l'objet dans la liste des objets a vendre.
	 * @param objet l'objet a vendre.
	 * @param acheteur le client voulant deposer l'objet.
	 * @throws Exception so l'objet est deja en vente ou si l'acheteur n'est pas encore inscrit.
	 */
	public void ajouterArticle(Objet objet, Acheteur acheteur) throws Exception{
		for(Objet each : this.listeObjets){
			if(each.equals(objet)){
				throw new Exception("Objet deja existant");
			}
		}
		for(Acheteur each : this.listeAcheteurs){
			if(each.equals(acheteur)){
				this.listeObjets.add(objet);
			}
		}
		throw new Exception("L'acheteur voulant ajouter un objet n'est pas encore inscrit");
	}
	
	
}
