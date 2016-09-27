package serveur;

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

	public void initObjets(){
		Objet obj1 = new Objet("jarre","jarre de ramses 3", 250);
		Objet obj2 = new Objet("herisson","herisson des bois", 100);
		
		listeObjets.add(obj1);
		listeObjets.add(obj2);
	}
	
	public void inscription(String login, Acheteur acheteur) throws Exception{
		
		for(Acheteur each : listeAcheteurs){
			if(each.getPseudo().equals(login)){
				throw new Exception("Ce nom est déjà utilisé par un autre acheteur");
			}
			if(each.getPseudo().equals(acheteur.getPseudo())){
				throw new Exception("Ce nom est déjà utilisé par un autre acheteur");
			}
		}
		
		listeAcheteurs.add(acheteur);
		
	}
}
