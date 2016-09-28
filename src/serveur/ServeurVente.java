package serveur;

import java.rmi.Remote;
import java.rmi.RemoteException;

import client.Acheteur;

public interface ServeurVente extends Remote {

	//TODO : remplacer RemoteException par Exception et mettre des throw dans les corps + faire en sorte que ca marche ;)
	
	/**
	 * Methode servant a inscrire un acheteur a une vente. Ajoute l'acheteur dans la liste des acheteurs
	 * @param login 
	 * @param acheteur
	 * @throws RemoteException
	 */
	public void inscriptionAcheteur(String login, Acheteur acheteur) throws RemoteException;
	
	/**
	 * Augmente le prix de l'objet a vendre.
	 * @param nouveauPrix le nouveau prix que le client a donne
	 * @param acheteur l'acheteur ayant encherit 
	 * @return le nouveau prix de l'objet a vendre
	 * @throws RemoteException
	 */
	public int rencherir(int nouveauPrix, Acheteur acheteur) throws RemoteException;
	
	/**
	 * je sais pas
	 * @param acheteur
	 * @return
	 * @throws RemoteException
	 */
	public int tempsEcoule(Acheteur acheteur) throws RemoteException;

}
