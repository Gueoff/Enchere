package serveur;

public class Objet {

	private String nom;
	private String description;
	private int prixBase;
	private int prixCourant;
	private boolean disponible;
	
	
	
	public Objet(String nom, String description, int prixBase) {
		super();
		this.nom = nom;
		this.description = description;
		this.prixBase = prixBase;
		this.prixCourant = prixBase;
		this.disponible = true;
	}
	
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getPrixBase() {
		return prixBase;
	}
	public void setPrixBase(int prixBase) {
		this.prixBase = prixBase;
	}
	public boolean isDisponible() {
		return disponible;
	}
	public void setDisponible(boolean disponible) {
		this.disponible = disponible;
	}
	public int getPrixCourant() {
		return prixCourant;
	}
	public void setPrixCourant(int prixCourant) {
		this.prixCourant = prixCourant;
	}
	
	
}
