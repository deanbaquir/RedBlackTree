package redBlackTrees;

public class RedBlackTree<E extends Comparable<E>> extends BinarySearchTree<E> {

    private final BSTNode<E> NIL = new BSTNode<>(null, 'B');

    public RedBlackTree() {
        super();
    }

    @SafeVarargs
    public RedBlackTree(E... args) {
        for (E element : args) {
			insert(element);
		}
    }

    @Override
    public void insert(E key) {
        BSTNode<E> child = new BSTNode<>(key, 'R');
        if (super.isEmpty()) {
            super.root = child;
            child.left = NIL;
            child.right = NIL;
            child.parent = NIL;
        }
        else {
            try {
                BSTNode<E> parent = insertionPoint(key);
                if (key.compareTo(parent.getData()) < 0) {
                    parent.left = child;
                    child.parent = parent;
                }
                else if (key.compareTo(parent.getData()) > 0) {
                    parent.right = child;
                    child.parent = parent;
                }
                // leaf node left and right always point to NIL
                child.left = NIL;
                child.right = NIL;
            }
            catch (DuplicateItemException e) {
                System.out.println("Duplicate Item: " + key + "\n");
            }
        }
        insertCleanup(child);
    }

    @Override
	protected BSTNode<E> insertionPoint(E key) {
		BSTNode<E> current = super.root;
		BSTNode<E> parent = NIL;

		while (current != NIL) {
			if (key.compareTo(current.getData()) == 0) {
				throw new DuplicateItemException("Duplicate Item");
			} 
			else if (key.compareTo(current.getData()) < 0) {
				parent = current;
				current = current.left;
			} 
			else if (key.compareTo(current.getData()) > 0) {
				parent = current;
				current = current.right;
			}
		}
		return parent;
	}

    private void insertCleanup(BSTNode<E> node) {
        if (super.root.getColor() == 'R') { // case 1: root red
            super.root.setColor('B');
            return;
        }
        else if (node.parent.getColor() == 'B') { // case 2: n parent black
            return;
        }
        else if (node.parent.getColor() == 'R' && super.uncle(node).getColor() == 'R') { // case 3: If n's parent & uncle red
            node.parent.setColor('B'); 
            super.uncle(node).setColor('B'); 
            super.grandparent(node).setColor('R'); 
            insertCleanup(super.grandparent(node)); // Recursive call on grandparent
        }
        if (node.parent.getColor() == 'R' && super.uncle(node).getColor() == 'B') { // case 4: parent red AND uncle black

            if (super.isRightChild(node) && super.isLeftChild(node.parent)) { // case 4a: left right zigzag 
                    leftRotate(node.parent);
                    node = node.left;
            } 
            else if (super.isLeftChild(node) && super.isRightChild(node.parent)) { // case 4b: right left zigzag
                    rightRotate(node.parent);
                    node = node.right;
            }
        }
        if (node.parent.getColor() == 'R' && super.uncle(node).getColor() == 'B') { // **case5 == case4: CASE 4 CONTINUES TO CASE 5

            if (super.isLeftChild(node) && super.isLeftChild(node.parent)) {  // n left child and parent left child (4a --> 5a)
                    node.parent.setColor('B');
                    super.grandparent(node).setColor('R');
                    rightRotate(super.grandparent(node));
            } 
            else if (super.isRightChild(node) && super.isRightChild(node.parent)) { // n right child and parent right child (4b --> 5b)
                    node.parent.setColor('B');
                    super.grandparent(node).setColor('R');
                    leftRotate(super.grandparent(node));
            }
        }
    }

    private void leftRotate(BSTNode<E> subTreeRootNode) {
        BSTNode<E> pivot = subTreeRootNode.right;
    
        // If subTreeRootNode is ACTUAL root of tree, pivot becomes root of tree
        if (subTreeRootNode == super.root) {
            super.root = pivot;
        }
        else if (isLeftChild(subTreeRootNode)) {
            subTreeRootNode.parent.left = pivot;
        }
        else if (isRightChild(subTreeRootNode)) {
            subTreeRootNode.parent.right = pivot;
        }
        pivot.parent = subTreeRootNode.parent;
        subTreeRootNode.right = pivot.left;
        subTreeRootNode.right.parent = subTreeRootNode;
        pivot.left = subTreeRootNode;
        subTreeRootNode.parent = pivot;
    }

