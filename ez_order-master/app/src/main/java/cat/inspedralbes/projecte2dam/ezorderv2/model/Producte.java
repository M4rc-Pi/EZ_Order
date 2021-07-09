package cat.inspedralbes.projecte2dam.ezorderv2.model;

import java.io.Serializable;

/**
 * Aquesta classe és la representació abstracta d'un producte alimentari.<br><br>
 *
 * Es pot utilitzar en qualsevol tipus de negoci on es vulgui treballar amb productes identificats per un numero enter i que
 * es vulgui considerar el seu nom, descripció, preu, i la seva classificació de si és un producte picant, apte per vegetarians
 * o si pot provocar reaccions al·lèrgiques.<br>
 * @author Gabriel Tous Burguillos
 * @author Marc Picas Hervas
 * @author Mario Burgos Piedra
 * @version May/2021
 */
public class Producte implements Serializable {

    private int id;
    private int id_categoria;
    private String nom;
    private String descripcio;
    private float preu;
    private int alergens;
    private int picant;
    private int veggie;
    private String foto;

    /**
     * Crea un Producte amb nom i preu.<br>
     * @param nom-El nom del producte.
     * @param preu-El preu del producte.
     */
    public Producte (String nom, float preu){
        this.nom = nom;
        this.preu = preu;
    }

    /**
     * Crea un producte amb un id, nom, descripció, preu i classificació de si picant, apte per vegetarians o si conté ingredients que poden causar reaccions al·lèrguiques.<br>
     * @param nom-El nom del producte.
     * @param id-El numero d'identificació del producte.
     * @param descripcio-La descripció del producte.
     * @param preu-El preu del producte.
     * @param alergens-El valor serà 0 si el producte no està classificat com a possible al·lèrgen, o 1 en cas positiu.
     * @param picant-El valor serà 0 si el producte no conté ingredients picants, o 1 en cas positiu.
     * @param veggie-El valor serà 0 si el producte no és apte per a vegetarians, o 1 en cas positiu.
     */
    public Producte(String nom, int id, String descripcio, float preu, int alergens, int picant, int veggie) {
        this.nom = nom;
        this.descripcio = descripcio;
        this.preu = preu;
        this.alergens = alergens;
        this.picant = picant;
        this.veggie = veggie;
        this.id = id;
    }

    public Producte() {

    }

    /**
     * Retorna una <i>String</i> amb el nom del producte.<br>
     * @return El nom del producte.
     */
    public String getNom() {
        return nom;
    }
    /**
     * Retorna una <i>String</i> amb la descripció del producte.<br>
     * @return La descripció del producte.
     */
    public String getDescripcio() {
        return descripcio;
    }
    /**
     * Retorna un <i>float</i> que és el preu del producte.<br>
     * @return El preu del producte.
     */
    public float getPreu() {
        return preu;
    }
    /**
     * Retorna un valor numeric per identificar si el producte conté ingredients <b>al·lèrgens</b><br>
     * @return 0 si el producte no conté ingredients al·lèrgens. 1 en cas positiu.
     */
    public int isAlergens() {
        return alergens;
    }
    /**
     * Retorna un valor numeric per identificar si el producte conté ingredients <b>picants</b><br>
     * @return 0 si el producte no conté ingredients picants. 1 en cas positiu.
     */
    public int isPicant() {
        return picant;
    }
    /**
     * Retorna un valor numeric per identificar si el producte està classificat com a apte per a <b>vegetarians</b><br>
     * @return 0 si el producte no es apte per a vegetarians. 1 en cas positiu.
     */
    public int isVeggie() {
        return veggie;
    }
    /**
     * Retorna el numero identificador del producte.<br>
     * @return L'id del producte.
     */
    public int getId() { return id; }
    /**
     * Assigna un numero identificador al producte.<br>
     * @param id-El numero d'identificació del producte.
     */
    public void setId(int id) {
        this.id = id;
    }
    public int getId_categoria(){return id_categoria;}
    /**
     * Assigna un numero d'identificació de categoría a la que pertany el producte.<br>
     * @param id_categoria-El numero d'identificació de la categoría a la que pertany el producte.
     */
    public void setId_categoria(int id_categoria) {
        this.id_categoria = id_categoria;
    }
    /**
     * Assigna un nom al producte.<br>
     * @param nom-El nom del producte.
     */
    public void setNom(String nom) {
        this.nom = nom;
    }
    /**
     * Assigna un text descriptiu de la informació del producte.<br>
      * @param descripcio-La descripció del producte.
     */
    public void setDescripcio(String descripcio) {
        this.descripcio = descripcio;
    }

    /**
     * Assigna un preu al producte.<br>
     * @param preu-El preu del producte.
     */
    public void setPreu(float preu) {
        this.preu = preu;
    }

    /**
     * Assigna un valor numéric a la classificació d'al·lèrgen del producte.<br>
     * @param alergens
     */
    public void setAlergens(int alergens) {
        this.alergens = alergens;
    }
    /**
     * Assigna un valor numéric a la classificació picant del producte.<br>
     * @param picant
     */
    public void setPicant(int picant) {
        this.picant = picant;
    }
    /**
     * Assigna un valor numéric a la classificació de vegetarià del producte.<br>
     * @param veggie
     */
    public void setVeggie(int veggie) {
        this.veggie = veggie;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
