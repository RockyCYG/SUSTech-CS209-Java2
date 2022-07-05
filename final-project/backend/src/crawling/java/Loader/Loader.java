package Loader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.Properties;

public class Loader {
    private static Connection con = null;
    private static PreparedStatement stmt = null;
    static int BATCH_SIZE = 500;

    private static void openDB(String host, String dbname,
                               String user, String pwd) {
        try {//find the driver
            Class.forName("org.postgresql.Driver");
        } catch (Exception e) {
            System.err.println("Cannot find the Postgres driver. Check CLASSPATH.");
            System.exit(1);
        }
        String url = "jdbc:postgresql://" + host + "/" + dbname;
        Properties props = new Properties();
        props.setProperty("user", user);
        props.setProperty("password", pwd);
        try {//connect
            con = DriverManager.getConnection(url, props);
            if (con != null) {
                System.out.println("Successfully connected to the database "
                        + dbname + " as " + user);
            }
            assert con != null;
            con.setAutoCommit(false);
        } catch (SQLException e) {
            System.err.println("Database connection failed");
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    private static void closeDB() {
        if (con != null) {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                con.close();
                con = null;
            } catch (Exception e) {
                // Forget about it
            }
        }
    }

    private static void loodQuestion() {
        try (BufferedReader inline = new BufferedReader(new InputStreamReader(new FileInputStream("src/main/resources/data/questions.json")))) {
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = inline.readLine()) != null) {
                stringBuilder.append(line);
            }
            JSONObject jsonObject = new JSONObject(stringBuilder.toString());
            JSONArray items = jsonObject.getJSONArray("questions");
            int cnt = 0;
            stmt = con.prepareStatement("insert into questions (id, title, score, view_count, create_year) values (?,?,?,?,?)");
            for (Object obj :
                    items) {
                JSONObject item = (JSONObject) obj;
                stmt.setInt(1, item.getInt("id"));
                stmt.setString(2, item.getString("title"));
                stmt.setInt(3, item.getInt("score"));
                stmt.setInt(4, item.getInt("view_count"));
                stmt.setInt(5, (item.getInt("create_year") - 946684800) / 31536000 + 2000);
                stmt.addBatch();
                cnt++;
                if (cnt % BATCH_SIZE == 0) {
                    stmt.executeBatch();
                    stmt.clearBatch();
                }
            }
            if (cnt % BATCH_SIZE != 0) {
                stmt.executeBatch();
                stmt.clearBatch();
            }
            con.commit();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void loodQuestionTags() {
        try (BufferedReader inline = new BufferedReader(new InputStreamReader(new FileInputStream("src/main/resources/data/question_tags.json")))) {
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = inline.readLine()) != null) {
                stringBuilder.append(line);
            }
            JSONObject jsonObject = new JSONObject(stringBuilder.toString());
            JSONArray items = jsonObject.getJSONArray("tags");
            int cnt = 0;
            stmt = con.prepareStatement("insert into question_tags (question_id, tag) values (?,?)");
            for (Object obj :
                    items) {
                JSONObject item = (JSONObject) obj;
                stmt.setInt(1, item.getInt("question_id"));
                stmt.setString(2, item.getString("tag"));
                stmt.addBatch();
                cnt++;
                if (cnt % BATCH_SIZE == 0) {
                    stmt.executeBatch();
                    stmt.clearBatch();
                }
            }
            if (cnt % BATCH_SIZE != 0) {
                stmt.executeBatch();
                stmt.clearBatch();
            }
            con.commit();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void loodRepos() {
        try (BufferedReader inline = new BufferedReader(new InputStreamReader(new FileInputStream("src/main/resources/data/repos.json")))) {
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = inline.readLine()) != null) {
                stringBuilder.append(line);
            }
            JSONObject jsonObject = new JSONObject(stringBuilder.toString());
            JSONArray items = jsonObject.getJSONArray("repos");
            int cnt = 0;
            stmt = con.prepareStatement("insert into repos (id, title, watchers, forks, issues, created_time, updated_time) values(?,?,?,?,?,?,?)");
            for (int i = 0; i < items.length(); i++) {
                Object obj = items.get(i);
                JSONObject item = (JSONObject) obj;
                stmt.setInt(1, item.getInt("id"));
                stmt.setString(2, item.getString("title"));
                stmt.setInt(3, item.getInt("watchers"));
                stmt.setInt(4, item.getInt("forks"));
                stmt.setInt(5, item.getInt("issues"));
                stmt.setString(6, item.getString("created_time"));
                stmt.setString(7, item.getString("updated_time"));

                stmt.addBatch();
                cnt++;
                if (cnt % BATCH_SIZE == 0) {
                    stmt.executeBatch();
                    stmt.clearBatch();
                }
            }
            if (cnt % BATCH_SIZE != 0) {
                stmt.executeBatch();
                stmt.clearBatch();
            }
            con.commit();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void loodReposTags() {
        try (BufferedReader inline = new BufferedReader(new InputStreamReader(new FileInputStream("src/main/resources/data/repos_tags.json")))) {
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = inline.readLine()) != null) {
                stringBuilder.append(line);
            }
            JSONObject jsonObject = new JSONObject(stringBuilder.toString());
            JSONArray items = jsonObject.getJSONArray("tags");
            int cnt = 0;
            stmt = con.prepareStatement("insert into repos_tags (repo_id, tag) values (?,?)");
            for (Object obj :
                    items) {
                JSONObject item = (JSONObject) obj;
                stmt.setInt(1, item.getInt("repo_id"));
                stmt.setString(2, item.getString("tag"));
                stmt.addBatch();
                cnt++;
                if (cnt % BATCH_SIZE == 0) {
                    stmt.executeBatch();
                    stmt.clearBatch();
                }
            }
            if (cnt % BATCH_SIZE != 0) {
                stmt.executeBatch();
                stmt.clearBatch();
            }
            con.commit();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void loadForks() {
        try (BufferedReader inline = new BufferedReader(new InputStreamReader(new FileInputStream("src/main/resources/data/record_forks.json")))) {
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = inline.readLine()) != null) {
                stringBuilder.append(line);
            }
            JSONObject jsonObject = new JSONObject(stringBuilder.toString());
            JSONArray items = jsonObject.getJSONArray("forks");
            int cnt = 0;
            stmt = con.prepareStatement("insert into record_forks (id, title, time) values (?,?,?)");
            for (int i = 0; i < items.length(); i++) {
                Object obj = items.get(i);
                JSONObject item = (JSONObject) obj;
                stmt.setInt(1, item.getInt("id"));
                stmt.setString(2, item.getString("title"));
                stmt.setString(3, item.getString("time"));
                stmt.addBatch();
                cnt++;
                if (cnt % BATCH_SIZE == 0) {
                    stmt.executeBatch();
                    stmt.clearBatch();
                }
            }
            if (cnt % BATCH_SIZE != 0) {
                stmt.executeBatch();
                stmt.clearBatch();
            }
            con.commit();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    public static void main(String[] args) throws SQLException {
        Properties defprop = new Properties();
        defprop.put("host", "localhost");
        defprop.put("user", "test");
        defprop.put("password", "123456");
        defprop.put("database", "java2");
        Properties prop = new Properties(defprop);
        openDB(prop.getProperty("host"), prop.getProperty("database"),
                prop.getProperty("user"), prop.getProperty("password"));
        Statement stmt0;
        if (con != null) {
            stmt0 = con.createStatement();
//            stmt0.execute(
//                    "truncate table " +
//                            "questions,question_tags,repos,repos_tags,record_forks cascade;");
            stmt0.execute(
                    "truncate table " +
                            "repos, repos_tags cascade;");
            con.commit();
            stmt0.close();
        }
//        loodQuestion();
//        loodQuestionTags();
        loodRepos();
        loodReposTags();
        loadForks();
        closeDB();
        System.out.println("LOAD DONE!");
    }
}
