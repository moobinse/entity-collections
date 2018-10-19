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
package org.moobin.entityset.impl;

import org.moobin.entityset.TreeNode;

/**
 * @author Magnus Lenti
 *
 */
public class TreeNodeImpl implements TreeNode {

	protected TreeNodeImpl left;
	protected TreeNodeImpl right;
	protected TreeNodeImpl parent;
	protected int size;

	@Override
	public TreeNodeImpl getLeftSubTree() {
		return left;
	}

	@Override
	public TreeNodeImpl getRightSubTree() {
		return right;
	}

	@Override
	public TreeNodeImpl getParent() {
		return parent;
	}

	@Override
	public int getSize() {
		return size;
	}

	private void resize(int diff) {
		size += diff;
		if (parent != null) {
			parent.resize(diff);
		}
	}
	
	@Override
	public void setLeft(TreeNode node) {
		assert left == null;
		assert node.getSize() == 1;
		left = (TreeNodeImpl) node;
		left.parent = this;
		size++;
	}
	
	@Override
	public void setRight(TreeNode node) {
		assert right == null;
		assert node.getSize() == 1;
		right = (TreeNodeImpl) node;
		right.parent = this;
		size++;
	}
	
	@Override
	public void remove() {
		if (getSize() == 1) {
			if (isRoot()) {
				recycle(this);
			}
			else if (isLeftSide()) {
				parent.left = null;
				parent.resize(-1);
				recycle(this);
			}
			else {
				parent.left = null;
				parent.resize(-1);
				recycle(this);
			}
		}
		else if (leftSize() > rightSize()) {
			// find previous node, take its values and remove it
			TreeNodeImpl previous = (TreeNodeImpl) getPrevious();
			//not null, has no right side
			previous.parent.right = previous.left;
			if (previous.left != null) {
				previous.left.parent = previous.parent;
			}
			previous.parent.resize(-1);
			takeOver(previous);
			recycle(previous);
		}
		else {
			// find next node, take its values and remove it
			TreeNodeImpl next = (TreeNodeImpl) getNext();
			//not null, has no left side
			next.parent.left = next.right;
			if (next.right != null) {
				next.right.parent = next.parent;
			}
			next.parent.resize(-1);
			takeOver(next);
			recycle(next);
		}
	}
	
	
	protected void takeOver(TreeNodeImpl node) {
	}
	
	private void recycle(TreeNodeImpl node) {
		node.left = null;
		node.right = null;
		node.parent = null;
		node.size = 0;
	}
	
}
