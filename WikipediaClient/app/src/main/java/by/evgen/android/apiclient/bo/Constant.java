package by.evgen.android.apiclient.bo;

/**
 * Created by User on 29.01.2015.
 */
public class Constant {

    public static String getGeosearch() {
        return GEOSEARCH;
    }

    private static final String GEOSEARCH = "geosearch";


    public static String getQuery() {
        return QUERY;
    }

    private static final String QUERY = "query";

    public static String getPages() {
        return PAGES;
    }

    private static final String PAGES = "pages";

    public static String getEncode(String str){
        str.replaceAll(" ", "_");
        return str;
    }

}
