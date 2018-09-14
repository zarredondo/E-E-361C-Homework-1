//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.atomic.AtomicInteger;
//
//public class PIncrement implements Runnable {
//    public static volatile AtomicInteger s;
//    public int count;
//    public int current;
//    public int next;
//
//    public PIncrement(int N) {
//        count = N;
//    }
//
//    public static int parallelIncrement(int c, int numThreads) {
//        int m = 1200000 / numThreads;
//        int r = 1200000 % numThreads;
//        s = new AtomicInteger();
//        s.set(c);
//        List<Thread> threadList = new ArrayList<Thread>(numThreads);
//        for (int i = 0; i < numThreads; i++) {
//            PIncrement p = new PIncrement(m);
//            threadList.add(new Thread(p));
//        }
//        for (Thread t : threadList) {
//            t.start();
//        }
//        for (Thread t : threadList) {
//            try {
//                t.join();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return s.get();
//    }
//
//    @Override
//    public void run() {
//        int i = 0;
//        while (i < count) {
//            current = s.get();
//            next = current + 1;
//            if (s.compareAndSet(current, next)) {
//                i++;
//            }
//        }
//    }
//
//    public static void main(String[] args) {
//        System.out.println(parallelIncrement(20, 8));
//    }
//}
