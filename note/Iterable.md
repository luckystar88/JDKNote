Collection接口是众多集合对象的一个顶层接口，然后Collection接口继承自Iterable。实现了Iterable接口的类表明该类支持迭代，可以进行for-each操作。
这个接口只是表示可以迭代，具体的迭代逻辑在Iterator中。关于Iterator可参考[Iterator](/note/Iterator.md)。

注意：Iterable是java.lang包的，不是java.util包的。

```java
public interface Iterable<T> {
    Iterator<T> iterator();
    default void forEach(Consumer<? super T> action) {
        Objects.requireNonNull(action);
        for (T t : this) {
            action.accept(t);
        }
    }
    default Spliterator<T> spliterator() {
        return Spliterators.spliteratorUnknownSize(iterator(), 0);
    }
}
```
其中，forEach和spliterator是jdk8新增的方法。forEach遍历每个元素，对每个元素执行action给定的操作。

比如：
```java
public class IterableForEachTest {
    String[] data = {"apple","banana","orange"};
    List<String> list = Arrays.asList(data);
    // 输出每个元素
    list.forEach(System.out::println);
}
```

spliterator()方法返回一个Spliterator，这是一个可分割的迭代器。是JDK8中新增的并行遍历元素而设计的一个迭代器。关于Spliterator可以参考[Spliterator](/note/Spliterator.md).