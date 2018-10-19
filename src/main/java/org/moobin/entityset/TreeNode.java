/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2017 Magnus Lenti (moobin@moobin.org)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.moobin.entityset;

/**
 * @author Magnus Lenti
 *
 */
public interface TreeNode {
	
	/**
	 * 
	 */
	
	/**
	 * Get the parent node
	 * 
	 * @return
	 */
	TreeNode getParent();
	
	/**
	 * Get the left sub tree
	 * 
	 * @return
	 */
	TreeNode getLeftSubTree();
	
	/**
	 * Get the right sub tree
	 * 
	 * @return
	 */
	TreeNode getRightSubTree();

	/**
	 * Get the size of this sub tree
	 * 
	 * @return
	 */
	int getSize();
	
	/**
	 * Remove this node from the tree
	 */
	void remove();

	/**
	 * Set the left sub tree
	 * 
	 * @param left
	 */
	void setLeft(TreeNode left);
	
	/**
	 * Set the right sub tree
	 * 
	 * @param right
	 */
	void setRight(TreeNode right);

	/**
	 * Get the root node of this tree
	 * 
	 * @return
	 */
	default TreeNode getRoot() {
		return getParent() == null ? this : getParent().getRoot();
	}

	/**
	 * The size of the left sub tree
	 * 
	 * @return
	 */
	default int leftSize() {
		return getLeftSubTree() == null ? 0 : getLeftSubTree().getSize();
	}

	/**
	 * The size of the right sub tree
	 * 
	 * @return
	 */
	default int rightSize() {
		return getRightSubTree() == null ? 0 : getRightSubTree().getSize();
	}
	
	/**
	 * Test if this is the root node
	 * 
	 * @return
	 */
	default boolean isRoot() {
		return getParent() == null;
	}
	
	/**
	 * Test if this is the right side of the parent
	 * 
	 * @return
	 */
	default boolean isRightSide() {
		return !isRoot() && this == getParent().getRightSubTree();
	}
	
	/**
	 * Test if this is the left side of the parent
	 * 
	 * @return
	 */
	default boolean isLeftSide() {
		return !isRoot() && this == getParent().getLeftSubTree();
	}

	/**
	 * Get the n:th node of this sub tree
	 * 
	 * @param index
	 * @return
	 */
	default TreeNode get(int index) {
		if (index < 0 || index >= getSize()) {
			throw new IndexOutOfBoundsException();
		}
		if (index == leftSize()) return this;
		if (index < leftSize()) return getLeftSubTree().get(index);
		return getRightSubTree().get(index - leftSize() - 1);
	}

	/**
	 * Get the first node of this sub tree
	 * 
	 * @return
	 */
	default TreeNode getFirst() {
		return getLeftSubTree() == null ? this : getLeftSubTree().getFirst();
	}

	/**
	 * Get the last node of this sub tree
	 * 
	 * @return
	 */
	default TreeNode getLast() {
		return getRightSubTree() == null ? this : getRightSubTree().getLast();
	}
	
	/**
	 * Get the next node in the tree
	 * 
	 * @return
	 */
	default TreeNode getNext() {
		if (getRightSubTree() != null) {
			return getRightSubTree().getFirst();
		}
		for (TreeNode node = this; node != null; node = node.getParent()) {
			if (node.isLeftSide()) {
				return node.getParent();
			}
		}
		return null;
	}
	
	/**
	 * Get the previous node in the tree
	 * @return
	 */
	default TreeNode getPrevious() {
		if (getLeftSubTree() != null) {
			return getLeftSubTree().getLast();
		}
		for (TreeNode node = this; node != null; node = node.getParent()) {
			if (node.isRightSide()) {
				return node.getParent();
			}
		}
		return null;
	}

	/** 
	 * Get the position of this node in the tree
	 * 
	 * @return
	 */
	default int getIndex() {
		if (getParent() == null) {
			return leftSize();
		}
		if (isRightSide()) {
			return getParent().getIndex() + leftSize() + 1;
		}
		return getParent().getIndex() - getSize() + leftSize();
	}

}
