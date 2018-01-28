package edu.iastate.cs228.hw4;

import java.util.Arrays;

/**
 * @author Omair Ijaz
 *
 *         An entry tree class
 */
public class EntryTree<K, V> {
	/**
	 * dummy root node made public for grading
	 */
	protected Node root;

	/**
	 * prefixlen is the largest index such that the keys in the subarray keyarr
	 * from index 0 to index prefixlen - 1 are, respectively, with the nodes on
	 * the path from the root to a node. prefixlen is computed by a private
	 * method shared by both search() and prefix() to avoid duplication of code.
	 */
	protected int prefixlen;

	protected class Node implements EntryNode<K, V> {
		protected Node child; // link to the first child node
		protected Node parent; // link to the parent node
		protected Node prev; // link to the previous sibling
		protected Node next; // link to the next sibling
		protected K key; // the key for this node
		protected V value; // the value at this node

		public Node(K aKey, V aValue) {
			key = aKey;
			value = aValue;
			child = null;
			parent = null;
			prev = null;
			next = null;
		}

		@Override
		public EntryNode<K, V> parent() {
			return parent;
		}

		@Override
		public EntryNode<K, V> child() {
			return child;
		}

		@Override
		public EntryNode<K, V> next() {
			return next;
		}

		@Override
		public EntryNode<K, V> prev() {
			return prev;
		}

		@Override
		public K key() {
			return key;
		}

		@Override
		public V value() {
			return value;
		}
	}

	public EntryTree() {
		root = new Node(null, null);
	}

	/**
	 * Returns the value of the entry with a specified key sequence, or null if
	 * this tree contains no entry with the key sequence.
	 * 
	 * @param keyarr
	 * 
	 * @return the value of the entry with a specified key sequence, or null if
	 *         this tree contains no entry with the key sequence.
	 */
	public V search(K[] keyarr) {
		if(keyarr == null || keyarr.length == 0)
			return null;
		hasNullCheck(keyarr);// helper method to check for null values
		if(searchNodeAtEnd(keyarr) == null)//our helper method returns null if there
			return null;//is no word with specified keyarr, so we must return null here
		return searchNodeAtEnd(keyarr).value;
	}

	/**
	 * The method returns an array of type K[] with the longest prefix of the
	 * key sequence specified in keyarr such that the keys in the prefix label
	 * the nodes on the path from the root to a node. The length of the returned
	 * array is the length of the longest prefix.
	 * 
	 * @param keyarr
	 * 	Key array whose prefix in the EntryTree will be searched
	 * @return
	 * 	Key array that corresponds with the longest prefix of parameter.
	 */
	public K[] prefix(K[] keyarr) {//done recursively
		if (keyarr == null || keyarr.length == 0)// base case
			return null;
		hasNullCheck(keyarr);
		if (searchNodeAtEnd(keyarr) == null)//if my helper method can't find the last node
			return prefix(Arrays.copyOf(keyarr, keyarr.length - 1));//we call prefix on a smaller array
		if (searchNodeAtEnd(keyarr) != null)
			return keyarr;
		return null;
	}

	/**
	 * The method locates the node P corresponding to the longest prefix of the
	 * key sequence specified in keyarr such that the keys in the prefix label
	 * the nodes on the path from the root to the node. If the length of the
	 * prefix is equal to the length of keyarr, then the method places aValue at
	 * the node P and returns true. Otherwise, the method creates a new path of
	 * nodes (starting at a node S) labeled by the corresponding suffix for the
	 * prefix, connects the prefix path and suffix path together by making the
	 * node S a child of the node P, and returns true.
	 * 
	 * @param keyarr
	 * 	Key array that will be added to the tree
	 * @param aValue
	 * 	The value the stored at the end of keyarr
	 * @return
	 * 	True, if the add was successful or false if unsuccessful 
	 */
	public boolean add(K[] keyarr, V aValue) {
		if (keyarr == null || keyarr.length == 0 || aValue == null)
			return false;
		hasNullCheck(keyarr);
		Node P;// we set up P, our node to add on to
		if (searchNodeAtEnd(prefix(keyarr)) == null)
			P = root;
		else {
			P = searchNodeAtEnd(prefix(keyarr));
		}
		if (prefixlen == keyarr.length) {// all we have to do is change value
			P.value = aValue;
			return true;
		}
		boolean isModified = false;
		K[] suffix = Arrays.copyOfRange(keyarr, prefixlen, keyarr.length);
		for (int i = 0; i < suffix.length; i++) { // loop suffix length
			Node toAdd = new Node(suffix[i], null);
			if (i == suffix.length - 1)// if we are at the end of suffix
				toAdd.value = aValue;// then we add in the value
			linkParentChild(P, toAdd);// helper method
			P = toAdd;// move the node to attach onto
			isModified = true;
		}
		return isModified;
	}

