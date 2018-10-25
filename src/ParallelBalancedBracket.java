import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;


public class ParallelBalancedBracket {

    char[] list;
    int threshold;
    static int intialArraylenght;

    public ParallelBalancedBracket(char[] list, int threshold) {
        this.list = list;
        this.threshold = threshold;
    }


    public static void startMainTask(char[] list, int threshold) {
        intialArraylenght = list.length;
        RecursiveAction result = new ChunkResult(list, threshold,0,0);
        ForkJoinPool pool = new ForkJoinPool();
        pool.invoke(result);
    }


    private static class ChunkResult extends RecursiveAction {
        private char[] list;
        private int threshold;
        private int braceDelta;
        private int minBraceDelta;

        public ChunkResult(char[] list, int threshold, int braceDelta, int minBraceDelta) {
            this.list = list;
            this.threshold = threshold;
            this.braceDelta = braceDelta;
            this.minBraceDelta = minBraceDelta;
        }

        @Override
        protected void compute() {
            if (list.length <= threshold) {
                MergeBrackets a = MergeBrackets.BraceDeltaAlgorithm(0, list.length, list);
                this.braceDelta = a.getBraceDelta();
                this.minBraceDelta = a.getMinBraceDelta();
                return;
            }

            int firstHalfLength = list.length / 2;
            char[] firstHalf = new char[firstHalfLength];
            System.arraycopy(list, 0, firstHalf, 0, firstHalfLength);


            int secondHalfLength = list.length - list.length / 2;
            char[] secondHalf = new char[secondHalfLength];
            System.arraycopy(list, list.length / 2,
                    secondHalf, 0, secondHalfLength);

            ChunkResult r1 = new ChunkResult(firstHalf , threshold, braceDelta, minBraceDelta );
            ChunkResult r2 = new ChunkResult(secondHalf , threshold, braceDelta, minBraceDelta );
            invokeAll(r1, r2);

            braceDelta =  r1.braceDelta + r2.braceDelta;
            minBraceDelta =   Math.min(r1.minBraceDelta, r1.braceDelta + r2.minBraceDelta);

            if(firstHalf.length + secondHalf.length == ParallelBalancedBracket.intialArraylenght) {
                if (braceDelta == 0 && minBraceDelta >= 0) {
                    System.out.println("Brackets Are Balanced");
                }else{
                   System.out.println("UnBalanced Brackets");
                }

            }
        }
    }
}