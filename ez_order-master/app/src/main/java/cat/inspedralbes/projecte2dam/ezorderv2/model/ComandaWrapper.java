package cat.inspedralbes.projecte2dam.ezorderv2.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cat.inspedralbes.projecte2dam.ezorderv2.utilities.RandomCodeGenerator;

public class ComandaWrapper {

    //ESTATS DE LES COMANDES
    public static final String CANCELADA = "CANCELADA";
    public static final String ENVIADA = "ENVIADA";
    public static final String ACCEPTADA = "ACCEPTADA";
    public static final String EN_PREPARACIO = "EN PREPARACIO";
    public static final String FINALITZADA = "FINALITZADA";

    //atributs de la BD que identifiquen la comanda
    private int id;
    private String codi;
    private int numTaula;
    private String estatComanda;
    private String timeStamp;
    private Usuari usuari;
    private Map<Producte, Integer> cistella;
    private float preuTotal;

    public ComandaWrapper(Usuari usuari, int numTaula){
        this.usuari = usuari;
        this.numTaula = numTaula;
        codi= new RandomCodeGenerator().generarCodiRandom();
        cistella = new HashMap<Producte, Integer>();
        estatComanda = ENVIADA;
    }

    /**
     * Afegeix <i>Producte</i> i la seva quantitat al HashMap que actua com a cistella de la comanda.<br>
     *
     * @param producte-L'objecte <i>Producte</i> que s'afegeix a la comanda.
     * @param quantitat-La       quantitat afegida.
     */
    public void afegir(Producte producte, int quantitat) {
        int quantitat_anterior = 0, quantitat_final;
        if (cistella.containsKey(producte)) {
            quantitat_anterior = cistella.get(producte);
        }
        quantitat_final = quantitat_anterior + quantitat;
        cistella.put(producte, quantitat_final);
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
        int quantitat_anterior = 0, quantitat_final;
        if (cistella.containsKey(producte)) {
            quantitat_anterior = cistella.get(producte);
            quantitat_final = quantitat_anterior - quantitat;
            if (quantitat_final < 0) {
                quantitat_final = 0;
            }
            cistella.put(producte, quantitat_final);
        }
    }

    /**
     * Retorna el preu total obtingut de la suma dels preus de cada <i>Producte</i> que conté el <i>HashMap</i> multiplicat per la seva quantitat.<br>
     *
     * @return El preu total de la comanda.
     */
    public float getPreuTotal() {
        float suma = 0;
        Iterator<Map.Entry<Producte, Integer>> it = cistella.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Producte, Integer> set = (Map.Entry<Producte, Integer>) it.next();
            Producte producte = set.getKey();
            int quantitat = set.getValue();
            suma += (producte.getPreu() * quantitat);
        }
        preuTotal = suma;
        return preuTotal;
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
        Iterator<Map.Entry<Producte, Integer>> it = cistella.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Producte, Integer> set = (Map.Entry<Producte, Integer>) it.next();
            Producte producte = set.getKey();
            int quantitat = set.getValue();
            float preu = producte.getPreu() * quantitat;
            json = "{\n" +
                    "      \"codi\":\"" + codi + "\",\n" +
                    "      \"quantitat\":\"" + quantitat + "\",\n" +
                    "      \"preu_total\":\"" + preu + "\",\n" +
                    "      \"id_usuari\":\"" + usuari.getID() + "\",\n" +
                    "      \"id_producte\":\"" + producte.getId() + "\",\n" +
                    "      \"estat\":\"" + estatComanda + "\",\n" +
                    "      \"taula\":\"" + numTaula + "\"\n" +
                    "    }";
            comandes.add(json);
        }
        return comandes;
    }

    public String getEstatComanda(){ return estatComanda; }
    public void setEstatComanda(String estatComanda) { this.estatComanda = estatComanda; }
    public void setID(int idComanda){
        this.id = idComanda;
    }
    public int getID(){
        return id;
    }
    public String getCodi() {
        return codi;
    }
    public void setNumTaula(int numTaula) {
        this.numTaula = numTaula;
    }
    public int getNumTaula(){ return numTaula; }
    public Usuari getUsuari() {
        return usuari;
    }
    public void setUsuari(Usuari usuari) {
        this.usuari = usuari;
    }
    public Map<Producte, Integer> getCistella() { return cistella; }
    public void setCistella(Map<Producte, Integer> cistella) {
        this.cistella = cistella;
    }
    public String getTimeStamp(){return timeStamp;}

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        String mapaToString = "[ ";
        for (Map.Entry<Producte, Integer> pair : cistella.entrySet()) {
            mapaToString += " " + pair.getKey().toString() + ":" + pair.getValue() + " | ";
        }
        mapaToString += " ]";

        return "Comanda{" +
                "codi='" + codi + '\'' +
                ", codi_taula=" + numTaula +
                ", idUsuari=" + usuari.getID() +
                ", mapa_producte_quantitat=" + mapaToString +
                ", estatComanda=" + estatComanda +
                ", timestamp='" + timeStamp + '\'' +
                '}';
    }
}
