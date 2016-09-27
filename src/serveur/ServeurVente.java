package serveur;

import java.rmi.Remote;

import client.Acheteur;

public interface ServeurVente extends Remote{

	public void inscriptionAcheteur(String login, Acheteur acheteur);
	public int rencherir(int nouveauPrix, Acheteur acheteur);
	public int tempsEcoule(Acheteur acheteur);
}
