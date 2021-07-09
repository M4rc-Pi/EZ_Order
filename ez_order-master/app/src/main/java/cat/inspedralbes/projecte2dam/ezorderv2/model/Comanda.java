package cat.inspedralbes.projecte2dam.ezorderv2.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cat.inspedralbes.projecte2dam.ezorderv2.utilities.Constants;
import cat.inspedralbes.projecte2dam.ezorderv2.utilities.RandomCodeGenerator;

/**
 * Aquesta classe és la representació abstracta d'una comanda d'un bar o restaurant.<br><br>
 * Es pot utilitzar quan es vulgui treballar amb comandes que registren un codi d'identificació alfanuméric, la taula on s'ha fet la comanda, l'identificador numéric i el nom de l'usuari que l'ha fet, una cistella de <i>Producte</i> que emmagatzema la quantitat, un estat de la comanda que podría representar si la comanda ha sigut acceptada, està en preparació, finalitzada o entregada, per exemple; un <i>timestamp</i> per situar la comanda en el temps i un preu total que és la suma del preu de cada producte que la composa multiplicat per la quantitat de productes que hi ha.<br>
 *
 * @author Gabriel Tous Burguillos
 * @author Marc Picas Hervas
 * @author Mario Burgos Piedra
 * @version May/2021
 * @see Map
 * @see Producte
 * @see RandomCodeGenerator
 */
public class Comanda {

    private  int idComanda;
    private String codi;
    private int num_taula;
    private int idUsuari; //Quan tinguem fet el tema del login, agafarem l'id de l'usuari loguejat.
    private String nomUsuari;
    private Map<Producte, Integer> mapa_producte_quantitat;
    private Constants.EstatComanda estatComanda;
    private String estatComandaString;
    private String timestamp;
    private float preu_total;
    private float preuQuantitat;
    private Map<Producte, String> estatsProductes;

    private int quantitat_anterior = 0;
    private int quantitat_final;

    private int quantitat;
    private int idProducte;

    private RandomCodeGenerator codeGenerator;

    /**
     * Construeix una comanda amb un mapa de buit que emmagatzema els productes que s'afegeixen.<br><br>
     * Necessita que li passin l'id d'un usuari i un numero de taula, donat que aquest constructor s'ha pensat perque l'usuari client del bar o restaurant l'utilitzi.<br>
     * S'inizialitza amb un número de comanda aleatori i un estat (ENVIADA).<br>
     *
     * @param cistella
     * @param idUsuari
     * @param num_taula
     */
    public Comanda(Map<Producte, Integer> cistella, int idUsuari, int num_taula) {
        mapa_producte_quantitat = cistella;
        codeGenerator = new RandomCodeGenerator();
        codi = codeGenerator.generarCodiRandom();
        //codi_taula = 1;
        estatComanda = Constants.EstatComanda.ENVIADA;
        this.idUsuari = idUsuari;
        this.num_taula = num_taula;
        estatsProductes = new HashMap<>();
    }

    /**
     * @param codi
     * @param nomUsuari
     * @param estatComanda
     * @param preuQuantitat
     * @param cistella
     */
    public Comanda(String codi, String nomUsuari, String estatComanda, float preuQuantitat, Map<Producte, Integer> cistella) {
        this.codi = codi;
        this.nomUsuari = nomUsuari;
        this.estatComandaString = estatComanda.toString();
        this.preuQuantitat = preuQuantitat;
        this.mapa_producte_quantitat = cistella;
        estatsProductes = new HashMap<>();
    }

    /**
     * Constructor buit de la comanda.<br><br>
     * En construir-se aquest objecte, s'inicialitza un <i>HashMap</i> buit i es genera un codi aleatori per la comanda.<br>
     */
    public Comanda() {
        mapa_producte_quantitat = new HashMap<>();
        codeGenerator = new RandomCodeGenerator();
        codi = codeGenerator.generarCodiRandom();
        estatsProductes = new HashMap<>();
    }

