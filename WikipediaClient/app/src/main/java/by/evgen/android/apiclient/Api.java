package by.evgen.android.apiclient;

/**
 * Created by evgen on 15.11.2014.
 */
public class Api {

    public static final String BASE_PATH = "https://en.wikipedia.org/w/api.php?";
    public static final String GEOSEARCH_GET = BASE_PATH + "action=query&list=geosearch&format=json&gslimit=100&gsradius=10000&gscoord=";
    public static final String IMAGEVIEW_GET = BASE_PATH + "action=query&prop=pageimages&piprop=thumbnail&format=json&titles=";
    public static final String SEARCH_GET = BASE_PATH + "action=query&list=search&format=json&";
    public static final String CONTENTS_GET = BASE_PATH + "action=mobileview&format=json&page=";
    public static final String RANDOM_GET = BASE_PATH + "action=query&format=json&list=wikigrokrandom";
    public static final String MOBILE_GET = BASE_PATH + "action=mobileview&sections=all&format=json&page=";
    public static final String RANDOM_PAGE_GET = BASE_PATH + "action=query&list=categorymembers&format=json&cmnamespace=0&cmlimit=100&cmtitle=Category:Physics";
    public static final String EXTRAS_PAGE_GET = BASE_PATH + "action=query&prop=extracts&format=json&exintro=1&explaintext=1&titles=";
    public static final String MAIN_URL_HTML = "https://en.wikipedia.org/wiki/";
    public static final String MAIN_URL = "https://en.wikipedia.org/";
    public static final String IDTITLE_GET = BASE_PATH + "action=query&format=json&";


    public static final String BASE_PATH_VK = "https://api.vk.com/method/";
    public static final String V_VK = "5.28";
    public static final String VKNOTES_GET = BASE_PATH_VK + "notes.add?privacy=3&comment_privacy=3&v=" + V_VK + "&title=";
    public static final String VKFOTOS_GET = BASE_PATH_VK + "users.get?fields=photo_200_orig,city,verified&name_case=Nom&version=" + V_VK;
    public static final String VKNOTES_ALL_GET =  BASE_PATH_VK + "notes.get?fields=notes$count=100&sort=0&v=" + V_VK;
    public static final String VKLIKEIS_GET = BASE_PATH_VK + "likes.isLiked?type=note&v=" + V_VK + "&item_id=";
    public static final String VKLIKE_GET = BASE_PATH_VK + "likes.add?type=note&v=" + V_VK + "&item_id=";
    public static final String STORAGE_SET = BASE_PATH_VK + "storage.set?v=" + V_VK + "&key=";
    public static final String STORAGE_GET = BASE_PATH_VK + "storage.get?v=" + V_VK + "&keys=";
    public static final String STORAGE_KEYS_GET = BASE_PATH_VK + "storage.getKeys?v=" + V_VK;

    public static String getSearchGet(int count, int offset) {
        return Api.SEARCH_GET + "srlimit=" + count + "&sroffset=" + offset + "&srsearch=";
    }

    public static String getStorage(String key, String value) {
        return Api.STORAGE_SET + key +"&value=" + value;
    }

    public static String getNotes(String value) {
        return Api.VKNOTES_GET + value + "&text=" + Api.MAIN_URL_HTML + value;
    }

}
