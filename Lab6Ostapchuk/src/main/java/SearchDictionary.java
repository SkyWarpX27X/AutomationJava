import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

public class SearchDictionary {
    private Node root;
    private int wordAmount;

    public static void main(String[] args) {
        System.out.println(System.getProperty("user.dir"));
    }

    public SearchDictionary(){
        root = new Node();
    }

    public void addWord(String word){
        Node current = root;
        for(char c : word.toCharArray()){
            if (current.next.containsKey(c)) current = current.next.get(c);
            else {
                Node node = new Node();
                current.next.put(c, node);
                current = node;
            }
        }
        if (!current.next.containsKey('\0')) {
            current.next.put('\0', null);
            wordAmount++;
        }
    }

    public void addWordsFromFile(String fileName){
        try {
            Scanner sc = new Scanner(new File(fileName));
            while (sc.hasNextLine()) {
                addWord(sc.nextLine());
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public String delWord(String word){
        char[] chars = word.toCharArray();
        Node current = root;
        Node forkBegin = root;
        char forkKey = word.charAt(0);
        for(int i = 0; i < chars.length; i++){
            if (!current.next.containsKey(chars[i])) return null;
            current = current.next.get(chars[i]);
            if (current.next.size() > 1) {
                forkBegin = current;
                if (i == chars.length - 1) forkKey = '\0';
                else forkKey = chars[i+1];
            }
        }
        if (!current.next.containsKey('\0')) return null;
        forkBegin.next.remove(forkKey);
        return word;
    }

    public boolean hasWord(String word){
        Node current = root;
        for(char c : word.toCharArray()){
            if (!current.next.containsKey(c)) return false;
            current = current.next.get(c);
        }
        return current.next.containsKey('\0');
    }

    public Iterable<String> query(String query){
        LinkedList<String> res = new LinkedList<>();
        if (query.charAt(query.length() - 1) != '*') {
            if (this.hasWord(query)) res.addFirst(query);
            return res;
        }
        String newQuery = query.replace("*", "");
        Node current = root;
        for (char c : newQuery.toCharArray()) {
            if (!current.next.containsKey(c)) return res;
            current = current.next.get(c);
        }
        getWords(res, newQuery, current);
        return res;
    }

    private void getWords(LinkedList<String> words, String query, Node node){
        for (char c : node.next.keySet()) {
            if (c == '\0') words.addFirst(query);
            else getWords(words, query + c, node.next.get(c));
        }
    }

    public int countWords(){
        return wordAmount;
    }

    private class Node{
        HashMap<Character, Node> next;
        char value;
        public Node(){
            next = new HashMap<>();
        }
    }
}