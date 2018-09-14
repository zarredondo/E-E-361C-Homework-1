import java.util.ArrayList;
import java.util.List;

public class PIncrementSynchronized implements Runnable {
    public static volatile int s;
    private static Object lock = new Object();
    public int count;
    public int current;
    public int next;

    public PIncrementSynchronized(int N) {
        count = N;
    }

    public static int parallelIncrement2(int c, int numThreads) {
        int m = 1200000 / numThreads;
        int r = 1200000 % numThreads;
        s = c;

        List<Thread> threadList = new ArrayList<Thread>(numThreads);
        for (int i = 0; i < numThreads; i++) {
            PIncrementSynchronized p = new PIncrementSynchronized(m);
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
            synchronized (lock) {
                s++;
            }
            i++;
        }
    }

    public static void main(String[] args) {
        System.out.println(parallelIncrement2(20, 8));
    }
}
