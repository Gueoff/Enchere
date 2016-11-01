package client;

public class Chrono extends Thread {

	private Client client;
	private long tempsFin;
	private long tempsEcoule;
	private boolean enCours = false;
	
	public Chrono(long secondes, Client c) {
		tempsFin = secondes;
		client = c;
	}
	
	public void run() {
		while(true) {
			if(enCours) {
				System.out.println("Début du chrono.");
				tempsEcoule = 0;
				while((tempsFin * 1000)>= tempsEcoule && enCours) {
					try {
						sleep(1); // Attends 1ms
						tempsEcoule++;
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				System.out.println("Fin du chrono.");
				if(enCours) {
					try {
						client.encherir(-1);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				enCours = false;
			}
			System.out.print(""); // A mettre apparemment, histoire des boucles vides. Comprends pas.
		}
	}
	
	public long getTemps() {
		return tempsEcoule;
	}

	public void demarrer() {
		enCours = true;
	}
	
	public void arreter() {
		enCours = false;
	}
	
	public boolean getFini() {
		return enCours;
	}
}
