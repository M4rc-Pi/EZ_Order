package cat.inspedralbes.projecte2dam.ezorderv2.model;

/**
 * Aquesta classe representa un Usuari de tipus Client.  Hereda d'Usuari.<br><br>
 *
 * Es pot utilitzar quan es vulgui treballar amb la informació d'usuaris que contingui nom, cognoms, email i password.<br>
 * @author Gabriel Tous Burguillos
 * @author Marc Picas Hervas
 * @author Mario Burgos Piedra
 * @version May/2021
 */
public class UsuariClient extends Usuari{

    private int numComandesRealitzades;

    /**
     * Constructor d'un usuari amb les dades nom, primer cognom, segon cognom, email i password.<br>
     *
     * @param nom     El nom de l'usuari
     * @param cognom  El primer cognom de l'usuari
     * @param cognom2 El segon cognom de l'usuari
     * @param email   L'adreça d'email de l'usuari
     * @param pass    El password de l'usuari
     */
    public UsuariClient(String nom, String cognom, String cognom2, String email, String pass) {
        super(nom, cognom, cognom2, email, pass);
    }

    /**
     * Assigna un numero de comandes realitzades.<br>
     * @param numComandesRealitzades
     */
    public void setNumComandesRealitzades(int numComandesRealitzades){this.numComandesRealitzades = numComandesRealitzades;}

    /**
     * Retorna el numero de comandes que ha realitzat l'usuari client.<br>
     * @return
     */
    public int getNumComandesRealitzades(){return numComandesRealitzades;}

    /**
     * Incrementa en 1 el numero de comandes realitzades per l'usuari.
     */
    public void incrementaNumComandesRealitzades(){
        numComandesRealitzades += 1;
    }
}
