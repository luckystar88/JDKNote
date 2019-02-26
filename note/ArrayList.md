ArrayList继承AbstractList，实现List接口。ArrayList中的元素是有序的，可重复，内部使用数组来存储元素。

先看下ArrayList在集合中的位置：
![](/note/java_collection_uml.png)

## ArrayList的初始化
ArrayList提供了3种初始化方法，下面一一说明。
### 无参构造方法
```java
/**
 * Constructs an empty list with an initial capacity of ten.
 */
public ArrayList() {
    this.elementData = DEFAULTCAPACITY_EMPTY_ELEMENTDATA;
}

/**
 * Shared empty array instance used for default sized empty instances. We
 * distinguish this from EMPTY_ELEMENTDATA to know how much to inflate when
 * first element is added.
 */
private static final Object[] DEFAULTCAPACITY_EMPTY_ELEMENTDATA = {};
/**
 * The array buffer into which the elements of the ArrayList are stored.
 * The capacity of the ArrayList is the length of this array buffer. Any
 * empty ArrayList with elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA
 * will be expanded to DEFAULT_CAPACITY when the first element is added.
 */
transient Object[] elementData; // non-private to simplify nested class access
/**
 * Shared empty array instance used for empty instances.
 */
private static final Object[] EMPTY_ELEMENTDATA = {};
```
其中，elementData是存储ArrayList元素的一个Object数组。
无参构造方法时，elementData为DEFAULTCAPACITY_EMPTY_ELEMENTDATA，其实就是一个空的数组。那么这里为何
要写为DEFAULTCAPACITY_EMPTY_ELEMENTDATA呢？其实看代码，会发现还有另外一个字段也是空数组，那就是EMPTY_ELEMENTDATA。
为何要定义2个呢？这其实是为了区分容量是0还是默认的10.在<<指定数组容量初始化>>初始化方法中可以看到。

### 指定数组容量初始化
```java
/**
 * Constructs an empty list with the specified initial capacity.
 *
 * @param  initialCapacity  the initial capacity of the list
 * @throws IllegalArgumentException if the specified initial capacity
 *         is negative
 */
public ArrayList(int initialCapacity) {
    if (initialCapacity > 0) {
        this.elementData = new Object[initialCapacity];
    } else if (initialCapacity == 0) {
        this.elementData = EMPTY_ELEMENTDATA;
    } else {
        throw new IllegalArgumentException("Illegal Capacity: "+
                                           initialCapacity);
    }
}
```
通过代码可以看到，在指定的容量参数initialCapacity大于0时，elementData直接初始化为一个长度为initialCapacity的Object数组。
在容量为0时，elementData=EMPTY_ELEMENTDATA;在容量小于0时，很明显的参数错误，抛出一个IllegalArgumentException.

### 通过已有集合初始化
```java
/**
 * Constructs a list containing the elements of the specified
 * collection, in the order they are returned by the collection's
 * iterator.
 *
 * @param c the collection whose elements are to be placed into this list
 * @throws NullPointerException if the specified collection is null
 */
public ArrayList(Collection<? extends E> c) {
    elementData = c.toArray();
    if ((size = elementData.length) != 0) {
        // c.toArray might (incorrectly) not return Object[] (see 6260652)
        if (elementData.getClass() != Object[].class)
            elementData = Arrays.copyOf(elementData, size, Object[].class);
    } else {
        // replace with empty array.
        this.elementData = EMPTY_ELEMENTDATA;
    }
}
```
toArray()是Collection中定义的一个方法，可以将结合元素转换为一个Object[]。在通过已有的集合初始化ArrayList时，
ArrayList的elementData被初始化为传入的集合的元素数组，然后更新size为当前元素的个数，继而判断集合是否有内容（size !=0）
如果size=0，那么直接将elementData初始化为一个空的数组，否则如果elementData类型不是Object数组类型，通过Arrays.copyOf()
将原集合元素转换为Object[]，然后elementData初始化为转换的Object[]。

## ArrayList如何自动扩容
扩容是在执行add操作时发生的。在add之前会先确保有足够的容量，在容量不够时会发生扩容。

