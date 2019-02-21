package concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/*
* 
* @author Donny
* @email luckystar88@aliyun.com
* @date 2019/02/21 12:04
*/
public class ExecutorsThreadFactory {

    public static void main(String[] args) {
        ExecutorService executors = Executors.newFixedThreadPool(10,new MyThreadFactory());
        for (int i=0;i<10;i++) {
            executors.submit(() -> System.out.println(Thread.currentThread().getName() + " is running.."));
        }
        executors.shutdown();
    }

    private static class MyThreadFactory implements ThreadFactory {
        private final AtomicInteger num = new AtomicInteger(0);
        private final String namePrefix = "myThread-";

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            String name = namePrefix + num.incrementAndGet();
            t.setName(name);

            return t;
        }
    }
}
