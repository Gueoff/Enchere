package serveur;

import java.rmi.Remote;
import java.rmi.RemoteException;

import client.Acheteur;

public interface ServeurVente extends Remote {

	public void inscriptionAcheteur(String login, Acheteur acheteur) throws RemoteException;
	public int rencherir(int nouveauPrix, Acheteur acheteur) throws RemoteException;
	public int tempsEcoule(Acheteur acheteur) throws RemoteException;

}
