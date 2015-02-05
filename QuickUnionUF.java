
import java.util.ArrayList;
import java.io.File;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileReader;

public class QuickUnionUF {

    private int[] id;
    private int[] sz;

    public QuickUnionUF(int N) {
        id = new int[N];
        sz = new int[N];
        for(int i = 0; i < N; i++) {
            id[i] = i;  // each "object" has itself as its root
            sz[i] = 1;
        }
    }
    private int root(int i) {
        // chase parent pointers until root is reached
        // while navigating up the tree, make each node point directly to the root to flatten the thing out
        while(id[i] != i) {
            // System.out.println("root: " + i + " is not " + id[i] + " so following " + id[i] + " which points to " + id[id[i]]);
            i = id[i] = id[id[i]];   // first, updating the pointer in the current slot to be the same as whatever is in the slot it points to, then step over in to the next slop
        }
        return i;
    }

    public boolean connected(int p, int q) {
        return root(p) == root(q);   // check if they have the same roots
    }
    public void union(int p, int q) {
        int i = root(p);
        int j = root(q);
        if(i == j) return;
        if(sz[i] < sz[j]) { id[i] = j; sz[j] += sz[i]; }       // sz[] is new and keeps track of how many nodes are above a node; initialized to 1 I guess
        else              { id[j] = i; sz[i] += sz[j]; }
    }
    public void dump() {
        int i;
        StringBuffer s = new StringBuffer("");
        for(i = 0; i < 10; i++) {
            s = s.append(" " + i + "=" + id[i]);
        }
        System.out.println(s);
    }

    public static void main(String[] args) {

        QuickUnionUF qu = new QuickUnionUF(Integer.parseInt(args[1]));

        String fn = args[0];
        // ArrayList<Array> list = new ArrayList<A>();
        File file = new File(fn);
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(file));
            String text;
            while((text = reader.readLine()) != null) {
                System.out.println(text);
                // list.add(Integer.parseInt(text));
                String[] tokens = text.split("[ ]+");
                qu.union(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]));
                System.out.println("0 and 9 are connected: " + (qu.connected(0, 9) ? "yes" : "no"));
                qu.dump();
            }
        } catch(Exception e) {
           e.printStackTrace();
        } finally {
            try {
                if(reader != null) {
                    reader.close();
                }
            } catch(IOException e) {
            }
        }

        // System.out.println(list);
    }

}



