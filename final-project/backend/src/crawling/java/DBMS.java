import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;

public class DBMS {
    private static final int BATCH_SIZE = 500;
    private static Connection con = null;
    private static PreparedStatement stmt1 = null;
    private static PreparedStatement stmt2 = null;

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
                if (stmt1 != null) {
                    stmt1.close();
                }
                con.close();
                con = null;
            } catch (Exception e) {
                // Forget about it
            }
        }
    }

    private static void loadquestions() throws SQLException, IOException, InterruptedException {
        Statement statement = con.createStatement();
        statement.execute(
                "truncate table " +
                        "questions, question_tags cascade;");
        stmt1 = con.prepareStatement("insert into questions(id, title, score, view_count, create_year) values (?,?,?,?,?);");
        stmt2 = con.prepareStatement("insert into question_tags(question_id, tag) values (?,?);");
        /**
         * 1262304000 1293840000
         * 1293840000 1325376000
         * 1325376000 1356998400
         * 1356998400 1388534400
         * 1388534400 1420070400
         * 1420070400 1451606400
         * 1451606400 1483228800
         * 1483228800 1514764800
         * 1514764800 1546300800
         * 1546300800 1577836800
         * 1577836800 1609459200
         * 1609459200 1640995200
         */
        List<Integer> year = new ArrayList<>();
        year.add(1262304000);
        year.add(1325376000);
        year.add(1356998400);
        year.add(1388534400);
        year.add(1420070400);
        year.add(1451606400);
        year.add(1483228800);
        year.add(1514764800);
        year.add(1546300800);
        year.add(1577836800);
        year.add(1609459200);
        year.add(1640995200);
        HashSet<question> ques = new HashSet<>();
        int cnt = 0;
        try {
            for (int y = 1; y <= 11; y++) {
                int begin = year.get(y - 1), end = year.get(y);
                for (int i = 1; i < 21; i++) {
                    String s = "https://api.stackexchange.com/2.3/search?page=" + i + "&pagesize=100&fromdate=" + begin + "&todate=" + end + "&order=desc&sort=votes&tagged=java&site=stackoverflow";
                    ArrayList<question> questions = StackOverflow.getquestions(s);
                    for (question q :
                            questions) {
                        ques.add(q);
                    }
                    System.out.println(s);
                    Thread.sleep(5000);
                }
            }
            for (question qu :
                    ques) {
                try {
                    cnt++;
                    stmt1.setInt(1, qu.id);
                    stmt1.setString(2, qu.title);
                    stmt1.setInt(3, qu.score);
                    stmt1.setInt(4, qu.view_count);
                    stmt1.setInt(5, qu.createdyear);
                    stmt1.addBatch();
                    List<String> tags = qu.tags;
                    for (String tag :
                            tags) {
                        stmt2.setInt(1, qu.id);
                        stmt2.setString(2, tag);
                        stmt2.addBatch();
                    }
                    if (cnt % 500 == 0) {
                        stmt1.executeBatch();
                        stmt1.clearBatch();
                        stmt2.executeBatch();
                        stmt2.clearBatch();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (cnt % 500 != 0) {
                stmt1.executeBatch();
                stmt1.clearBatch();
                stmt2.executeBatch();
                stmt2.clearBatch();
            }
            con.commit();
        } catch (Exception ee) {
            for (question qu :
                    ques) {
                try {
                    cnt++;
                    stmt1.setInt(1, qu.id);
                    stmt1.setString(2, qu.title);
                    stmt1.setInt(3, qu.score);
                    stmt1.setInt(4, qu.view_count);
                    stmt1.setInt(5, qu.createdyear);
                    stmt1.addBatch();
                    List<String> tags = qu.tags;
                    for (String tag :
                            tags) {
                        stmt2.setInt(1, qu.id);
                        stmt2.setString(2, tag);
                        stmt2.addBatch();
                    }
                    if (cnt % 500 == 0) {
                        stmt1.executeBatch();
                        stmt1.clearBatch();
                        stmt2.executeBatch();
                        stmt2.clearBatch();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (cnt % 500 != 0) {
                stmt1.executeBatch();
                stmt1.clearBatch();
                stmt2.executeBatch();
                stmt2.clearBatch();
            }
            con.commit();
        }
    }

    private static void loadrepos() throws SQLException, IOException, InterruptedException {
        Statement statement = con.createStatement();

        statement.execute(
                "truncate table " +
                        "repos, repos_tags cascade;");
        stmt1 = con.prepareStatement("insert into repos (id, title, watchers, forks, issues, created_time, updated_time)  values (?,?,?,?,?,?,?);");
        stmt2 = con.prepareStatement("insert into repos_tags (repo_id, tag) values (?,?);");
        HashSet<repositories> repo_set = new HashSet<>();
        for (int year = 2010; year <= 2022; year++) {
            for (int page = 1; page <= 10; page++) {
                try {
                    String url = "https://api.github.com/search/repositories?page=" + page + "&q=java+created%3A" + year + "+stars%3A%3E10+forks%3A%3E10+language%3AJava&per_page=100";
                    List<repositories> repos = Github_rest.getrepos(url, year);
                    repos.forEach((re) -> {
                        repo_set.add(re);
                    });
                    Thread.sleep(3000);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    Thread.sleep(10000);
                    continue;
                }
            }

        }
        int cnt = 0;
        for (repositories re : repo_set
        ) {
            cnt++;
            stmt1.setInt(1, re.id);
            stmt1.setString(2, re.title);
            stmt1.setInt(3, re.watchers);
            stmt1.setInt(4, re.forks);
            stmt1.setInt(5, re.issues);
            stmt1.setString(6, re.created_time);
            stmt1.setString(7, re.uppdate_time);
            stmt1.addBatch();

            for (String tag : re.tags) {
                stmt2.setInt(1, re.id);
                stmt2.setString(2, tag);
                stmt2.addBatch();
            }
            if (cnt % 500 == 0) {
                stmt1.executeBatch();
                stmt1.clearBatch();
                stmt2.executeBatch();
                stmt2.clearBatch();
            }
        }
        if (cnt % 500 != 0) {
            stmt1.executeBatch();
            stmt1.clearBatch();
            stmt2.executeBatch();
            stmt2.clearBatch();
        }
        con.commit();
    }

    private static void loadforks() throws SQLException, IOException, InterruptedException {
        Statement statement = con.createStatement();

        statement.execute(
                "truncate table " +
                        "record_forks_temp cascade;");
        stmt1 = con.prepareStatement("insert into record_forks_temp (id, title, time) values (?,?,?);");
        HashSet<Fork> fork_set = new HashSet<>();
        for (int page = 1; page <= 116; page++) {
            try {
                String url = "https://api.github.com/repos/jeecgboot/jeecg-boot/forks?page=" + page + "&per_page=100";
                List<Fork> forks = ForkRecord.getfork(url);
                forks.forEach((re) -> {
                    re.title = "jeecg-boot";
                    fork_set.add(re);
                });
                Thread.sleep(1500);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                Thread.sleep(5000);
                continue;
            }
        }

        int cnt = 0;
        for (Fork re : fork_set
        ) {
            cnt++;
            stmt1.setInt(1, re.id);
            stmt1.setString(2, re.title);
            stmt1.setString(3, re.time);
            stmt1.addBatch();
            if (cnt % 500 == 0) {
                stmt1.executeBatch();
                stmt1.clearBatch();
            }
        }
        if (cnt % 500 != 0) {
            stmt1.executeBatch();
            stmt1.clearBatch();
        }
        con.commit();
    }


    public static void main(String[] args) throws SQLException, IOException, InterruptedException {
        openDB("localhost", "java2", "leo", "123456");
        //loadquestions();
        loadrepos();
        //loadforks();
        closeDB();
    }

}
