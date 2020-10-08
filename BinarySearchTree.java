package redBlackTrees;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class BinarySearchTree<E extends Comparable<E>> {

	protected BSTNode<E> root = null;

	// Default constructor
	public BinarySearchTree() {
	}

	@SafeVarargs // No vulnerabilities :)
	public BinarySearchTree(E... args) {
		for (E element : args) {
			insert(element);
		}
	}

	public boolean find(E key) {
		if (isEmpty()) {
			throw new NullPointerException("Empty Tree");
		}
		BSTNode<E> current = this.root;
		while (current != null) {
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

	public void insert(E key) {
		BSTNode<E> child = new BSTNode<>(key);
		if (isEmpty()) {
			this.root = child;
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
			} 
			catch (DuplicateItemException e) {
				System.out.println("Duplicate Item: " + key + "\n");
			}
		}
	}

	// Used in insert(), returns future parent node of item to add
	protected BSTNode<E> insertionPoint(E key) {
		BSTNode<E> current = this.root;
		BSTNode<E> parent = null;

		while (current != null) {
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

	public void delete(E key) {
		delete(nodeToDelete(key));
	}

	protected void delete(BSTNode<E> node) {
		// checks if nodeToDelete() wasn't found
		if (node == null) {
			return;
		}

		if (isleafNode(node)) { // Case 1: 0 child
			if (isLeftChild(node)) {
				node.parent.left = null;
			} 
			else if (isRightChild(node)) {
				node.parent.right = null;
			}
		} 
		else if (numChildren(node) == 1) { // Case 2: One child
			// child = get left or right child of node
			BSTNode<E> child = node;
			if (node.left != null) {
				child = node.left;
			}
			else if (node.right != null) {
				child = node.right;
			}
			if (isLeftChild(node)) {
				node.parent.left = child;
				child.parent = node.parent;
			} 
			else if (isRightChild(node)) {
				node.parent.right = child;
				child.parent = node.parent;
			}
		} 
		else if (numChildren(node) == 2) { // Case 3: Two child
			// Finding max in left subtree, Starting at left subtree
			BSTNode<E> maxLeftNode = node.left;
			while (maxLeftNode.right != null) {
				maxLeftNode = maxLeftNode.right;
			}
			node.setData(maxLeftNode.getData());
			delete(maxLeftNode);
		}
	}

	// Reimplementation of find(), returns actual node instead of boolean value
	protected BSTNode<E> nodeToDelete(E key) {
		// checks if node to delete wasn't found
		if (!find(key)) {
			System.out.println(key + " not found \n");
		}
		BSTNode<E> current = this.root;

		while (current != null) {
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

	public ArrayList<E> preOrder() {
		ArrayList<E> list = new ArrayList<>();
		preOrder(this.root, list);
		return list;
	}

	private void preOrder(BSTNode<E> node, ArrayList<E> list) {
		if (node == null) { // base case
			return;
		}
		// visit node
		list.add(node.getData());
		preOrder(node.left, list);
		preOrder(node.right, list);
	}

	public ArrayList<E> inOrder() {
		ArrayList<E> list = new ArrayList<>();
		inOrder(this.root, list);
		return list;
	}

	private void inOrder(BSTNode<E> node, ArrayList<E> list) {
		if (node == null) {
			return;
		}
		inOrder(node.left, list);
		list.add(node.getData());
		inOrder(node.right, list);
	}

	public ArrayList<E> postOrder() {
		ArrayList<E> list = new ArrayList<>();
		postOrder(this.root, list);
		return list;
	}

	private void postOrder(BSTNode<E> node, ArrayList<E> list) {
		if (node == null) {
			return;
		}
		postOrder(node.left, list);
		postOrder(node.right, list);
		list.add(node.getData());
	}

	public ArrayList<E> breadthFirst() {
		ArrayList<E> list = new ArrayList<>();
		breadthFirst(this.root, list);
		return list;
	}

	private void breadthFirst(BSTNode<E> rootNode, ArrayList<E> list) {
		Queue<BSTNode<E>> queue = new LinkedList<>();
		queue.add(rootNode);
		while (!queue.isEmpty()) {
			BSTNode<E> temp = queue.remove();
			list.add(temp.getData());
			
			// Conditional statement needed to not get NullPointerException
			if (temp.left != null) {
				queue.add(temp.left);
			}
			if (temp.right != null) {
				queue.add(temp.right);
			}
		}
	}

	protected BSTNode<E> grandparent(BSTNode<E> node) {
		BSTNode<E> grandparent = null;
		if (node.parent == null || node.parent.parent == null) {
			System.out.println("No grandparent");
			return grandparent;
		} 
		else {
			grandparent = node.parent.parent;
		}
		return grandparent;
	}

	protected BSTNode<E> uncle(BSTNode<E> node) {
		BSTNode<E> uncle = null;
		if (node.parent == null || sibling(node.parent) == null) {
			System.out.println("No uncle");
		return uncle;
		}
		else {
			uncle = sibling(node.parent);
		}
		return uncle;
	}

	// returns sibling node or null if no sibling
	protected BSTNode<E> sibling(BSTNode<E> node) {
		BSTNode<E> sibling = null;
		if (numChildren(node.parent) == 2) {
			if (isLeftChild(node)) {
				sibling = node.parent.right;
			} 
			else if (isRightChild(node)) {
				sibling = node.parent.left;
			}
		}
		return sibling;
	}

	protected boolean isleafNode(BSTNode<E> node) {
		return (node.left == null && node.right== null);
	}

	protected boolean isLeftChild(BSTNode<E> node) {
		if (node.parent == null) {
			return false;
		}
		return node.parent.left == node;
	}

	protected boolean isRightChild(BSTNode<E> node) {
		if (node.parent == null) {
			return false;
		}
		return node.parent.right == node;
	}

	protected int numChildren(BSTNode<E> node) {
		int count = 0;

		if (node.left != null) {
			count += 1;
		}
		if (node.right != null) {
			count += 1;
		}
		return count;
	}

	public boolean isEmpty() {
		return this.root == null;
	}

	/**
	 * Original Author: Laurent Demailly
	 * Stackoverflow post: https://stackoverflow.com/questions/4965335/how-to-print-binary-tree-diagram
	 * Adapated algorithm into toString()
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (this.root.right != null) {
			sb.append(this.printTree(this.root.right, true, ""));
		}

		sb.append(printNodeValue(this.root));

		if (this.root.left != null) {
			sb.append(this.printTree(this.root.left, false, ""));
		}

		return sb.toString();
	}

	private String printTree(BSTNode<E> node, boolean isRight, String indent) {
		StringBuilder sb = new StringBuilder();
		if (node.right != null) {
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
		if (node.left != null) {
			sb.append(printTree(node.left, false, indent + (isRight ? " |      " : "        ")));
		}
		return sb.toString();
	}

	private String printNodeValue(BSTNode<E> node) {
		StringBuilder sb = new StringBuilder();
		if (node == null) {
			sb.append("<null>");
		} 
		else {
			sb.append(node.getData());
		}
		sb.append("\n");
		return sb.toString();
	}

}
