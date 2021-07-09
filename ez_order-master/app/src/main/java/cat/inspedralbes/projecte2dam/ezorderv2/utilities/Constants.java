package cat.inspedralbes.projecte2dam.ezorderv2.utilities;

public class Constants {

    // QR
    public static final String KEY_QR_CODE = "001";

    //CONNEXIO
    public static final String IP = "158.101.208.160";
    public static final String PORT = "8080";
    public static final String URL_BASE = "http://" + IP + ":" + PORT + "/api.php/records/";

    //TAULES BD
    public static final String URL_COMANDES = URL_BASE+"COMANDES/";
    public static final String URL_TOTS_ELS_PRODUCTES = URL_BASE+"PRODUCTES/";
    public static final String URL_CATEGORIES = URL_BASE+"CATEGORIES/";
    public static final String URL_PRODUCTES_DE_CATEGORIA = URL_BASE+"CATEGORIES?join=PRODUCTES&filter=nom,eq,"; //hay que añadir el nombre de la categoria de que quieras los productos
    public static final String URL_USUARIS = URL_BASE+"USUARIS/";


    public static final int ID_USUARI_CAMBRER = 22;

    //enumeracio per l'estat de la comanda
    public enum EstatComanda{
        ENVIADA ,ACCEPTADA, EN_PREPARACIO, CANCELADA, FINALITZADA
    }

}
