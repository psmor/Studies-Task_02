import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MainCollectors {
    Map<String, Long> getCountWord(List<String> str){
         return str.stream()
                .collect(
                        Collectors.groupingBy(String::toString, Collectors.counting())
                );
    }

}
