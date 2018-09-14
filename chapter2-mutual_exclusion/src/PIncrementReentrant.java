import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class PIncrementReentrant implements Runnable {
    public static volatile int s;
    private static ReentrantLock lock = new ReentrantLock();
    public int count;
    public int current;
    public int next;

    public PIncrementReentrant(int N) {
        count = N;
    }

    public static int parallelIncrement3(int c, int numThreads) {
        int m = 1200000 / numThreads;
        int r = 1200000 % numThreads;
        s = c;

        List<Thread> threadList = new ArrayList<Thread>(numThreads);
        for (int i = 0; i < numThreads; i++) {
            PIncrementReentrant p = new PIncrementReentrant(m);
            threadList.add(new Thread(p));
        }
        for (Thread t : threadList) {
            t.start();
        }
        for (Thread t : threadList) {
            try {
                t.join();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return s;
    }

    @Override
    public void run() {
        int i = 0;
        while (i < count) {
            lock.lock();
            try {
                s++;
            } finally {
                lock.unlock();
            }
            i++;
        }
    }

    public static void main(String[] args) {
        System.out.println(parallelIncrement3(20, 8));
    }
}
