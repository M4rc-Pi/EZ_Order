package cat.inspedralbes.projecte2dam.ezorderv2.model;

/**
 * Classe que hereda d'Usuari.  Representa els usuaris de tipus treballador de l'aplicació.<br>
 * @author Gabriel Tous Burguillos
 * @author Marc Picas Hervas
 * @author Mario Burgos Piedra
 * @version May/2021
 * @see Usuari
 * {@link Usuari}
 */
public class UsuariTreballador extends Usuari {

    private int numComandesRegistrades;
    private float caixaDiariaGenerada;

    /**
     * Constructor d'un usuari amb les dades nom, primer cognom, segon cognom, email i password.<br>
     *
     * @param nom     El nom de l'usuari
     * @param cognom  El primer cognom de l'usuari
     * @param cognom2 El segon cognom de l'usuari
     * @param email   L'adreça d'email de l'usuari
     * @param pass    El password de l'usuari
     */
    public UsuariTreballador(String nom, String cognom, String cognom2, String email, String pass) {
        super(nom, cognom, cognom2, email, pass);
    }

    /**
     * Retorna el numero de comandes que ha registrat el treballador.<br>
     * @return el numero de comandes
     */
    public int getNumComandesRegistrades() {
        return numComandesRegistrades;
    }

    /**
     * Assigna un numero de comandes que ha registrat el treballador.<br>
     * @param numComandesRegistrades
     */
    public void setNumComandesRegistrades(int numComandesRegistrades) {
        this.numComandesRegistrades = numComandesRegistrades;
    }

    /**
     * Retorna el total de la recaudació diaria del treballador.<br>
     * @return el total diari de recaudació
     */
    public float getCaixaDiariaGenerada() {
        return caixaDiariaGenerada;
    }

    /**
     * Assigna el total de la recaudació diaria del treballador.
     * @param caixaDiariaGenerada
     */
    public void setCaixaDiariaGenerada(float caixaDiariaGenerada) {
        this.caixaDiariaGenerada = caixaDiariaGenerada;
    }

}
