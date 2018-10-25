
public class MergeBrackets {
    int braceDelta;
    int minBraceDelta;

    public MergeBrackets(int braceDelta, int minBraceDelta) {
        this.braceDelta = braceDelta;
        this.minBraceDelta = minBraceDelta;
    }

    public int getBraceDelta() {
        return braceDelta;
    }

    public int getMinBraceDelta() {
        return minBraceDelta;
    }


    static MergeBrackets BraceDeltaAlgorithm(int chunkStartPos, int chunkEndPos, char[] input) {
        int braceDelta = 0;
        int minBraceDelta = braceDelta;
        for (int i = chunkStartPos; i < chunkEndPos; i++) {
            char c = input[i];
            if (c == '{' || c == '(' )  {
                braceDelta++;
            } else if (c == '}' || c == ')') {
                braceDelta--;
                if (braceDelta < minBraceDelta) {
                    minBraceDelta = braceDelta;
                }
            }
        }

        return new MergeBrackets(braceDelta,minBraceDelta);

        //return new ParallelBalancedBracket.ChunkResult(braceDelta, minBraceDelta);
    }

}