    /**
     * Afegeix <i>Producte</i> i la seva quantitat al HashMap que actua com a cistella de la comanda.<br>
     *
     * @param producte-L'objecte <i>Producte</i> que s'afegeix a la comanda.
     * @param quantitat-La       quantitat afegida.
     */
    public void afegir(Producte producte, int quantitat) {

        if (mapa_producte_quantitat.containsKey(producte)) {
            quantitat_anterior = mapa_producte_quantitat.get(producte);
        }

        quantitat_final = quantitat_anterior + quantitat;
        mapa_producte_quantitat.put(producte, quantitat_final);
    }

    /**
     * Elimina del <i>HashMap</i> que actua com a cistella la quantitat de <i>Producte</i> indicada als paràmetres de la funció.<br><br>
     * Si el producte no existeix al <i>HashMap</i>, no fa res.<br>
     * Si el producte existeix, la quantitat afegida és negativa i resulta que el <i>HashMap</i> rep un numero negatiu, aquest numero es transforma en 0.<br>
     * En altre cas, la quantitat afegida es suma a la quantitat existent de la posició que ocupa el <i>Producte</i> al <i>HashMap</i>.<br>
     *
     * @param producte-L'objecte <i>Producte</i> que s'afegeix a la comanda.
     * @param quantitat-La       quantitat de <i>Producte</i> que s'afegeix a la comanda.
     */
    public void eliminar(Producte producte, int quantitat) {
        if (mapa_producte_quantitat.containsKey(producte)) {
            quantitat_anterior = mapa_producte_quantitat.get(producte);
            quantitat_final = quantitat_anterior - quantitat;
            if (quantitat_final < 0) {
                quantitat_final = 0;
            }
            mapa_producte_quantitat.put(producte, quantitat_final);
        }
    }

