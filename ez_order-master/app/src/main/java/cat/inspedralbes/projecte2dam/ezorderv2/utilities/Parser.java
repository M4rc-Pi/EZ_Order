package cat.inspedralbes.projecte2dam.ezorderv2.utilities;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cat.inspedralbes.projecte2dam.ezorderv2.model.Comanda;
import cat.inspedralbes.projecte2dam.ezorderv2.model.Producte;

/**
 * Aquesta classe és una utilitat que parseja documents JSON.<br>
 *
 * @author Gabriel Tous Burguillos
 * @author Marc Picas Hervas
 * @version May/2021
 * @autor Mario Burgos Piedra
 */
public class Parser {

    /**
     * Mètode que retorna les dades d'un producte en format JSON per poder pujar-lo a la BD.
     * @param producte (Objecte)
     * @return String amb la info del producte
     */
    public String getJSONProducte(Producte producte) {

        String json = "{\n" +
                "      \"nom\":\"" + producte.getNom() + "\",\n" +
                "      \"descripcio\":\"" + producte.getDescripcio() + "\",\n" +
                "      \"preu\":\"" + producte.getPreu() + "\",\n" +
                "      \"alergens\":\"" + producte.isAlergens() + "\",\n" +
                "      \"picant\":\"" + producte.isPicant() + "\",\n" +
                "      \"veggie\":\"" + producte.isVeggie() + "\",\n" +
                "      \"id_categoria\":\"" + producte.getId_categoria() + "\",\n" +
                "      \"foto\":\"" + producte.getFoto() + "\"\n" +
                "    }";
        return json;
    }

    //    /**
//     * El mètode retorna el numero enter corresponent a l'id de la categoria del producte que rep per paràmetre.<br><br>
//     * El JSON en format String que rep per parametre ha de tenir aquesta estructura:<br><br>
//     * <code>
//     *     {<br>
//     *
//     *     }
//     *
//     * </code>
//     * @param json El document JSON en format String
//     * @param nomProducte El nom del producte del qual es vol saber l'id de la categoria a la que pertany
//     * @return L'id de categoria.
//     * @throws JSONException
//     */
    public String parsejaIdComanda(String json, String nomProducte) throws JSONException {
        String idComanda = null;
        JSONObject doc = new JSONObject(json);
        JSONArray records = doc.getJSONArray("records");
        JSONObject idProducte;
        for (int i = 0; i < records.length(); i++) {
            JSONObject dadesComanda = records.getJSONObject(i);
            idProducte = dadesComanda.getJSONObject("nom");
            if (nomProducte.equals(idProducte)) {
                idComanda = dadesComanda.getString("id");
                break;
            }
        }
        return idComanda;
    }

    /**
     * Crea objectes Comanda amb la informació que conté el JSON que rep per paràmetre.<br>
     * Després els afegeix a una llista.<br><br>
     * Per crear-se la comanda, es necessita coneixer la informació completa dels productes a fi de crear objectes Producte que s'afegeixen a la llista interna (cistella) propietat de l'objecte Comanda.<br><br>
     * Exemple de JSON que pot parsejar:<br>
     * <code>
     * {<br>
     * "records":[<br>
     * {<br>
     * "id" : (id de la comanda),<br>
     * "codi" : "(codi de la comanda)",<br>
     * "timestamp" : "(moment en que s'ha fet la comanda)",<br>
     * "quantitat" : (quantitat de producte),<br>
     * "preu_total" : (preu del producte x quantitat),<br>
     * "id_usuari" : (id_usuari que ha fet la comanda),<br>
     * "id_producte" : (id_producte que ha demanat),<br>
     * "estat" : "(estat de la seva preparació, si cal)",<br>
     * "taula" : (taula que ha fet la comanda)<br>
     * },<br>
     * {<br>
     * ...<br>
     * },<br>
     * {<br>
     * ...<br>
     * }<br>
     * ]<br>
     * }<br>
     * </code>
     *
     * @param json      el JSON amb les comandes
     * @param productes
     * @return un objecte List amb una comanda a cada posició
     * @throws JSONException
     * @see List
     * @see Producte
     * @see Comanda
     */
    public List<Comanda> parsejaComandes(String json, List<Producte> productes) throws JSONException {
        String codiComandaAnterior = "";
        Comanda comanda = new Comanda();
        comanda.setMapa(new HashMap<Producte, Integer>());
        List<Comanda> comandes = new ArrayList<Comanda>();

        int idComanda;
        String codi;
        int quantitat;
        int idProducte;
        int idCategoria;
        String timestamp;
//        float preuTotal;
        String estatComanda;

        JSONObject doc = new JSONObject(json);
        JSONArray records = doc.getJSONArray("records");
        for (int i = 0; i < records.length(); i++) {
            JSONObject dadesComanda = records.getJSONObject(i);
            codi = dadesComanda.getString("codi");

            if (!codiComandaAnterior.equals(codi)) {
                comanda = new Comanda();
                comanda.setCodi(codi);
                comanda.setMapa(new HashMap<Producte, Integer>());
            }
            idComanda = dadesComanda.getInt("id");
            idProducte = dadesComanda.getInt("id_producte");
            quantitat = dadesComanda.getInt("quantitat");
            timestamp = dadesComanda.getString("timestamp");
            estatComanda = dadesComanda.getString("estat");

//            preuTotal = (float) dadesComanda.getDouble("preu_total");
            for (Producte p : productes) {
                if (idProducte == p.getId()) {
                    comanda.getMapa().put(p, quantitat);
                    break;
                }
            }
            comanda.setIdComanda(idComanda);
            comanda.setEstatComanda(estatComanda);
            comanda.setTimestamp(timestamp);
            comandes.add(comanda);
        }
        return comandes;
    }