    // Inverse of leftRotate
    private void rightRotate(BSTNode<E> subTreeRoot) {
        BSTNode<E> pivot = subTreeRoot.left;
    
        if (subTreeRoot == super.root) {
            super.root = pivot;
        }
        else if (isLeftChild(subTreeRoot)) {
            subTreeRoot.parent.left = pivot;
        }
        else if (isRightChild(subTreeRoot)) {
            subTreeRoot.parent.right = pivot;
        }
        pivot.parent = subTreeRoot.parent;
        subTreeRoot.left = pivot.right;
        subTreeRoot.left.parent = subTreeRoot;
        pivot.right = subTreeRoot;
        subTreeRoot.parent = pivot;
    }
    
    @Override
	protected void delete(BSTNode<E> node) {
		// checks if nodeToDelete() wasn't found
		if (node == null) {
			return;
		}

		if (isleafNode(node)) { // Case 1: 0 child
            if (node == super.root) {
                super.root = null;
                return;
            }
            if (isLeftChild(node)) {
				node.parent.left = NIL;
			} 
			else if (isRightChild(node)) {
				node.parent.right = NIL;
            }
            NIL.parent = node.parent;
            if (node.getColor() == 'B') { 
                NIL.setColor('D'); // D = double black
                fixDoubleBlack(NIL);
            }
		} 
        else if (numChildren(node) == 1) { // Case 2: One child
			// child = get left or right child of node
			BSTNode<E> child = node;
			if (node.left != NIL) {
				child = node.left;
			}
			else if (node.right != NIL) {
				child = node.right;
            }
            // if node to delete is actual root of tree
            if (node == super.root) {
                super.root = child;
                insertCleanup(root);
            }
			if (isLeftChild(node)) {
				node.parent.left = child;
				child.parent = node.parent;
			} 
			else if (isRightChild(node)) {
				node.parent.right = child;
				child.parent = node.parent;
            }
            if (child.getColor() == 'R' || node.getColor() == 'R') {
                child.setColor('B');
            }
            else if (child.getColor() == 'B' || node.getColor() == 'B') {
                child.setColor('D'); 
                fixDoubleBlack(child);
            }
		} 
		else if (numChildren(node) == 2) { // Case 3: Two child
			// Finding max in left subtree, Starting at left subtree
			BSTNode<E> maxLeftNode = node.left;
			while (maxLeftNode.right != NIL) {
				maxLeftNode = maxLeftNode.right;
			}
			node.setData(maxLeftNode.getData());
			delete(maxLeftNode);
		}
	}

	@Override
	protected BSTNode<E> nodeToDelete(E key) {
		// checks if node to delete wasn't found
		if (!find(key)) {
			System.out.println(key + " not found \n");
		}
		BSTNode<E> current = this.root;

		while (current != NIL) {
			if (key.compareTo(current.getData()) == 0) {
				return current;
			} 
			else if (key.compareTo(current.getData()) < 0) {
				current = current.left;
			} 
			else if (key.compareTo(current.getData()) > 0) {
				current = current.right;
			}
		}
		return null;
    }

