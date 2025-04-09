import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class main {

    static List<Employee> employees = new ArrayList<>(Arrays.asList(
            new Employee("Man1", 35, Post.MANAGER),
            new Employee("Dir", 44, Post.DIRECTOR),
            new Employee("Eng1_25", 25, Post.ENGINEER),
            new Employee("Eng2_25", 25, Post.ENGINEER),
            new Employee("Eng3_42", 42, Post.ENGINEER),
            new Employee("Man2", 55, Post.MANAGER),
            new Employee("Eng4_33", 33, Post.ENGINEER)
    ));


    public static void main(String[] args) {
        /////////////////////////////////////////////////////////////////////////////////////////
        // 3-е наибольшее число
        int[] ints1 = {5,2,10,9,4,3,10,1,13};                          // На вход int
        int max11 = Arrays.stream(ints1)
                .max()
                .getAsInt();
        System.out.println("Максимальное число (1-й способ): " + max11);

        Integer[] ints2 = new Integer[]{5,2,10,9,4,3,10,1,13};        // На вход Integer
        int max12 = Arrays.stream(ints2)
                .max(Integer::compare)
                .get();
        System.out.println("Максимальное число (2-й способ): " + max12);

        /////////////////////////////////////////////////////////////////////////////////////////
        // 3-е наибольшее уникальное число
        Integer max21 = Arrays.stream(ints1)                            //На вход int
                .distinct()
                .boxed()                                               // Упаковка потока в оболочку (в данном случае int в Integer)
                .sorted(Collections.reverseOrder())                    // Обратная сортировка. Требует передачи массива объектов
                .limit(3)
                .min(Comparator.comparing(Integer::valueOf))
                .get();
        System.out.println("3-е наибольшее уникальное число (1-й способ): "+max21);

        int max22 = Arrays.stream(ints2)                               // На вход Integer
                .distinct()
                // здесь .boxed() не нужен, поскольку изначально поток из Integer
                .sorted(Collections.reverseOrder())
                .limit(3)
                .min(Comparator.comparing(Integer::valueOf))
                .get();
        System.out.println("3-е наибольшее уникальное число (2-й способ): "+max22);

        /////////////////////////////////////////////////////////////////////////////////////////
        // Список имён 3-х самыз старших сотрудников, с должностью "Инженер", в порядке убывания возраста
        List<String> names = employees.stream()
                .filter(employee -> employee.getPost() == Post.ENGINEER)           // Оставляем только тех у кого должность ENGINEER
                //.sorted(Comparator.comparingInt( (o-> o.getAge())) )               // Сортировка по возрасту, в порядке возрастания
                .sorted( Comparator.comparingInt(Employee :: getAge).reversed() )  // Сортировка по возрасту, в порядке убывания возраста
                .map(employee->employee.getName())                                 // Вывод только Имён
                .toList();
        System.out.println("3 самых старших сотрудника: "+names);

        // Тестировал вывод в потоке
//        employees.stream()
//                .filter(employee -> employee.getPost() == Post.ENGINEER)           // Оставляем только тех у кого должность ENGINEER
//                //.sorted(Comparator.comparingInt( (o-> o.getAge())) )               // Сортировка по возрасту, в порядке возрастания
//                .sorted( Comparator.comparingInt(Employee :: getAge).reversed() )  // Сортировка по возрасту, в порядке убывания возраста
//                .map(employee->employee.getName())                                 // Вывод только Имён
//                .forEach(System.out::println);

        /////////////////////////////////////////////////////////////////////////////////////////
        // Средний возраст сотрудников, с должностью "Инженер"
        Double averAge = employees.stream()
                .filter(employee -> employee.getPost() == Post.ENGINEER)           // Оставляем только тех у кого должность ENGINEER
                .mapToInt( employee->employee.getAge() )                           // Оставляем только возраст
                .average()                                                         // Среднее значение
                .orElseThrow( ()-> new RuntimeException("Считать нечего") );       // На случай если поток будет пустым
        System.out.println("Средний возраст Инженеров = "+averAge);

        /////////////////////////////////////////////////////////////////////////////////////////
        // Самое длинное слово
        String[] strings = {"Длинное_слово", "Эх", "Упс"};
        String str1 = Arrays.stream(strings)
                .max(Comparator.comparingInt(String::length))
                .get();
        System.out.println("Самое длинное слово: "+str1);

        /////////////////////////////////////////////////////////////////////////////////////////
        // Построить хеш мапы
        String str2 = "шляпа солнце сакура море пляж шляпа";
        //Arrays.stream(str2.split(" ")).forEach(System.out::println); // Вывожу массив строк

        MainCollectors mainCollectors = new MainCollectors();
        Map<String, Long> map = mainCollectors.getCountWord( Arrays.stream(str2.split(" ")).toList() );
        System.out.println("Получилась мапа:");
        for (String word : map.keySet()){
            System.out.println("word: " + word + ", value: " + map.get(word));
        }

        /////////////////////////////////////////////////////////////////////////////////////////
        // Вывести слова в порядке увеличения длинны слова, если длинна одинаковая, тогда в алфовитном порядке.
        System.out.println("Строки в порядке увеличения длинны слова:");
        Arrays.stream(str2.split(" "))
                .sorted()                                                     // Сначала сортируем по алфавиту
                .sorted(Comparator.comparingInt(String :: length).reversed()) // Затем по длинне слова
                .forEach(System.out::println);

        /////////////////////////////////////////////////////////////////////////////////////////
        //Массив строк, в каждом набор из 5 слов, разделённых пробелом. Нужно найти самое длинное.
        System.out.print("Самое длинное слово: ");
        String[] arr = {"шляпа солнце сакура море бриллиант",
                        "погода навека длинный выгода последний"
        };
        String str = Arrays.stream(arr)
                       .map(x->x.split(" "))   // каждую строку представим в виде массива. Теперь имеем вложенны массив
                       .flatMap(Stream::of)          // Разворачиваем вложеннвй массив в общий поток
                       .max(Comparator.comparingInt(String::length)) // Самое длинное
                       .get();
                //.forEach(System.out::println);
        System.out.println(str);







    }
}
