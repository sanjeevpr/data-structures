import java.util.LinkedList;
import java.util.Queue;

public class BinarySearchTree<T extends Comparable<T>> {
    // The root node of the tree
    Node root;
    // Number of nodes in the tree
    int size;

    // Internal class Node to represent a node
    private class Node {
        // The left and the right Nodes a node.
        Node left, right;
        // The data in the node.
        T data;

        public Node(Node left, Node right, T data) {
            this.left = left;
            this.right = right;
            this.data = data;
        }
    }

    /*
    * Returns true if the tree is empty, otherwise, false
    */
    public boolean isEmpty() {
        return size() == 0;
    }

    /*
    * Returns the size of the tree
    */
    public int size() {
        return size;
    }

    /*
    * Returns true if the specified data is added to the tree, otherwise, false
    */
    public boolean add(T data) {
        // Don't add if the data is already present
        if(contains(data)) {
            return false;
        } else {
            root = add(root, data);
            size++;
            return true;
        }
    }

    /*
    * Helper method
    * Returns the added Node
    */
    private Node add(Node root, T data) {
        // Reached the left node
        if (root == null) {
            return new Node(null, null, data);
        } else {
            // Dig the left subtree
            if (data.compareTo(root.data) < 0) {
                root.left = add(root.left, data);
            } else {
                root.right = add(root.right, data);
            }
        }
        return root;

    }
    /*
    * Returns true if the specified data is removed from the tree, otherwise, false
    */
    public boolean remove(T data) {
        // Remove only if the data is present
        if (contains(data)) {
            root = remove(root, data);
            size--;
            return true;
        }
        return false;
    }

    /*
    * Helper method
    * Returns the removed Node
    */
    private Node remove(Node root, T data) {
        if (root == null) {
            return null;
        }

        int comparatorVal = data.compareTo(root.data);

         // Dig the left subtree if the specified data is smaller than the data in the current node.
        if(comparatorVal < 0) {
            root.left = remove(root.left, data)
        } else if(comparatorVal > 0) {
            root.right = remove(root.right, data);
        } else {
            // If the node to be removed is found.
            // If the left subtree is not present
            if(root.left == null) {
                return root.right;
            
            } else if(root.right == null) {
                 // If the right subtree is not present
                return root.left;
            } else {
                // If both the left and right subtrees are present.
                // Dig the left subtree to find the largest node.
                Node x = max(root.left);
                // Set/swap the data. 
                root.data = x.data;
                // Now remove the duplicate.
                root.left = remove(root.left, x.data);
            }
        }
        return root;
    }

    /*
    * Helper method
    * Returns the smallest node in the subtree rooted at the specified node
    */
    private Node min(Node node) {
        // Dig the left subtree as the smallest node is always present at the left subtree.
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    /*
    * Helper method
    * Returns the largest node in the subtree rooted at the specified node
    */
    private Node max(Node node) {
        // Dig the right subtree as the largest node is always present at the right subtree.
        while (node.right != null) {
            node = node.right;
        }
        return node;
    }

    /*
    * Returns true if the data is present in the tree, otherwise, false
    */
    public boolean contains(T data) {
        return contains(root, data);
    }

    /*
    * Helper method
    * Returns true if the data is present in the tree, otherwise, false
    */
    private boolean contains(Node root, T data) {
        if (root == null) {
            return false;
        }

        int comparatorVal = data.compareTo(root.data);

        // Dig the left subtree if the specified data is smaller than the data in the current node.
        if (comparatorVal < 0) {
            return contains(root.left, data);
        } else if (comparatorVal > 0) {
            return contains(root.right, data);
        } else {
            return true;
        }
    }

    /*
    * Returns the height of a node
    */
    public int height() {
        return height(root);
    }

    /*
    * Helper method
    * Returns the height of a node
    */
    private int height(Node root) {
        return Math.max(height(root.left), height(root.right)) + 1;
    }

    /*
    * Traverses the BST and prints the data.
    * Uses Depth First Search technique.
    */
    public void traversePreOrder(Node root) {
        if (root != null) {
            System.out.print(" " + root.data);
            traversePreOrder(root.left);
            traversePreOrder(root.right);
        }
    }

    /*
    * Traverses the BST and prints the data.
    * Uses Depth First Search technique.
    */
    public void traverseInOrder(Node root) {
        if (root != null) {
            traverseInOrder(root.left);
            System.out.print(" " + root.data);
            traverseInOrder(root.right);
        }
    }

    /*
    * Traverses the BST and prints the data.
    * Uses Depth First Search technique.
    */
    public void traversePostOrder(Node root) {
        if (root != null) {
            traversePostOrder(root.left);
            traversePostOrder(root.right);
            System.out.print(" " + root.data);
        }
    }

    /*
    * Traverses the BST and prints the data.
    * Uses Breadth First Search technique.
    */
    public void traverseLevelOrder() {
        if (root == null) {
            return;
        }

        Queue<Node> queue = new LinkedList<>();
        queue.add(root);

        while (!queue.isEmpty()) {
            Node node = queue.remove();

            System.out.print(" " + node.data);

            if (node.left != null) {
                queue.add(node.left);
            }
            if (node.right != null) {
                queue.add(node.right);
            }
        }
    }

}
