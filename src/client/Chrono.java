package client;

public class Chrono extends Thread {

	private int temps;
	private boolean fini;
	private Client client;
	private long tempsDebut;
	
	public Chrono(int millisecondes, Client c) {
		temps = millisecondes;
		fini = false;
		client = c;
	}
	
	public void run() {
		fini = false;
		tempsDebut = java.lang.System.currentTimeMillis();
		while(temps <= java.lang.System.currentTimeMillis() - tempsDebut) {
			try {
				sleep(1);
			} catch (InterruptedException e) {
			}
		}
		fini = true;
		try {
			client.nouveauPrix(0);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public long getTemps() {
		return java.lang.System.currentTimeMillis() - tempsDebut;
	}

	public boolean getFini() {
		return fini;
	}
}