    /**
     * Retorna el preu total obtingut de la suma dels preus de cada <i>Producte</i> que conté el <i>HashMap</i> multiplicat per la seva quantitat.<br>
     *
     * @return El preu total de la comanda.
     */
    public float getPreuTotal() {
        float suma = 0;
        Iterator<Map.Entry<Producte, Integer>> it = mapa_producte_quantitat.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Producte, Integer> set = (Map.Entry<Producte, Integer>) it.next();
            Producte producte = set.getKey();
            int quantitat = set.getValue();
            suma += (producte.getPreu() * quantitat);
        }
        preu_total = suma;
        return preu_total;
    }

    /**
     * Aquest mètode retorna una <i>List<String></i> on cada posició de la llista conté la informació de la comanda en format JSON.<br><br>
     * Format de sortida:<br>
     * <code><br>
     * {<br>
     * "codi" : "(<i>codi</i>)",<br>
     * "quantitat" : "(<i>quantitat</i>)",<br>
     * "preu_total" : "(<i>preu_total</i>)",<br>
     * "id_usuari" : "(<i>id_usuari</i>)",<br>
     * "id_producte" : "(<i>id_producte</i>)",<br>
     * "estat" : "(<i>estat</i>)",<br>
     * "taula" : "(<i>taula</i>)"<br>
     * }</code><br>
     *
     * @return Una llista de la informació de la comanda en format JSON.
     */
    public List<String> getJsonComanda() {

        String json;
        List<String> comandes = new ArrayList<>();

        Iterator<Map.Entry<Producte, Integer>> it = mapa_producte_quantitat.entrySet().iterator();
        while (it.hasNext()) {

            Map.Entry<Producte, Integer> set = (Map.Entry<Producte, Integer>) it.next();

            Producte producte = set.getKey();
            int quantitat = set.getValue();
            float preu_total = producte.getPreu() * quantitat;
            int producteId = producte.getId();

            json = "{\n" +
                    "      \"codi\":\"" + codi + "\",\n" +
                    "      \"quantitat\":\"" + quantitat + "\",\n" +
                    "      \"preu_total\":\"" + preu_total + "\",\n" +
                    "      \"id_usuari\":\"" + idUsuari + "\",\n" +
                    "      \"id_producte\":\"" + producteId + "\",\n" +
                    "      \"estat\":\"" + estatComanda + "\",\n" +
                    "      \"taula\":\"" + num_taula + "\"\n" +
                    "    }";

            comandes.add(json);
        }
        return comandes;
    }

    /**
     * Assigna un estat a la comanda.<br>
     *
     * @param estatComanda-L'estat de la comanda.
     */
    public void setEstatComanda(String estatComanda) {
        switch (estatComanda) {
            case "ENVIADA":
            case "null":
                this.estatComanda = Constants.EstatComanda.ENVIADA;
                break;
            case "ACCEPTADA":
                this.estatComanda = Constants.EstatComanda.ACCEPTADA;
                break;
            case "CANCELADA":
                this.estatComanda = Constants.EstatComanda.CANCELADA;
                break;
            case "EN_PREPARACIO":
                this.estatComanda = Constants.EstatComanda.EN_PREPARACIO;
                break;
            case "FINALITZADA":
                this.estatComanda = Constants.EstatComanda.FINALITZADA;
                break;
        }
    }

    public String getEstatComanda(){return estatComanda.toString();}

    public void setIdComanda(int idComanda){
        this.idComanda = idComanda;
    }
    public int getIdComanda(){
        return idComanda;
    }
    /**
     * Retorna una <i>String</i> amb el codi de la comanda.<br>
     *
     * @return El codi de la comanda.
     */
    public String getCodi() {
        return codi;
    }

    /**
     * Assigna un codi a la comanda.
     *
     * @param codi-El codi de la comanda.
     */
    public void setCodi(String codi) {
        this.codi = codi;
    }

    public void setCodi_taula(int codi_taula) {
        this.num_taula = codi_taula;
    }
    public int getCodi_taula(){return num_taula;}
    public int getIdUsuari() {
        return idUsuari;
    }
    public void setIdUsuari(int idUsuari) {
        this.idUsuari = idUsuari;
    }

    /**
     * Retorna el <i>Map</i> que conté la relació de <i>Producte</i> i la seva quantitat.<br>
     *
     * @return La relació <i>Producte</i>/quantitat que hi ha a la comanda.
     */
    public Map getMapa() {
        return mapa_producte_quantitat;
    }

    /**
     * Assigna un <i>Map</i> a la comanda.<br>
     *
     * @param mapa-El mapa que actua com a cistella de <i>Producte</i>/quantitat.
     */
    public void setMapa(Map mapa) {
        mapa_producte_quantitat = mapa;
    }

    /**
     * Assigna una marca de temps en forma d'<i>String</i>.<br>
     *
     * @param timestamp-L'<i>String</i> que conté la marca de temps.
     */
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setQuantitat(int quantitat){this.quantitat = quantitat;}
    public void setIdProducte(int idProducte){this.idProducte = idProducte;}
    public void setPreu_total(float preu_total){this.preu_total = preu_total;}
    public float getPreuQuantitat(){return preuQuantitat;}

    /**
     * Retorna el nom de l'usuari propietari de la comanda.<br>
     * @return El nom del propietari de la comanda.
     */
    public String getNomUsuari() {
        return nomUsuari;
    }

    /**
     * Assigna un nom d'usuari.<br>
     *
     * @param nomUsuari
     */
    public void setNomUsuari(String nomUsuari) {
        this.nomUsuari = nomUsuari;
    }

    /**
     * Retorna l'estat de la comanda.<br>
     *
     * @return L'estat de la comanda.
     */
    public String getEstatComandaString() {
        return estatComandaString;
    }
    public Map<Producte, String> getEstatsProductes() {
        return estatsProductes;
    }
    public void setEstatsProductes(Map<Producte, String> estatsProductes) {
        this.estatsProductes = estatsProductes;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        String mapaToString = "[ ";
        for (Map.Entry<Producte, Integer> pair : mapa_producte_quantitat.entrySet()) {
            mapaToString += " " + pair.getKey().toString() + ":" + pair.getValue() + " | ";
        }
        mapaToString += " ]";

        return "Comanda{" +
                "codi='" + codi + '\'' +
                ", codi_taula=" + num_taula +
                ", idUsuari=" + idUsuari +
                ", mapa_producte_quantitat=" + mapaToString +
                ", estatComanda=" + estatComanda +
                ", timestamp='" + timestamp + '\'' +
                ", quantitat_anterior=" + quantitat_anterior +
                ", quantitat_final=" + quantitat_final +
                '}';
    }
}

