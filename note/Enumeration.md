Enumeration接口中定义了一些方法，通过这些方法可以枚举（一次获得一个）对象集合中的元素。
比如打印Vector中的所有元素：
```java
String[] strings = {"apple","orange","banana"};
Vector<String> vector = new Vector<>(Arrays.asList(strings));
for (Enumeration<String> e=vector.elements();e.hasMoreElements();) {
    System.out.println(e.nextElement());
}
```

Iterator拥有Enumeration的功能，并提供了额外的删除操作，新的实现应该使用Iterator优于Enumeration。

Enumeration的定义如下：
```java
public interface Enumeration<E> {
    /**
    * 是否包含更多的元素 
    */
    boolean hasMoreElements();
    /**
    * 如果此枚举对象至少还有一个可提供的元素，则返回此枚举的下一个元素。
    */
    E nextElement();
}
```