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

public class Github_rest {
    public static List<repositories> getrepos(String s , int year) throws IOException {
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
        JSONObject jsonObject = new JSONObject(stringBuilder.toString());
        JSONArray items = jsonObject.getJSONArray("items");
        ArrayList<repositories> repositories = new ArrayList<>();

        for (Object obj : items) {
            JSONObject json = (JSONObject) obj;
            JSONArray tags = json.getJSONArray("topics");
            List<String> s_tags = new ArrayList<>();
            StringBuilder ss = new StringBuilder();
            for (int i = 0; i < tags.length(); i++) {
                s_tags.add(tags.getString(i));
            }
            //ss.delete(ss.length()-2, ss.length());
            repositories.add(new repositories(json.getInt("id"),json.getString("name"),json.getInt("watchers"),json.getInt("forks"),json.getInt("open_issues"),s_tags,json.getString("created_at"),json.getString("updated_at")));

        }
        //questions.stream().forEach(System.out::println);
        return repositories;
    }
}
class repositories{
    int id;
    String title;
    int watchers;
    int forks;
    int issues;
    List<String> tags;
    String created_time;
    String uppdate_time;

    public repositories(int id, String title, int watchers, int forks, int issues, List<String> tags, String created_time, String uppdate_time) {
        this.id = id;
        this.title = title;
        this.watchers = watchers;
        this.forks = forks;
        this.issues = issues;
        this.tags = tags;
        this.created_time = created_time;
        this.uppdate_time = uppdate_time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof repositories)) return false;
        repositories that = (repositories) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

