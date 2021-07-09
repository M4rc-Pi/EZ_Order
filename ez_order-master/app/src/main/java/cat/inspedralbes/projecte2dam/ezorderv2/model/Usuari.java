package cat.inspedralbes.projecte2dam.ezorderv2.model;

/**
 * Classe abstracta que representa els usuaris de l'aplicació.<br>
 *
 * Es pot utilitzar quan es vulgui treballar amb la informació d'usuaris que contingui nom, cognoms, email i password.<br>
 * @author Gabriel Tous Burguillos
 * @author Marc Picas Hervas
 * @author Mario Burgos Piedra
 * @version May/2021
 */
public abstract class Usuari {

    private String id;
    private String nom;
    private String cognom;
    private String cognom2;
    private String email;
    private String pass;

    /**
     * Constructor d'un usuari amb les dades nom, primer cognom, segon cognom, email i password.<br>
     * @param nom El nom de l'usuari
     * @param cognom El primer cognom de l'usuari
     * @param cognom2 El segon cognom de l'usuari
     * @param email L'adreça d'email de l'usuari
     * @param pass El password de l'usuari
     */
    public Usuari (String nom, String cognom, String cognom2, String email, String pass){
        this.nom = nom;
        this.cognom = cognom;
        this.cognom2 = cognom2;
        this.email = email;
        this.pass = pass;
    }


    public String getID() {
        return id;
    }
    public void setId(String id){
        this.id = id;
    }

    /**
     * Retorna el nom de l'usuari.<br>
     * @return El nom de l'usuari.
     */
    public String getNom() {
        return nom;
    }
    /**
     * Assigna un nom a l'usuari.<br>
     * @param nom el nom que es vol assignar.
     */
    public void setNom(String nom) {
        this.nom = nom;
    }
    /**
     * Retorna el primer cognom de l'usuari.<br>
     * @return El primer cognom de l'usuari.
     */
    public String getCognom() {
        return cognom;
    }
    /**
     * Assigna el primer cognom de l'usuari.<br>
     * @param cognom el primer cognom que es vol assignar.
     */
    public void setCognom(String cognom) {
        this.cognom = cognom;
    }
    /**
     * Retorna el segon cognom de l'usuari.<br>
     * @return El segon cognom de l'usuari.
     */
    public String getCognom2() {
        return cognom2;
    }
    /**
     * Assigna el segon cognom de l'usuari.<br>
     * @param cognom2 el segon cognom que es vol assignar.
     */
    public void setCognom2(String cognom2) {
        this.cognom2 = cognom2;
    }
    /**
     * Retorna l'adreça d'email de l'usuari.<br>
     * @return L'adreça d'email de l'usuari.
     */
    public String getEmail() {
        return email;
    }
    /**
     * Assigna una adreça d'email a l'usuari.<br>
     * @param email l'adreça d'email que es vol assignar.
     */
    public void setEmail(String email) {
        this.email = email;
    }
    /**
     * Retorna el password de l'usuari.<br>
     * @return El password de l'usuari.
     */
    public String getPass() {
        return pass;
    }

    /**
     * Assigna un password a l'usuari.<br>
     * @param pass El password que es vol assignar.
     */
    public void setPass(String pass) {
        this.pass = pass;
    }
}
