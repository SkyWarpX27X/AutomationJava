import java.util.List;

public class Main {

    public static void main(String[] args) {



        List<String> lines = List.of(
                "Lorem",
                "ipsum dolor sit amet ",
                "consectetur",
                "adipiscing elit",
                "sed",
                "do eiusmod tempor incididunt ut ",
                "labore et dolore",
                "magna aliqua"
        );
        System.out.println(filterStringsCount(lines));

    }

    private static long filterStringsCount(List<String> list) {
        return list.stream().filter(string -> string.contains(" ")).filter(string -> string.length() - 10 <= 9).count();
    }
}
