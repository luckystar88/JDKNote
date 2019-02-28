## HashMap介绍
HashMap是基于哈希表的Map接口的实现。HashMap 继承于AbstractMap，实现了Map、Cloneable、java.io.Serializable接口。  
它实现了所有可选的map操作，并允许null作为key和value。  
HashMap的实现不是同步的，即不是线程安全的。另外，HashMap不保证Map的顺序。

HashMap是数组存储数据，数组的每个元素是一个链表。数组的默认的长度是16，可以在初始化HashMap时指定，也可以不指定。  
如果不指定则HashMap默认容量是16.数组可以扩容，扩容后的大小是原大小的2倍，扩容的条件是元素个数达到当前容量*负载因子。

JDK1.8之前HashMap底层使用数组+链表实现，JDK1.8使用数组+链表+红黑树实现。在链表节点较少时使用链表，节点较多时（大于8）使用红黑树。  
红黑树的相关知识参考：<https://www.cnblogs.com/skywang12345/p/3245399.html>

HashMap的一个重点是如何根据key定位到元素在什么位置。这里的算法如下：
```java
// 计算key的hash值
static final int hash(Object key) {
	int h;
	// 如果key为null返回0；否则将hashCode的高16位参与预算
	return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
}

int n = tab.length; // 当前容量
int index = (n-1) & hash; // 计算key在数组的位置。这里(n-1) & hash == hash % n
```
具体的下面这篇文章写的不错，可以参考这篇文章：  
关于HashMap的源码解读参考：<https://blog.csdn.net/v123411739/article/details/78996181>


JDK1.7的HashMap源码分析参考：<http://www.importnew.com/28263.html>