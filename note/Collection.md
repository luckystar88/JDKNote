Collection是List和Set的父接口，提供了常规集合操作方法，比如add,get,contains,remove,size,isEmpty等方法。
同时，为了方便操作，提供了AbstractCollection，实现了一些方法。比如remove,contains,clear等。


Java集合UML图如下：
![](/imgs/java_collection_uml.png)

Collection的定义如下：
```java
public interface Collection<E> extends Iterable<E> {
    
}
```