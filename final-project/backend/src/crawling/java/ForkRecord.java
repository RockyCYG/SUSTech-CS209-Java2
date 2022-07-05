import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.zip.GZIPInputStream;

public class ForkRecord {
    public static List<Fork> getfork(String s) throws IOException {
        //String s = "https://api.stackexchange.com/2.3/questions?page=1&order=desc&sort=votes&tagged=java&site=stackoverflow";
        URL url = new URL(s);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization","Token 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");
        conn.connect();

        int responeseCode = conn.getResponseCode();
        String respomseMessage = conn.getResponseMessage();
        String contentEncoding = conn.getContentEncoding();
        System.out.println(responeseCode);
        System.out.println(respomseMessage);
        System.out.println(contentEncoding);
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine = null;
        while ((inputLine = in.readLine()) != null) {
            stringBuilder.append(inputLine);
        }
        in.close();
 //       JSONObject jsonObject = new JSONObject(stringBuilder.toString());
 //       JSONArray items = jsonObject.getJSONArray("items");
        JSONArray items = new JSONArray(stringBuilder.toString());
        ArrayList<Fork> forks = new ArrayList<>();

        for (Object obj : items) {
            JSONObject json = (JSONObject) obj;
            //ss.delete(ss.length()-2, ss.length());
            forks.add(new Fork(json.getInt("id"),null, json.getString("created_at")));

        }
        //questions.stream().forEach(System.out::println);
        return forks;
    }
}
class Fork{
    int id;
    String title;
    String time;

    public Fork(int id, String title, String time) {
        this.id = id;
        this.title = title;
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Fork)) return false;
        Fork fork = (Fork) o;
        return id == fork.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