	/**
	 * Removes the entry for a key sequence from this tree and returns its value
	 * if it is present. Otherwise, it makes no change to the tree and returns
	 * null.
	 * 
	 * @param keyarr
	 * @return the value of the node at the last index of keyarr
	 */
	public V remove(K[] keyarr) {
		if (keyarr == null || keyarr.length == 0)
			return null;
		hasNullCheck(keyarr);
		V vToRet = searchNodeAtEnd(keyarr).value;
		searchNodeAtEnd(keyarr).value = null;

		for (int i = keyarr.length - 1; i >= 0; i--) {// removes nodes at end first
			Node toRemove = searchNodeAtEnd(Arrays.copyOf(keyarr, i + 1));//gets the node at the end
			if (toRemove == root || toRemove.value != null || toRemove.child != null)
				return vToRet;// we cant remove this node
			else {
				unlink(toRemove);
			}
		}
		return vToRet;
	}

	/**
	 * The method prints the tree on the console in the output format shown in
	 * an example output file.
	 */
	public void showTree() {
		showTreeRec(root);
	}

	// Helper Methods

	/** This method finds the number of parent links to get to root.
	 *  The height of the root is 0.
	 *  The height of a node is the amount of "parent" links required to reach the root.
	 * 
	 * @param node 
	 * 	The node to find the level of
	 * @return
	 * 	the level of the node
	 */
	private int findNodeLevel(Node node) {
		Node cursor = node;
		int height = 0;
		while (cursor.parent != null) { // while our cursor is not the root, the
										// only node with a parent
			height++;
			cursor = cursor.parent;
		}
		return height;
	}

	/** The method prints a specified
	 *  node as described by the spec. This method is called in the
	 *  recursive showTree helper method.
	 * @param node
	 * 	The node that will be printed according to the format
	 * 
	 * 
	 */
	private void formatPrint(Node node) {
		int numOfTabs = findNodeLevel(node);
		for (int i = 0; i < numOfTabs; i++) { // print correct amt of tabs
			System.out.print("  ");
		}
		System.out.println(node.key() + "->" + node.value());
	}

	/** Prints the node and all of its descendants in our EntryTree. 
	 *  This is the recursive method that prints a tree given a node.
	 *  (This method is called on the root node in our 
	 *   public showTree method)	
	 *  
	 * 
	 * @param node
	 * node whose descendants will be printed. 
	 * 
	 */
	private void showTreeRec(Node node) {
		if (node == null)// base case
			return;
		formatPrint(node);
		showTreeRec(node.child);
		showTreeRec(node.next);
	}

	/** This method checks to see if any value in an array is null
	 *  as described in the spec for the public methods.
	 * 
	 * @param anArray
	 * The array to be checked for null values.
	 */
	private void hasNullCheck(K[] anArray) {
		for (int i = 0; i < anArray.length; i++) {
			if (anArray[i] == null)
				throw new NullPointerException();
		}
	}

	/**This method iterates through the keyarr and matches the keys given by
	 * keyarr and returns the node that
	 * contains the key in the last index of keyarr.
	 * If the node with the key at the last index of keyarr
	 * does not exist it will return null.
	 * This method also sets our prefixlen field, which is used in add.
	 * @param keyarr
	 * 	Key array to be searched
	 * @return
	 * 	A node in the EntryTree whose key matches keyarr[keyarr.length - 1]
	 */
	private Node searchNodeAtEnd(K[] keyarr) {

		if (keyarr == null || keyarr.length == 0)
			return null;

		Node cursor = root.child;// we start at the child of the node
		int index = 0;// this index cant be greater than keyarr - 1, this will
						// be our prefixlen by adding 1

		while (cursor != null) {
			if (cursor.key.equals(keyarr[index])) {
				if (index + 1 == keyarr.length) {// we are at the end of keyarr
					prefixlen = index + 1;
					return cursor;// so return the cursor
				}
				cursor = cursor.child;// we can keep moving down
				index++;
			} else {
				cursor = cursor.next;// we will keep moving to the sibling
			}
		}
		prefixlen = 0; // if in loop we couldn't match anything in the
					   // given keyarr then we don't have a prefix
		return null;
	}

	/** Links the child to the parent and the child to siblings if there are any
	 * 
	 * @param P 
	 * Node to be added on too, aka Parent node
	 * @param toAdd
	 * The child to be appended to P
	 */
	private void linkParentChild(Node P, Node toAdd) {
		if(toAdd == root)
			throw new IllegalArgumentException("Cant add root");
		
		if (P.child == null) {//situation where the P node has no children
			P.child = toAdd;
			toAdd.parent = P;
		} else {// this case occurs when we need to link siblings
			Node cursor = P.child;
			while (cursor.next != null) {// cursor finds last sibling
				cursor = cursor.next;
			}
			toAdd.parent = P;// now link these two siblings(cursor and toAdd)
			cursor.next = toAdd;
			toAdd.prev = cursor;
		}
	}

	/** Unlinks a given node
	 * 
	 * @param toRemove
	 * node to be removed
	 */
	private void unlink(Node toRemove) {
		if (toRemove.next != null && toRemove.prev == null) {// first sibling
			toRemove.parent.child = toRemove.next;
			toRemove.next.prev = null;
		} else if (toRemove.next != null && toRemove.prev != null) {//middle sibling
			toRemove.prev.next = toRemove.next;
			toRemove.next.prev = toRemove.prev;
		} else if (toRemove.prev != null && toRemove.next == null) {//last sibling
			toRemove.prev.next = null;
		} else if (toRemove.prev == null && toRemove.next == null) {// only child 
			toRemove.parent.child = null;
		}
	}
}
