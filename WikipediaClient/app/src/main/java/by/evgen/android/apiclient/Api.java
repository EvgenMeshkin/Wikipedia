package by.evgen.android.apiclient;

import android.util.Base64;

import java.io.UnsupportedEncodingException;

import by.evgen.android.apiclient.utils.Log;

/**
 * Created by evgen on 15.11.2014.
 */
public class Api {

    public static final String BASE_PATH = "https://en.wikipedia.org/w/api.php?";
    public static final String GEOSEARCH_GET = BASE_PATH + "action=query&list=geosearch&format=json&gslimit=100&gsradius=10000&gscoord=";
    public static final String IMAGEVIEW_GET = BASE_PATH + "action=query&prop=pageimages&piprop=thumbnail&format=json&titles=";
    public static final String SEARCH_GET = BASE_PATH + "action=query&list=search&format=json&";//"https://en.wikipedia.org/w/api.php?action=query&generator=search&gsrlimit=50&prop=info&gsrsearch=meaning";
    public static final String CONTENTS_GET = BASE_PATH + "action=mobileview&format=json&page=";
    public static final String RANDOM_GET = BASE_PATH + "action=query&format=json&list=wikigrokrandom";
    public static final String MOBILE_GET = BASE_PATH + "action=mobileview&sections=all&format=json&page=";
    public static final String RANDOM_PAGE_GET = BASE_PATH + "action=query&list=categorymembers&format=json&cmnamespace=0&cmlimit=100&cmtitle=Category:Physics";
    public static final String EXTRAS_PAGE_GET = BASE_PATH + "action=query&prop=extracts&format=json&exintro=1&explaintext=1&titles=";
    public static final String MAIN_URL_HTML = "https://en.wikipedia.org/wiki/";
    public static final String MAIN_URL = "https://en.wikipedia.org/";


    public static final String BASE_PATH_VK = "https://api.vk.com/method/";
    public static final String VKNOTES_GET = BASE_PATH_VK + "notes.add?privacy=3&comment_privacy=3&v=5.26&title=";
    public static final String VKFOTOS_GET = BASE_PATH_VK + "users.get?fields=photo_200_orig,city,verified&name_case=Nom&version=5.27";
    public static final String VKNOTES_ALL_GET =  BASE_PATH_VK + "notes.get?fields=notes$count=100&sort=0&v=5.26";
    public static final String VKLIKEIS_GET = BASE_PATH_VK + "likes.isLiked?type=note&v=5.26&item_id=";
    public static final String VKLIKE_GET = BASE_PATH_VK + "likes.add?type=note&v=5.26&item_id=";
    public static final String STORAGE_SET = BASE_PATH_VK + "storage.set?v=5.28&key=";
    public static final String STORAGE_KEYS_GET = BASE_PATH_VK + "storage.getKeys?v=5.28";

    public static String getStorageKeysGet(int count, int offset) {
        String stor = STORAGE_KEYS_GET + "&count=" + count + "&sroffset=" + offset;
        return stor;
    }

    public static String getSearchGet(int count, int offset) {
        String search = Api.SEARCH_GET + "srlimit=" + count + "&sroffset=" + offset + "&srsearch=";
        return search;
    }

    public static byte[] df;

    public static String getStorage(String value) {
        try {
            df = value.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String str = Api.STORAGE_SET + Base64.encodeToString(df, 0) +"&value=" + value;
        Log.text(Api.class, Base64.encodeToString(df, 0));
        return str;
    }

    public static String getNotes(String value) {
        String str = Api.VKNOTES_GET + value + "&text=" + Api.MAIN_URL_HTML + value;
        return str;
    }


}
