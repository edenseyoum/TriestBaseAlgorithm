public class Edge {
    public final int u;
    public final int v;

    public Edge(String e) {
        String[] ns = e.split(" ");
        u = Integer.parseInt(ns[0]);
        v= Integer.parseInt(ns[1]);
    }

    public String toString() {
        return u + " " + v;
    }
}
