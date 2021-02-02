/*
* Union Find or Disjoint set data structure implementation. This uses path compression to acheive the amortize constant time complexity
* @author Sanjiv
*/
public class UnionFind {
    // Tracks the size of each components
    private int[] sz;
    // Tracks the parent of each element such that parent[i] returns the parent node of i.
    // If parent[i] = i, then i is the root node 
    private int[] parent;
    // Number of components
    private int size = 0;

    public UnionFind(int size) {
        if (size < 0) {
            throw new IllegalArgumentException("Size < 0 is not allowed");
        }
        this.size = size;
        sz = new int[size];
        parent = new int[size];
        
        for (int i = 0; i < size; i++) {
            // Initially each element is the parent of itself.
            parent[i] = i;
            // Setting the size of each components to 0
            sz[i] = 0;
        }
    }

    /*
    * Returns the root of element p. Uses path compression
    */
    public int find(int p) {
        while (p != parent[p]) {
            // path compression by halving
            parent[p] = parent[parent[p]];
            p = parent[p];
        }
        return p;
    }

    /*
    * Returns the number of components.
    */
    public int size() {
        return size;
    }

    /*
    * Returns true if the root of p is equal to root of q; otherwise false.
    */
    public boolean connected(int p, int q) {
        return find(p) == find(q);
    }

    /*
    * Return nothing
    * Merges the component/set containing the element p with that of q.
    */
    public void union(int p, int q) {
        int rootP = find(p);
        int rootQ = find(q);

        if (rootP == rootQ) {
            return;
        }

        // Merges the smaller component/set to the larger set 
        if (sz[rootP] > sz[rootQ]) {
            parent[rootQ] = rootP;
        } else if (sz[rootP] < sz[rootQ]) {
            parent[rootP] = rootQ;
        } else {
            parent[rootQ] = rootP;
            sz[rootP]++;
        }
        size--;
    }
}
