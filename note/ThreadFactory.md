# 线程工厂（ThreadFactory）
顾名思义，这就是一个用来创建线程的对象。我们通常创建线程会使用`Thread t = new Thread`或`Thread t = new Thread(runnable);`，在创建线程后还可以设置线程的名字、优先级、是否是守护线程等等。

它是一个接口，提供一个方法`Thread newThread(Runnable r);`，你可以实现ThreadFactory来创建自定义的ThreadFactory。

ThreadFactory用的比较多的就是在Executors和Fork/Join框架中了。在使用Executors时，我们可以传入一个ThreadFactory，Executor将会使用我们传入的ThreadFactory来创建线程。
比如：
```java
ExecutorService executors = Executors.newFixedThreadPool(10,new MyThreadFactory());
```

一个比较经典的例子就是Executors中的ThreadFactory实现了。
```java
static class DefaultThreadFactory implements ThreadFactory {
        private static final AtomicInteger poolNumber = new AtomicInteger(1);
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        DefaultThreadFactory() {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() :
                                  Thread.currentThread().getThreadGroup();
            namePrefix = "pool-" +
                          poolNumber.getAndIncrement() +
                         "-thread-";
        }

        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r,
                                  namePrefix + threadNumber.getAndIncrement(),
                                  0);
            if (t.isDaemon())
                t.setDaemon(false);
            if (t.getPriority() != Thread.NORM_PRIORITY)
                t.setPriority(Thread.NORM_PRIORITY);
            return t;
        }
    }
```