扩容代码如下：
```java
private void ensureCapacityInternal(int minCapacity) {
    if (elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA) { // 说明构造ArrayList时未指定容量
        // minCapacity通过add方法可以看到，它的值为当前size+你要添加的元素的个数
        // 通过max方法取得较大的一个作为本次添加元素需要的最小的容量
        minCapacity = Math.max(DEFAULT_CAPACITY, minCapacity);
    }

    ensureExplicitCapacity(minCapacity);
}

private void ensureExplicitCapacity(int minCapacity) {
    modCount++;

    // overflow-conscious code
    if (minCapacity - elementData.length > 0) // minCapacity > elementData.length说明需要扩容
        grow(minCapacity);
}

/**
 * Increases the capacity to ensure that it can hold at least the
 * number of elements specified by the minimum capacity argument.
 *
 * @param minCapacity the desired minimum capacity
 */
private void grow(int minCapacity) {
    // overflow-conscious code
    int oldCapacity = elementData.length; // 原容量
    int newCapacity = oldCapacity + (oldCapacity >> 1); // 新容量是原容量的1.5倍
    if (newCapacity - minCapacity < 0) // 如果原容量1.5倍还是小于需要的最小容量，那么新的容量为需要的最小容量
        newCapacity = minCapacity;
    if (newCapacity - MAX_ARRAY_SIZE > 0) // 如果新的容量大于MAX_ARRAY_SIZE
        newCapacity = hugeCapacity(minCapacity);
    // minCapacity is usually close to size, so this is a win:
    elementData = Arrays.copyOf(elementData, newCapacity); // 复制原集合元素到新的扩容后的数组
}

private static int hugeCapacity(int minCapacity) {
    if (minCapacity < 0) // overflow
        throw new OutOfMemoryError();
    return (minCapacity > MAX_ARRAY_SIZE) ?
        Integer.MAX_VALUE :
        MAX_ARRAY_SIZE;
}
```

## ArrayList如何实现元素的添加
### 在List末尾添加元素
```java
/**
 * Appends the specified element to the end of this list.
 *
 * @param e element to be appended to this list
 * @return <tt>true</tt> (as specified by {@link Collection#add})
 */
public boolean add(E e) {
	// 第1步确保有足够的容量，因为这里是添加1个元素，所以minCapacity=size+1即可
    ensureCapacityInternal(size + 1);  // Increments modCount!!
    // 1.将size+1；2.将当前元素的下一个元素设置为要添加的元素。
    elementData[size++] = e;
    return true;
}
```

### 在指定位置后添加元素
```java
/**
 * Inserts the specified element at the specified position in this
 * list. Shifts the element currently at that position (if any) and
 * any subsequent elements to the right (adds one to their indices).
 *
 * @param index index at which the specified element is to be inserted
 * @param element element to be inserted
 * @throws IndexOutOfBoundsException {@inheritDoc}
 */
public void add(int index, E element) {
    rangeCheckForAdd(index); // 这一步判断了index需要在0到size之间。

	// 因为也是添加一个元素，所以minCapacity=size+1
    ensureCapacityInternal(size + 1);  // Increments modCount!!
    // 使用arraycopy将index之后的元素向后移动一位 比如原1,2,3,4,5 执行add(2,xx)操作=》1,2,3,xx,4,5。4和5相对于原来向后移动了一位。
    System.arraycopy(elementData, index, elementData, index + 1,
                     size - index);
	// 将index位置的元素设置为要添加的元素                     
    elementData[index] = element;
    size++;
}

/**
 * A version of rangeCheck used by add and addAll.
 */
private void rangeCheckForAdd(int index) {
    if (index > size || index < 0)
        throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
}
```

### 添加一个集合
```java
/**
 * Appends all of the elements in the specified collection to the end of
 * this list, in the order that they are returned by the
 * specified collection's Iterator.  The behavior of this operation is
 * undefined if the specified collection is modified while the operation
 * is in progress.  (This implies that the behavior of this call is
 * undefined if the specified collection is this list, and this
 * list is nonempty.)
 *
 * @param c collection containing elements to be added to this list
 * @return <tt>true</tt> if this list changed as a result of the call
 * @throws NullPointerException if the specified collection is null
 */
public boolean addAll(Collection<? extends E> c) {
    Object[] a = c.toArray(); // 通过Collection的toArray()方法得到元素数组
    int numNew = a.length; // 这是要添加的新的元素的个数
    // 扩容 确保至少有size+numNew的容量
    ensureCapacityInternal(size + numNew);  // Increments modCount
    // 执行copy操作，将集合c的元素从size位置复制到size+numNew的位置。即原1,2,3；addAll(4,5) =>1,2,3(size),4,5
    System.arraycopy(a, 0, elementData, size, numNew);
    size += numNew; // 更新size
    return numNew != 0;
}
```

