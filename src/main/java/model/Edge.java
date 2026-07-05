package model;

public class Edge {
    public int u;      // id первой вершины
    public int v;      // id второй вершины
    public int weight; // вес ребра

    public Edge(int u, int v, int weight) {
        this.u = u;
        this.v = v;
        this.weight = weight;
    }
}