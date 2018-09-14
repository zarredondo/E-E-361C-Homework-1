import java.util.ArrayList;
import java.util.List;

public class PIncrementTournament implements Runnable {
    public static volatile int s;
    public static Lock locks[];
    public int numThreads;
    public int id;
    public int count;
    public int[] processes;


    public static double log2(double d) {
        return Math.log(d)/Math.log(2.0);
    }

    public static int computeNumberOfLevels(int N) {
        return (int) Math.ceil(log2(N));
    }

    public PIncrementTournament(int count, int numThreads, int id) {
        this.numThreads = numThreads;
        this.count = count;
        this.id = id;
        processes = new int[numThreads];
    }

    public void requestCS(int i) {
        int current = i + numThreads - 1;
        int numLevels = computeNumberOfLevels(numThreads);
        for (int level = 1; level <= numLevels; level++) {
            processes[level] = current % 2;
            current = (int) Math.floor(current / 2);
            locks[current].requestCS(processes[level]);
        }
    }

    public void releaseCS(int i) {
        int current = 1;
        int levels = computeNumberOfLevels(numThreads);
        for (int level = levels; level > 0; level--) {
            locks[current].releaseCS(processes[level]);
            current = 2 * current + processes[level];
        }
    }

    public static int parallelIncrement4(int c, int numThreads) {
        int total = 80;
        int m = total / numThreads;
        int r = total % numThreads;
        s = c;

        locks = new Lock[numThreads];
        for (int i = 0; i < numThreads; i++) {
            locks[i] = new PetersonAlgorithm();
        }

        List<Thread> threadList = new ArrayList<Thread>(numThreads);
        for (int i = 0; i < numThreads; i++) {
            PIncrementTournament p = new PIncrementTournament(m, numThreads, i + 1);
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
            requestCS(id);
            s++;
            releaseCS(id);
            i++;
        }
    }

    public static void main(String[] args) {
        System.out.println(parallelIncrement4(20, 8));
    }
}
