package redBlackTrees;;

public class BSTNode<E extends Comparable<E>> {
	
	protected BSTNode<E> parent = null;
	protected BSTNode<E> left = null;
	protected BSTNode<E> right = null;
	private char color;
	private E data;
	
	public BSTNode(E data) {
		this.data = data;
	}

	// Used if implementing RedBlackTree
	public BSTNode(E data, char color) {
		this.data = data;
		this.color = color;
	}

	public char getColor() {
		return this.color;
	}

	public void setColor(char color) {
		this.color = color;
	}

	public E getData() {
		return this.data;
	}
	
	public void setData(E data) {
		this.data = data;
	}

	public String toString() {
		return this.data.toString();
	}

}