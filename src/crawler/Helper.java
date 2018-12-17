package crawler;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.FileWriter;
import java.io.IOException;

public class Helper {

    // Current file directory
    private static final String CURRENT_DIR = System.getProperty("user.dir") + "/src/crawler/";

    /**
     * remove last part of url
     * @param url String
     * @return String
     */
    public static String removeUrlLastPart(String url) {
        int index = url.lastIndexOf('/');

        return url.substring(0,index);
    }

    /**
     * concat multiple JSONArray
     * @param arrs JSONArray
     * @return JSONArray
     * @throws JSONException
     */
    public static JSONArray concatArray(JSONArray... arrs)
            throws JSONException {
        JSONArray result = new JSONArray();
        for (JSONArray arr : arrs) {
            for (int i = 0; i < arr.length(); i++) {
                result.put(arr.get(i));
            }
        }
        return result;
    }

    /**
     * convert JsonArray to Json file
     * @param jsonArray JSONArray
     */
    public static void jsonToFile(JSONArray jsonArray) {
        try (FileWriter file = new FileWriter(CURRENT_DIR + "monsters.json")) {
            file.write(jsonArray.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
