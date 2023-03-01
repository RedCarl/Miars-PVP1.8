import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String[] slot = {"0","1","2","3"};
        String element = "2";
        List<String> list = new ArrayList<>();
        for (String s:slot) {
            if (s.equals(element)){
                list.add(0,s);
            }else {
                list.add(s);
            }
        }

        System.out.println(list);
    }
}
