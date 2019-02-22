Iterator（迭代器）作用于集合上，Java为各种集合提供了公共的操作接口，这样使得对集合的遍历操作与底层实现隔离，达到解耦效果。

所有的集合都实现了Collection接口，而Collection继承自Iterable，在Iterable接口中定义了iterator()方法。通过该方法拿到一个迭代器，从而进行迭代操作。

Iterator的定义如下：
```java
public interface Iterator<E> {
    /**
    * 如果仍有元素可以迭代，则返回true
    */
    boolean hasNext();

    /**
    * 返回下一个元素 
    */
    E next();
    
    /**
    * 删除当前迭代的元素
    */
    default void remove() {
        throw new UnsupportedOperationException("remove");
    }

    /**
    * 对集合的剩余元素执行给定的操作。
    * 这个是JDK8新增的方法。
    */
    default void forEachRemaining(Consumer<? super E> action) {
        Objects.requireNonNull(action);
        while (hasNext())
            action.accept(next());
    }
}
```

下面我们实现Iteratable，并自定义Iterator来实现迭代操作。
```java
/*
* Iterable和Iterator使用示例。
* @author Donny
* @email luckystar88@aliyun.com
* @date 2019/02/21 18:05
*/
public class IterableAndIteratorTest {

    public static void main(String[] args) {
        MyList<Teacher> teachers = new MyList<>(10);
        for (int i=0;i<10;i++) {
            teachers.add(new Teacher("1000" + i,"Teacher " + (i+1)));
        }

        for (Teacher teacher : teachers) {
            System.out.println(teacher);
        }

        System.out.println("=============");
        Iterator<Teacher> iterator = teachers.iterator();
        int index = 0;
        while (iterator.hasNext()) {
            index++;
            iterator.next();

            if (index == 3) {
                iterator.remove();
                break;
            }
        }

        System.out.println("after remove element 4");
        teachers.forEach(System.out::println);
        teachers.forEach(teacher -> {
            if (teacher.sid.equalsIgnoreCase("10005"))
                teachers.remove(0);
        });

    }

    private static class MyList<E> implements Iterable<E> {
        private Object[] elementsData; // 存储元素的数组
        private int size = 0; // 记录当前集合元素个数

        MyList(int size) {
            elementsData = new Object[size];
        }

        void add(Teacher e) {
            elementsData[size++] = e;
        }

        void remove(int index) {
            // todo check range
            int numMoved = size - index - 1; // 元素移动的个数
            if (numMoved > 0)
                // 将index后的元素复制到index位置开始。比如1,2,3,4,5在移除3时（index=2，对于4,5向前移动一位）
                System.arraycopy(elementsData, index+1, elementsData, index,
                        numMoved);
            // 将元素的个数减一，同时将最后一个元素置空（因为已经remove了）
            elementsData[--size] = null; // clear to let GC do its work
        }

        @Override
        public Iterator iterator() {
            return new Itr();
        }

        private class Itr implements Iterator<E> {
            int cursor = 0; // 当前元素的下标
            @Override
            public boolean hasNext() {
                return cursor != size;
            }

            @Override
            public E next() {
                return (E) elementsData[cursor++]; // 返回下一个元素，同时游标向下移动一位
            }

            @Override
            public void remove() {
                MyList.this.remove(cursor);
                cursor -= 1;
            }
        }
    }

    private static class Teacher {
        private String sid;
        private String name;

        Teacher(String sid, String name) {
            this.sid = sid;
            this.name = name;
        }

        @Override
        public String toString() {
            return "Teacher{" +
                    "sid='" + sid + '\'' +
                    ", name='" + name + '\'' +
                    '}';
        }
    }


}
```