import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FacultyInfoQueryTest {

    @Test
    public void testReadFile() throws IOException {
        FacultyInfoQuery.readFile();
        Assertions.assertEquals(1034, FacultyInfoQuery.facultyList.size());
        BufferedReader br = new BufferedReader(new FileReader(FacultyInfoQuery.FILE_NAME));
        String line = "";
        List<Faculty> ans = new ArrayList<>();
        while ((line = br.readLine()) != null) {
            String[] split = line.split(",");
            String name = split[0].trim();
            String p = split[1].trim();
            String dep = split[2].trim();
            ans.add(new Faculty(name, p, dep));
        }
        Assertions.assertEquals(FacultyInfoQuery.facultyList, ans);
    }

}
