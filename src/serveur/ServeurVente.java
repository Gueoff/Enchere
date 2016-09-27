package serveur;

import java.rmi.Remote;

public interface ServeurVente extends Remote{

	public void inscriptionAcheteur(String login, InterfaceAcheteur acheteur);
	public int rencherir(int nouveauPrix, InterfaceAcheteur acheteur);
	public int tempsEcoule(InterfaceAcheteur acheteur);
}
