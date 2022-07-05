import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextProcessor {

    private String path;

    public TextProcessor(String path) {
        this.path = path;
    }

    public TreeMap<String, Integer> getCommonWords(int n, String[] stopwords) {
        TreeMap<String, Integer> treeMap = new TreeMap<>();
        Set<String> set = new HashSet<>(Arrays.asList(stopwords));
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            while ((line = br.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }
                String regex = "\\w+(['-]+\\w+)*";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(line);
                while (matcher.find()) {
                    String letter = matcher.group().toLowerCase();
                    if (!set.contains(letter)) {
                        Integer time = treeMap.get(letter);
                        if (time == null) {
                            treeMap.put(letter, 1);
                        } else {
                            treeMap.put(letter, time + 1);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Map.Entry<String, Integer>> list = new ArrayList<>(treeMap.entrySet());
        list.sort((o1, o2) -> {
            if (o1.getValue().equals(o2.getValue())) {
                return o1.getKey().compareTo(o2.getKey());
            } else {
                return o2.getValue() - o1.getValue();
            }
        });
        treeMap.clear();
        int size = Math.min(list.size(), n);
        for (int i = 0; i < size; i++) {
            Map.Entry<String, Integer> entry = list.get(i);
            treeMap.put(entry.getKey(), entry.getValue());
        }
        return treeMap;
    }

    public ArrayList<Position> highlightWord(Position pos) {
        ArrayList<Position> list = new ArrayList<>();
        String line;
        int row = 0;
        String target = "";
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            while ((line = br.readLine()) != null) {
                row++;
                if (line.isBlank()) {
                    continue;
                }
                if (row == pos.getLine()) {
                    int col = pos.getCol() - 1;
                    if (!(Character.isLetterOrDigit(line.charAt(col)) || line.charAt(col) == '\'' || line.charAt(col) == '-')) {
                        return list;
                    }
                    int left = col, right = col;
                    while (true) {
                        if (left == -1) {
                            break;
                        }
                        char ch = line.charAt(left);
                        if (!Character.isLetterOrDigit(ch)) {
                            if (ch == '\'' || ch == '-') {
                                if (!(left - 1 >= 0 && Character.isLetterOrDigit(line.charAt(left - 1)))) {
                                    break;
                                }
                            } else {
                                break;
                            }
                        }
                        left--;
                    }
                    while (true) {
                        if (right + 1 == line.length()) {
                            break;
                        }
                        char ch = line.charAt(right);
                        if (!Character.isLetterOrDigit(ch)) {
                            if (ch == '\'' || ch == '-') {
                                if (!(right + 1 < line.length() && Character.isLetterOrDigit(line.charAt(right + 1)))) {
                                    break;
                                }
                            } else {
                                break;
                            }
                        }
                        right++;
                    }
                    target = line.substring(left + 1, right).toLowerCase();
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if ("".equals(target)) {
            return list;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            row = 0;
            while ((line = br.readLine()) != null) {
                row++;
                if (line.isBlank()) {
                    continue;
                }
                for (int i = 0; i <= line.length() - target.length(); i++) {
                    if (!Character.isLetterOrDigit(line.charAt(i))) {
                        continue;
                    }
                    String substring = i + target.length() < line.length() ?
                            line.substring(i, i + target.length()).toLowerCase() :
                            line.substring(i).toLowerCase();
                    if (target.equals(substring)) {
                        if (!((i > 0 && (Character.isLetterOrDigit(line.charAt(i - 1)) || line.charAt(i - 1) == '\'' || line.charAt(i - 1) == '-'))
                                || (i + target.length() < line.length() && (Character.isLetterOrDigit(line.charAt(i + target.length()))
                                || line.charAt(i + target.length()) == '\'' || line.charAt(i + target.length()) == '-')))) {
                            list.add(new Position(row, i + 1));
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Position> fixLowercaseFirstLetter() {
        List<Position> list = new ArrayList<>();
        String line;
        int row = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            boolean flag = true;
            Read:
            while ((line = br.readLine()) != null) {
                row++;
                if (line.isBlank()) {
                    continue;
                }
                String[] split = line.split("[.?!]");
                int col = 0;
                Loop:
                for (int i = 0; i < split.length; i++) {
                    if (!flag && i == 0) {
                        continue;
                    }
                    String s = split[i];
                    for (int j = 0; j < s.length(); j++) {
                        char ch = s.charAt(j);
                        if (Character.isDigit(ch)) {
                            col += s.length() + 1;
                            continue Loop;
                        }
                        if (Character.isLetter(ch)) {
                            if (Character.isLowerCase(ch)) {
                                list.add(new Position(row, col + j + 1));
                            }
                            col += s.length() + 1;
                            continue Loop;
                        }
                    }
                    col += s.length() + 1;
                }
                line = line.trim();
                int index = Math.max(Math.max(line.lastIndexOf('.'), line.lastIndexOf('?')), line.lastIndexOf('!'));
                for (int i = index + 1; i < line.length(); i++) {
                    if (Character.isLetterOrDigit(line.charAt(i))) {
                        flag = false;
                        continue Read;
                    }
                }
                flag = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public String encrypt() {
        StringBuilder sb = new StringBuilder();
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            while ((line = br.readLine()) != null) {
                StringBuilder word = new StringBuilder();
                for (int i = 0; i < line.length(); i++) {
                    char ch = line.charAt(i);
                    if (Character.isLetterOrDigit(ch) || ch == '-' || ch == '\'') {
                        word.append(ch);
                        if (i == line.length() - 1) {
                            int length = word.length();
                            word.reverse();
                            char first = word.charAt(0);
                            if (first == 'a' || first == 'e' || first == 's'
                                    || first == 'A' || first == 'E' || first == 'S') {
                                int val = first;
                                word.insert(1, val);
                            }
                            word.append(length);
                            sb.append(word);
                            word = new StringBuilder();
                            sb.append(word);
                        }
                    } else {
                        if (!word.isEmpty()) {
                            int length = word.length();
                            word.reverse();
                            char first = word.charAt(0);
                            if (first == 'a' || first == 'e' || first == 's'
                                    || first == 'A' || first == 'E' || first == 'S') {
                                int val = first;
                                word.insert(1, val);
                            }
                            word.append(length);
                            sb.append(word);
                            word = new StringBuilder();
                        }
                        sb.append(ch);
                    }
                }
                sb.append("\n");
            }
            sb.deleteCharAt(sb.length() - 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public HashSet<List<String>> ngramSim(int ngram, String pathForAnotherFile) {
        HashSet<List<String>> set1 = new HashSet<>();
        HashSet<List<String>> set2 = new HashSet<>();
        List<String> list1 = new ArrayList<>();
        List<String> list2 = new ArrayList<>();
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            while ((line = br.readLine()) != null) {
                String regex = "\\w+['-]\\w+|\\w+";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(line);
                while (matcher.find()) {
                    String word = matcher.group().toLowerCase();
                    list1.add(word);
                    if (list1.size() == ngram) {
                        set1.add(new ArrayList<>(list1));
                        list1.remove(0);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedReader br = new BufferedReader(new FileReader(pathForAnotherFile))) {
            while ((line = br.readLine()) != null) {
                String regex = "\\w+(['-]+\\w+)*";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(line);
                while (matcher.find()) {
                    String word = matcher.group().toLowerCase();
                    list2.add(word);
                    if (list2.size() == ngram) {
                        set2.add(new ArrayList<>(list2));
                        list2.remove(0);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        set1.retainAll(set2);
        return set1;
    }
}
