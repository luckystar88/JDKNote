package collection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

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
        //list.remove(-1);

        ListIterator<String> iterator = list2.listIterator();
        while (iterator.hasNext()) {
            String value = iterator.next();

            if (value.equals("hello")) {
                iterator.set("new jack");
            }
            if (value.equals("jack")) {
                iterator.add("donny"); // 注意：这里是在当前游标的位置添加元素，之后是遍历不到的。在add后，游标已经加1，而添加的元素在加1之前的位置
            }

        }

        System.out.println(list2);
    }

}
