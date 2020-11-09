package redBlackTrees;

public class BinaryTreeTester {
    
    public static void main(String[] args) {

        // BinarySearchTree<Integer> tree = new BinarySearchTree<>(25, 20, 36, 10, 22, 30, 40, 5, 12, 28, 38, 48, 1, 8, 15,
        //         45, 50);

        // tree.delete(25);
        // tree.delete(22);
        // tree.delete(40);
        // tree.delete(20);
        // tree.delete(1);
        // tree.delete(5);

        // System.out.println("\n" + tree + "\n");

        // tree.insert(15);
       
        // System.out.println("pre-order: " + tree.preOrder());

        // System.out.println("in-order: " + tree.inOrder());

        // System.out.println("post-order: " + tree.postOrder());

        // System.out.println("breadth-first: " + tree.breadthFirst() + "\n");

        RedBlackTree<Integer> redBlackTree = new RedBlackTree<>(10,12,15);

        System.out.println(redBlackTree + "\n");
        
        // redBlackTree.delete(redBlackTree.root);
        // redBlackTree.delete(8);
        
        // System.out.println(redBlackTree);
        


        
   
 
    }

}