import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class Main {

    public static final String website = "https://pokeapi.co/api/v2/pokemon/";

    public static void main(String[] args) throws IOException {
        String name = "pikachu";
        URL url = new URL(website + name);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String json = "";
        String line = null;
        while ((line = reader.readLine()) != null) {
            json += line + "\n";
        }
        reader.close();
        connection.disconnect();
        R data = JSON.parseObject(json, R.class);
        System.out.println("Name: " + name);
        System.out.println("Height: " + data.getHeight() + ", Weight: " + data.getWeight());
        System.out.println("Abilities: " + data.getAbilities());
    }

}

@Data
class R {
    private String name;
    private int height;
    private int weight;
    private List<Object> abilities;
}