    //He fet una altre metode molt semblant perquè el de sobre no em servia. Quan poguem o revisem perquè segur que es poden fusionar.

    public List<Comanda> parsejaComandaJoinUsuariProducte(String json) throws JSONException {

        String codiComandaAnterior = "";
        List<Comanda> comandaCompleta = new ArrayList<>();
        Map<Producte, Integer> cistella = new HashMap<>();
        Map<Producte, String> estatsProductes = new HashMap<>();
        Comanda comanda = new Comanda();
        Producte producte;

        String codi;
        String timestamp;
        int quantitat;
        float preuTotal;
        int idUsuari;
        String nomUsuari;
        int idProducte;
        String estatComanda;
        int taula;
        float preuFinalComanda = 0;

        JSONObject doc = new JSONObject(json);
        JSONArray records = doc.getJSONArray("records");

        for (int i = 0; i < records.length(); i++) {

            Log.d("ParsejarComandaProduc", "Index: " + i);

            JSONObject dadesComanda = records.getJSONObject(i);
            codi = dadesComanda.getString("codi");

            if (!codiComandaAnterior.equals(codi)) {

                comanda = new Comanda();
                cistella = new HashMap<>();
                estatsProductes = new HashMap<>();

                timestamp = dadesComanda.getString("timestamp");
                taula = dadesComanda.getInt("taula");

                JSONObject dadesUsuari = dadesComanda.getJSONObject("id_usuari");
                nomUsuari = dadesUsuari.getString("nom");
                idUsuari = dadesUsuari.getInt("id");

                comanda.setCodi(codi);
                comanda.setTimestamp(timestamp);
                comanda.setCodi_taula(taula);
                comanda.setIdUsuari(idUsuari);
                comanda.setNomUsuari(nomUsuari);
                comandaCompleta.add(comanda);
                Log.d("ParsejarComandesProduc", "Nova Comanda: " + comanda.toString());
            }

            quantitat = dadesComanda.getInt("quantitat");
            preuTotal = dadesComanda.getInt("preu_total");
            preuFinalComanda += preuTotal;

            comanda.setPreu_total(preuFinalComanda);

            JSONObject dadesProducte = dadesComanda.getJSONObject("id_producte");
            idProducte = dadesProducte.getInt("id");

            String nomProducte = dadesProducte.getString("nom");
            float preuProducteUnitat = (float) dadesProducte.getDouble("preu");

            producte = new Producte(nomProducte, preuProducteUnitat);
            Log.d("ParsejarComandaProduc", "Producte: " + producte.getNom());
            cistella.put(producte, quantitat);
            comanda.setMapa(cistella);

            estatComanda = dadesComanda.getString("estat");
            estatsProductes.put(producte, estatComanda);
            comanda.setEstatsProductes(estatsProductes);

            codiComandaAnterior = codi;

            //comanda = new Comanda(codi, nomUsuari, estatComanda, preuTotal, cistella);

        }

        return comandaCompleta;
    }

    /**
     * Retorna una llista amb el nom de les categories dels productes.<br><br>
     * Tipus de JSON que pot parsejar aquest metode:<br>
     * <code>
     * {<br>
     * "records":[<br>
     * {<br>
     * "id":(id de la categoria),<br>
     * "nom":(nom de la categoria)<br>
     * },<br>
     * {<br>
     * ...<br>
     * }<br>
     * ]<br>
     * }<br>
     * </code>
     *
     * @param json
     * @return
     * @throws JSONException
     * @see List
     */
    public List<String> parsejaCategories(String json) throws JSONException {
        List<String> categories = new ArrayList<String>();
        JSONObject doc = new JSONObject(json);
        JSONArray records = doc.getJSONArray("records");
        for (int i = 0; i < records.length(); i++) {
            JSONObject dadesCategoria = records.getJSONObject(i);
            String nom = dadesCategoria.getString("nom");
            categories.add(nom);
        }
        return categories;
    }

