package client;

public class Chrono extends Thread {

	private int temps;
	private boolean fini;
	
	public Chrono(int millisecondes) {
		temps = millisecondes;
		fini = false;
	}
	
	public void run() {
		try {
			fini = false;
			sleep(temps);
			fini = true;
		} catch (InterruptedException e) {
			e.printStackTrace();
		} 
	}
	
	public boolean getFini() {
		return fini;
	}
}
