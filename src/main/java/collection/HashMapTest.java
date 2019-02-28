package collection;

/*
* 
* @author Donny
* @email luckystar88@aliyun.com
* @date 2019/02/27 18:00
*/
public class HashMapTest {
    public static void main(String[] args) {
        int h = 0b0101;
        int len = 8; // len为2^n时，h % len == h & (len-1)
        System.out.println(h % (len-1));
        System.out.println(h & (len-1));
    }
}
