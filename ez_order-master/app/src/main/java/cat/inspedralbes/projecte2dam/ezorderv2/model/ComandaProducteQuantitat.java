package cat.inspedralbes.projecte2dam.ezorderv2.model;

/**
 * Aquesta classe agrega Comanda i Producte a fi de tenir en un mateix objecte el codi de la comanda i el producte que s'ha demanat. <br>
 * @author Gabriel Tous Burguillos
 * @author Marc Picas Hervas
 * @author Mario Burgos Piedra
 * @version May/2021
 * @see Usuari
 * {@link Usuari}
 */
public class ComandaProducteQuantitat {

    private Comanda comanda;
    private Producte producte;
    private int quantitat;
    /**
     * Constructor que rep el numero de comanda, un objecte Producte i la quantitat.<br>
     * @param comanda
     * @param producte
     * @param quantitat
     */
    public ComandaProducteQuantitat(Comanda comanda, Producte producte, int quantitat){
        this.comanda = comanda;
        this.producte = producte;
        this.quantitat = quantitat;
    }
    /**
     * Retorna el codi de la comanda
     * @return El codi de la comanda
     */
    public Comanda getComanda() {
        return comanda;
    }
    /**
     * Assigna el codi de la comanda
     * @param comanda
     */
    public void setComanda(Comanda comanda) {
        this.comanda = comanda;
    }
    /**
     * Retorna el Producte que s'ha demanat en la comanda.<br>
     * @return un objecte Producte
     */
    public Producte getProducte() {
        return producte;
    }
    /**
     * Assigna el Producte a la comanda
     * @param producte
     */
    public void setProducte(Producte producte) {
        this.producte = producte;
    }
    /**
     * Retorna la quantitat de Producte que hi ha en la comanda.<br>
     * @return un int
     */
    public int getQuantitat() {
        return quantitat;
    }
    /**
     * Assigna la quantitat de producte que hi ha en la comanda.<br>
     * @param quantitat
     */
    public void setQuantitat(int quantitat) {
        this.quantitat = quantitat;
    }
}