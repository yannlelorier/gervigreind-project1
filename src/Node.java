
public class Node {
    public Node parent;
    public int depth;
    public int evaluation;
    public Moves m;

    /**
     * create the root node of the search tree
     *
     * @param val the evaluation of this node
     */
    public Node(int val, Moves m) {
        this.parent = null;
        this.depth = 0;
        this.m = m;
        this.evaluation = val;
    }

    /**
     * create a new node
     *
     * @param parent the parent of the node
     * @param val    the evaluation of this node
     */
    public Node(Node parent, int val, Moves m) {
        this.parent = parent;
        this.depth = parent.depth + 1;
        this.evaluation = val;
        this.m = m;
    }


    public String toString() {
        return "Node{depth: " + depth + ", value: " + evaluation + "}";
    }

}