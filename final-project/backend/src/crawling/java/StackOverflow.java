import org.json.JSONArray;
import org.json.JSONException;
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

public class StackOverflow {
    public static ArrayList<question> getquestions(String s) throws IOException, JSONException {
        //String s = "https://api.stackexchange.com/2.3/questions?page=1&order=desc&sort=votes&tagged=java&site=stackoverflow";
        URL url = new URL(s);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("client_id", "23433");
        conn.setRequestProperty("client_secret", "ZU2zeNAi*HJVqFdIPN33fw((");
        int responeseCode = conn.getResponseCode();
        String respomseMessage = conn.getResponseMessage();
        String contentEncoding = conn.getContentEncoding();
        System.out.println(responeseCode);
        System.out.println(respomseMessage);
        System.out.println(contentEncoding);
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader in = new BufferedReader(new InputStreamReader(new GZIPInputStream(conn.getInputStream())));
        String inputLine = null;
        while ((inputLine = in.readLine()) != null) {
            stringBuilder.append(inputLine);
        }
        in.close();
        JSONObject jsonObject = new JSONObject(stringBuilder.toString());
        JSONArray items = jsonObject.getJSONArray("items");
        ArrayList<question> questions = new ArrayList<>();

        for (Object obj : items) {
            JSONObject json = (JSONObject) obj;
            JSONArray tags = json.getJSONArray("tags");
            List<String> s_tags = new ArrayList<>();
            StringBuilder ss = new StringBuilder();
            for (int i = 0; i < tags.length(); i++) {
                //ss.append(tags.getString(i)).append(", ");
                s_tags.add(tags.getString(i));
            }
            //ss.delete(ss.length()-2, ss.length());
            questions.add(new question(json.getInt("question_id"),json.getString("title"),json.getInt("score"),json.getInt("view_count"),s_tags,json.getInt("creation_date")));
        }
        //questions.stream().forEach(System.out::println);
        return questions;
    }
}
class question{
    int id;
    String title;
    int score;
    int view_count;
    List<String> tags;
    int createdyear;

    public question(int id, String title, int score, int view_count, List<String> tags, int createdyear) {
        this.id = id;
        this.title = title;
        this.score = score;
        this.view_count = view_count;
        this.tags = tags;
        this.createdyear = createdyear;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof question)) return false;
        question question = (question) o;
        return id == question.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