### 在指定位置插入一个集合
```java
/**
 * Inserts all of the elements in the specified collection into this
 * list, starting at the specified position.  Shifts the element
 * currently at that position (if any) and any subsequent elements to
 * the right (increases their indices).  The new elements will appear
 * in the list in the order that they are returned by the
 * specified collection's iterator.
 *
 * @param index index at which to insert the first element from the
 *              specified collection
 * @param c collection containing elements to be added to this list
 * @return <tt>true</tt> if this list changed as a result of the call
 * @throws IndexOutOfBoundsException {@inheritDoc}
 * @throws NullPointerException if the specified collection is null
 */
public boolean addAll(int index, Collection<? extends E> c) {
    rangeCheckForAdd(index); // 检查index在0到size之间

    Object[] a = c.toArray(); // 将要添加的集合元素转换为Object[]
    int numNew = a.length; // 新添加的元素个数
    // 扩容 至少需要容量为szie+numNew
    ensureCapacityInternal(size + numNew);  // Increments modCount

    int numMoved = size - index; // 元素需要移动的位置 比如原1,2,3,4，执行addAll(2,[5,6])，即最终得到1,2,5,6,3,4。那么3,4需要移动size-index
    if (numMoved > 0)
    	// 将index之后的元素向后移动numMoved ps:这里其实是将index之后的元素复制到index+numNew之后
        System.arraycopy(elementData, index, elementData, index + numNew,
                         numMoved);
	// 将本次要添加的集合元素复制到index位置
    System.arraycopy(a, 0, elementData, index, numNew);
    size += numNew; // 更新size
    return numNew != 0;
}
```

## ArrayList如何实现元素的移除
```java
/**
 * Removes the element at the specified position in this list.
 * Shifts any subsequent elements to the left (subtracts one from their
 * indices).
 *
 * @param index the index of the element to be removed
 * @return the element that was removed from the list
 * @throws IndexOutOfBoundsException {@inheritDoc}
 */
public E remove(int index) {
    rangeCheck(index); // 这里只是判断了index必须<=size 并没有判断为负数的情况。

    modCount++;
    E oldValue = elementData(index); // 得到要移除的元素

    int numMoved = size - index - 1; // 移除元素之后的元素需要向前移动的位置 即原1,2,3,4,5 remove(2) => 1,2,4,5.对于4,5实际是向前移动了1位。
    if (numMoved > 0)
    	// 将index+1开始的元素copy到index的位置 注意：是复制 不是移动。1,2,3,4,5 假定要remove(2)，在执行下面的arraycopy后实际内容是：1,2,4,5,5
        System.arraycopy(elementData, index+1, elementData, index,
                         numMoved);
    // 执行上面的arraycopy后，数组最后一位元素其实是废弃的了 需要设置为null                     
    elementData[--size] = null; // clear to let GC do its work

    return oldValue;
}

/**
 * Checks if the given index is in range.  If not, throws an appropriate
 * runtime exception.  This method does *not* check if the index is
 * negative: It is always used immediately prior to an array access,
 * which throws an ArrayIndexOutOfBoundsException if index is negative.
 */
private void rangeCheck(int index) {
    if (index >= size)
        throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
}
```

## ArrayList小结
* ArrayList中的元素时有序的、可重复、可为null；
* ArrayList的所有操作都不是线程安全的，在多线程环境建议使用Vector或CopyOnWriteArrayList；
* ArrayList内部使用Object数组存储元素；
* ArrayList在初始化时可以指定初始容量，如果不指定则会使用默认容量，默认为10；
* 添加新元素时，会先进行容量检查。如果容量不够会进行扩容。扩容的容量是原容量的1.5倍。所以如果可以预估容量最好初始化时就指定，避免扩容；
* add(index,e)、remove(index)、addAll(Collection)都是通过System.arraycopy来实现的。