import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

public class Frequency implements Callable<Integer> {
    int value;
    int[] subarray;
    static List<Frequency> frequencies;

    public Frequency(int x, int[] A) {
        this.value = x;
        this.subarray = A;
    }
    public static ExecutorService threadPool = Executors.newCachedThreadPool();
    @Override
    public Integer call() throws Exception {
        int count = 0;
        try {
            for (int el : subarray) {
                if (el == value)
                    count++;
            }
            return count;
        } catch (Exception e) { System.err.println (e); return 1;}
    }

    public static int parallelFreq(int x, int[] A, int numThreads) {
    // your implementation goes here.
        ExecutorService es = Executors.newSingleThreadExecutor();

        int subarrayL = A.length / numThreads;
        int counter =0;
        frequencies = new ArrayList<Frequency>();
        int lower, upper;
        for (int i = 0; i < A.length - subarrayL + 1; i += subarrayL) {
            counter++;
            int remaining = A.length % subarrayL;
            if(counter == numThreads && (remaining != 0)) {
                frequencies.add(new Frequency(x, Arrays.copyOfRange(A, i, i + subarrayL + remaining)));
            } else{
                frequencies.add(new Frequency(x, Arrays.copyOfRange(A, i, i + subarrayL)));
            }
        }
        List<Future<Integer>> futureList = new ArrayList<Future<Integer>>();
        for (Frequency f : frequencies) {
            Future<Integer> future = es.submit(f);
            futureList.add(future);
        }
        int sum = 0;
        for (Future<Integer> future : futureList) {
            try {
                sum += future.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sum;
    }

    public static void main(String[] args) {
        int[] blah = {1, 2, 3, 4, 3, 4, 3, 3, 1, 2};
        System.out.println(Frequency.parallelFreq(3, blah, 3));
    }
}