    /**
     * Retorna una llista de Productes amb tota la seva informació parsejada desde un JSON amb aquesta aparença:<br>
     * {<br>
     * "records":[<br>
     * {<br>
     * "id": (id del producte),<br>
     * "nom":(nom del producte),<br>
     * "descripcio":(descripció),<br>
     * "preu":(preu),<br>
     * "alergens":(valor <i>int</i>),<br>
     * "picant":(valor <i>int</i>),<br>
     * "veggie":(valor <i>int</i>,<br>
     * "id_categoria": (id_categoria)<br>
     * },<br>
     * {<br>
     * ...<br>
     * }<br>
     * ]<br>
     * }<br>
     *
     * @param json (String) amb un json de la informació dels productes
     * @return ArrayList de productes
     * @throws JSONException en cas de que al parser li passin un json que no te l'estructura adequada
     * @see List
     * @see Producte
     */
    public List<Producte> parsejaProductes(String json) throws JSONException {
        Producte producte = null;
        List<Producte> productes = new ArrayList<Producte>();

        JSONObject doc = new JSONObject(json);
        JSONArray records = doc.getJSONArray("records");
        for (int i = 0; i < records.length(); i++) {
            JSONObject dadesProducte = records.getJSONObject(i);
            String nom = dadesProducte.getString("nom");
            int id = dadesProducte.getInt("id");
            int idCategoria = dadesProducte.getInt("id_categoria");
            String descripcio = dadesProducte.getString("descripcio");
            float preu = (float) dadesProducte.getDouble("preu");
            int alergens = dadesProducte.getInt("alergens");
            int picant = dadesProducte.getInt("picant");
            int veggie = dadesProducte.getInt("veggie");
            String foto = dadesProducte.getString("foto");

//            String nomCapitalize = nom.substring(0, 1).toUpperCase() + nom.substring(1);
//            producte = new Producte(nomCapitalize, id, descripcio, preu, alergens, picant, veggie);
            producte = new Producte(nom, id, descripcio, preu, alergens, picant, veggie);
            producte.setId_categoria(idCategoria);
            producte.setFoto(foto);
            productes.add(producte);
        }

        return productes;
    }

    public String parsejarIdProducte(String json) throws JSONException {

        String idProducte;

        JSONObject doc = new JSONObject(json);
        JSONArray records = doc.getJSONArray("records");
        JSONObject dadesProducte = records.getJSONObject(0);

        return idProducte = dadesProducte.getString("id");

    }

    public List<Producte> parsejarProductesCategoriaSeleccionada(String json) throws JSONException {

        Producte producte = null;
        List<Producte> productes = new ArrayList<Producte>();

        JSONObject doc = new JSONObject(json);
        JSONArray records = doc.getJSONArray("records");
        JSONObject res = records.getJSONObject(0);
        JSONArray productes_array = res.getJSONArray("PRODUCTES");

        for (int i = 0; i < productes_array.length(); i++) {
            JSONObject dadesProducte = productes_array.getJSONObject(i);
            String nom = dadesProducte.getString("nom");
            int id = dadesProducte.getInt("id");
            String descripcio = dadesProducte.getString("descripcio");
            float preu = (float) dadesProducte.getDouble("preu");
            int alergens = dadesProducte.getInt("alergens");
            int picant = dadesProducte.getInt("picant");
            int veggie = dadesProducte.getInt("veggie");

            producte = new Producte(nom, id, descripcio, preu, alergens, picant, veggie);
            productes.add(producte);

        }

        return productes;
    }

    public Map<String, String> parsejarLogin(String json) throws JSONException {

        Map<String, String> dadesLogin = new HashMap<String, String>();

        JSONObject doc = new JSONObject(json);
        JSONArray records = doc.getJSONArray("records");
        JSONObject dadesJsonLogin = records.getJSONObject(0);

        String email = dadesJsonLogin.getString("email");
        String pass = dadesJsonLogin.getString("password");
        int idUsuari = dadesJsonLogin.getInt("id");
        String tipus = dadesJsonLogin.getString("tipus");

        String idUsuariString = String.valueOf(idUsuari);

        dadesLogin.put("email", email);
        dadesLogin.put("password", pass);
        dadesLogin.put("id", idUsuariString);
        dadesLogin.put("tipus", tipus);

        return dadesLogin;
    }

    public List<String> parsejarEmails(String json) throws JSONException {

        List<String> emails = new ArrayList<>();

        JSONObject doc = new JSONObject(json);
        JSONArray records = doc.getJSONArray("records");

        for (int i = 0; i < records.length(); i++) {

            JSONObject emailObject = records.getJSONObject(i);
            String email = emailObject.getString("email");
            emails.add(email);
        }
        return emails;
    }

}
