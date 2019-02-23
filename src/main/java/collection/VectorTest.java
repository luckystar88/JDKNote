package collection;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.Vector;

/*
* 
* @author Donny
* @email luckystar88@aliyun.com
* @date 2019/02/23 17:46
*/
public class VectorTest {

    public static void main(String[] args) {
        String[] strings = {"apple","orange","banana"};
        Vector<String> vector = new Vector<>(Arrays.asList(strings));
        for (Enumeration<String> e=vector.elements();e.hasMoreElements();) {
            System.out.println(e.nextElement());
        }
    }
}