    private void fixDoubleBlack(BSTNode<E> node) {
        if (node == super.root) { // Case 1: node is actual root of tree; reduce number of black nodes on every path by 1
            node.setColor('B');
        }
        // Case 2: sibling of node is red
        else if (super.sibling(node).getColor() == 'R') { 
           
            if (super.isRightChild(node)) { // Case 2a 
                super.sibling(node).setColor('B');
                node.parent.setColor('R');
                rightRotate(node.parent);
                fixDoubleBlack(node);
            }
            else if (super.isLeftChild(node)) { // Case 2b
                super.sibling(node).setColor('B');
                node.parent.setColor('R');
                leftRotate(node.parent);
                fixDoubleBlack(node);
            }
        }
        // Case 3: sibling of node has ATLEAST 1 red child (rc)
        else if (super.sibling(node).left.getColor() == 'R' || super.sibling(node).right.getColor() == 'R') { 

            // sibling is the left child of its parent
            if (super.isLeftChild(super.sibling(node))) {
                // rc is the left child of sibling
                if (super.sibling(node).left.getColor() == 'R') {
                    rightRotate(node.parent);
                    super.sibling(node).setColor(node.parent.getColor()); // change color of S to color of P
                    super.sibling(node).left.setColor('B'); // Change RC to black
                    node.parent.setColor('B');
                    node.setColor('B');
                }
                // rc is the right child of sibling
                else if (super.sibling(node).right.getColor() == 'R') {
                    leftRotate(super.sibling(node));
                    rightRotate(node.parent);
                    super.sibling(node).right.setColor(node.parent.getColor()); // change color of RC to parents color
                    super.sibling(node).setColor('B'); // change color of S to black
                    node.parent.setColor('B'); // change color of P to black
                    node.setColor('B');
                }
            }
            else if (super.isRightChild(super.sibling(node))) {
                // rc is the right child of S
                if (super.sibling(node).right.getColor() == 'R') {
                    leftRotate(node.parent);
                    super.sibling(node).setColor(node.parent.getColor());
                    super.sibling(node).right.setColor('B');
                    node.parent.setColor('B');
                    node.setColor('B');
                }
                // rc is the left child of S
                else if (super.sibling(node).left.getColor() == 'R') {
                    rightRotate(super.sibling(node));
                    leftRotate(node.parent);
                    super.sibling(node).left.setColor(node.parent.getColor());
                    super.sibling(node).setColor('B');
                    node.parent.setColor('B');
                    node.setColor('B');
                }
            }
         }
         // Case 4: sibling and both of its children are black
         else if (super.sibling(node).getColor() == 'B' && 
            super.sibling(node).left.getColor() == 'B' && super.sibling(node).right.getColor() == 'B') {

                // parent of sibling is red
                if (super.sibling(node).parent.getColor() == 'R') {
                    super.sibling(node).setColor('R');
                    node.parent.setColor('B');
                    node.setColor('B');
                }
                else if (super.sibling(node).parent.getColor() == 'B') {
                    super.sibling(node).setColor('R');
                    node.parent.setColor('D');
                    node.setColor('B');
                    fixDoubleBlack(node.parent);
                }
         } 
    }
    
    @Override
    public boolean find(E key) {
		if (isEmpty()) {
			throw new NullPointerException("Empty Tree");
		}
		BSTNode<E> current = this.root;
		while (current != NIL) {
			if (key == current.getData()) {
				return true;
			} 
			else if (key.compareTo(current.getData()) < 0) {
				current = current.left;
			} 
			else if (key.compareTo(current.getData()) > 0) {
				current = current.right;
			}
		}
		return false;
    }
    
    @Override
    protected boolean isleafNode(BSTNode<E> node) {
		return (node.left == NIL && node.right== NIL);
	}

    /**
	 * Original Author: Laurent Demailly
	 * Stackoverflow post: https://stackoverflow.com/questions/4965335/how-to-print-binary-tree-diagram
	 * Adapated algorithm into toString()
	 */
    @Override
    public String toString() {
		StringBuilder sb = new StringBuilder();
		if (this.root.right != NIL) {
			sb.append(this.printTree(this.root.right, true, ""));
		}

        sb.append(printNodeValue(this.root));

		if (this.root.left != NIL) {
			sb.append(this.printTree(this.root.left, false, ""));
		}
		return sb.toString();
	}

	private String printTree(BSTNode<E> node, boolean isRight, String indent) {
		StringBuilder sb = new StringBuilder();
		if (node.right != NIL) {
			sb.append(printTree(node.right, true, indent + (isRight ? "        " : " |      ")));
		}
		sb.append(indent);

		if (isRight) {
			sb.append(" /");
		} 
		else {
			sb.append(" \\");
		}
		sb.append("----- ");
		sb.append(printNodeValue(node));
		if (node.left != NIL) {
			sb.append(printTree(node.left, false, indent + (isRight ? " |      " : "        ")));
		}
		return sb.toString();
	}

	private String printNodeValue(BSTNode<E> node) {
		StringBuilder sb = new StringBuilder();
		if (node == NIL) {
			sb.append("<null>");
		} 
		else {
            sb.append(node.getData() + "(" + node.getColor() + ")");
		}
		sb.append("\n");
		return sb.toString();
	}

    
}