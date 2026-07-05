package algorithm;

public class DSU {
    private int[] parent;

    public DSU(int n) {
        parent = new int[n];
        for (int i = 0; i < n; i++) parent[i] = i;
    }

    public int find(int x) {
        if (parent[x] != x)
            parent[x] = find(parent[x]);
        return parent[x];
    }

    public boolean union(int a, int b) {
        a = find(a);
        b = find(b);

        if (a == b) return false;

        parent[b] = a;
        return true;
    }
}