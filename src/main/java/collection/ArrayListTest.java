package collection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
* 
* @author Donny
* @email luckystar88@aliyun.com
* @date 2019/02/22 14:15
*/
public class ArrayListTest {

    public static void main(String[] args) {
        String[] data = {"apple","banana","orange"};
        List<String> list = Arrays.asList(data);
        list.forEach(System.out::println);

        list = new ArrayList<>();
        list.add("hello");
        list.add("jack");

        List<String> list2 = new ArrayList<>(list);
        System.out.println(list2);

        list.remove(0);
        System.out.println(list);
        System.out.println(list2);
        list.remove(-1);
    }

}
