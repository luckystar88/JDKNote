ListIterator接口继承自Iterator接口，在List接口中定义了listIterator()返回ListIterator，需要子类实现。

ListIterator中定义的方法如下：
![](/imgs/ListIterator.png)

其中，hasNext()，next()，remove()是Iterator中定义的方法，其它都是新增的方法。在List的实现类中都需要实现自己的ListIterator。

Iterator只有向后遍历的功能，ListIterator提供了向前、向后2个方向遍历的功能，同时可以在遍历时修改元素。

通过集合对象的iterator()拿到的就是Iterator，通过listIterator()或listIterator(index)拿到的就是ListIterator。相对于Iterator，ListIterator提供了更丰富的功能。  
listIterator():从0开始遍历；  
listIterator(index):从index位置开始遍历。

下面以ArrayList中的ListIterator说明。
```java
/**
 * An optimized version of AbstractList.ListItr
 */
private class ListItr extends Itr implements ListIterator<E> {
    ListItr(int index) {
        super();
        cursor = index; // cursor是ITr中定义的属性，记录当前元素位置。Itr是ArrayList中实现的自定义的Iterator。
    }

    public boolean hasPrevious() { // 是否有前一个元素
        return cursor != 0; // 当前游标不为0即说明还有前一个元素
    }

    public int nextIndex() { // 返回下一个index
        return cursor;
    }

    public int previousIndex() { // 前一个index
        return cursor - 1;
    }

    @SuppressWarnings("unchecked")
    public E previous() { // 返回前一个元素
        checkForComodification();
        int i = cursor - 1; // 得到前一个元素的索引
        if (i < 0) // 如果前一个元素的索引<0，说明当前已经是第一个元素，没有前一个元素了
            throw new NoSuchElementException();
        Object[] elementData = ArrayList.this.elementData; // 得到集合元素数组
        if (i >= elementData.length) // 正常情况单个线程执行previous操作i不会超过集合元素长度 如果超过说明是多个线程在操作
            throw new ConcurrentModificationException();
        cursor = i; // 记录当前游标
        return (E) elementData[lastRet = i]; // 返回前一个元素，并记录最后一次返回的位置
    }

    public void set(E e) { // 设置当前位置的元素为新的元素
        if (lastRet < 0) // 最后一次返回的位置<0，多线程环境异常操作
            throw new IllegalStateException();
        checkForComodification();

        try {
            ArrayList.this.set(lastRet, e); // 调用ArrayList的set方法，设置最后一次返回的位置的元素
        } catch (IndexOutOfBoundsException ex) {
            throw new ConcurrentModificationException();
        }
    }

    public void add(E e) { // 添加一个元素
        checkForComodification();

        try {
            int i = cursor;
            ArrayList.this.add(i, e); // 调用ArrayList的ad(i,e)方法，在当前游标的位置添加元素
            cursor = i + 1; // 添加元素后，游标向后移动一位
            lastRet = -1;
            expectedModCount = modCount;
        } catch (IndexOutOfBoundsException ex) {
            throw new ConcurrentModificationException();
        }
    }
}
```

示例：
```java
List<String> list = new ArrayList<>(list);
list.add("hello");
list.add("jack");

ListIterator<String> iterator = list.listIterator();
while (iterator.hasNext()) {
    String value = iterator.next();

    if (value.equals("hello")) {
        iterator.set("new jack");
    }
    if (value.equals("jack")) {
        iterator.add("donny"); // 注意：这里是在当前游标的位置添加元素，之后是遍历不到的。因为在add后，游标已经加1，而添加的元素在加1之前的位置。即添加的元素在cursor，执行后cursor+1了。
    }

}

System.out.println(list); // [new jack, jack, donny]
```