import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Predicate;

/**
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 * @author Albert Pernia Vazquez
 *
 * <p>This class implements a Tree 2-3 Data Structure. This kind of structure stores the
 *    elements as a tree structure but balanced. So then, every leaf is at the same level in the tree.</p>
 *
 * <p>It is focused on performing fast searchs.</p>
 *
 * <p>Check this <a href="https://en.wikipedia.org/wiki/2&#8208;3_tree"> link</a> for a deepest explanation
 * of how it works.</p>
 *
 * @param <T> Generic element
 *
 * @version 1.2.2 : Search enhanced and other minor improvements
 */

public class Tree23<T extends Comparable<T>> {

	private Node23<T> root;              // The root of the tree
	
	private int size;              // Number of elements inside of the tree
	
	private boolean addition;       // A flag to know if the last element has been added correctly or not

	public Tree23() {
		
		this.root = new Node23<>();

		size = 0;
	}

    public Tree23(Collection<T> elements) {

        this.root = new Node23<>();

        this.size = 0;

		elements.forEach(this::add);	// Java 8
    }

	/**
	 * Adds a new element to the tree keeping it balanced.
	 *
	 * @param element The element to add
	 *
	 * @return If the element has been added (true) or not because it already exists (false)
	 */
	public boolean add(T element) {

		size++;

		addition = false;

		if(root == null || root.getLeftElement() == null) { // first case

			if(root == null) root = new Node23<>();

			root.setLeftElement(element);

			addition = true;
		}
		else {

			Node23<T> newRoot = addElement(root, element); // Immersion

			if(newRoot != null) root = newRoot;
		}

		if(!addition) size--;

		return addition;
	}

	/**
	 * Adds all the elements into the tree.
	 *
	 * @param elements the collection of elements to add
	 *
	 * @return true if all the elements have been inserted, false if one or more elements could not be inserted because
	 *         they already exists
	 */
	public boolean addAll(Collection<T> elements) {
		boolean ok = true;

		for(T e : elements) {

			if(!add(e)) ok = false;
		}

		return ok;
	}

	/**
	 * Adds all the elements into the tree. If one or more elements can't be inserted because they already
	 * exists, all the elements inserted before are removed from the tree.
	 *
	 * @param elements the collection of elements to add
	 *
	 * @return true if all the elements have been inserted, false if one or more elements could not be inserted because
	 *         they already exists
	 */
	public boolean addAllSafe(Collection<T> elements) {
		int inserted = 0, i = 0;

		for(T e : elements) {

			if (!add(e)) {

				// for each element inserted from the collection, we remove it
				for(T a : elements) {

					if(i >= inserted) return false; // when all the elements inserted before the error have been removed, we return false

					else remove(a);
				}
			}
			else inserted++;
		}

		return true;
	}

	/**
	 * The algorithm stores the new element ordered as the 'compareTo' method of the Object is done. So the tree can store
	 * the data in Ascending or Descending mode.
	 *
	 * During the top down of the recursive, the algorithm tries to find the deepest level of the tree, where the new element will be saved.
	 *
	 * On the bottom up, if the new element has to be added inside a node with two existing elements (3-node), then we have to
	 * create a new tree level elevating a new node with the element which should be in the middle of the three.
	 *
	 * In the code, this situation happens when the Node returned by the method is not null. If it is null, the node where
	 * the new element has been inserted was a 2-node (there was an element position still empty).
	 *
	 * Also, during the bottom up, the algorithm checks if the tree is well-balanced correcting the structure if it isn't.
	 *
	 * @param current The child where we are
	 * @param element The element to insert
	 *
	 * @return If there is a new level to add (we have tried to add a new element to a 3-node) or we don't have to do nothing (node is null)
	 */
	private Node23<T> addElement( Node23<T> current, T element) {

		Node23<T> newParent = null;

		// We aren't in the deepest level yet
		if(!current.isLeaf()) {

		    Node23<T> sonAscended;

			if  (current.getLeftElement().compareTo(element) == 0 ||
				(current.is3Node() && current.getRightElement().compareTo(element) == 0)) {

				// Already exists. This condition can be modified for the particular needs of any programmer
			}
			// The new element is smaller than the left element
			else if (current.getLeftElement().compareTo(element) > 0) {

				sonAscended = addElement(current.getLeft(), element);

				// Case sonAscended != null --> the element has been added on a 3-node (there were 2 elements)
				if (sonAscended != null) { // A new node comes from the left branch

					// The new element, in this case, is always less than the current.left
					if (current.is2Node()) {

						current.setRightElement( current.getLeftElement());  // shift the current left element to the right
						current.setLeftElement( sonAscended.getLeftElement());
						current.setRight( current.getMid());
						current.setMid( sonAscended.getMid());
						current.setLeft( sonAscended.getLeft());
					}
					else { // In this case we have a new split, so the current element in the left will go up

						// We copy the right part of the subtree
						Node23<T> rightCopy = new Node23<>(current.getRightElement(), null, current.getMid(), current.getRight());

						// Now we create the new "structure", pasting the right part
						newParent = new Node23<>(current.getLeftElement(), null, sonAscended, rightCopy);
					}
				}

				// Case: the ascended element is bigger than the left element and less than the right element
			} else if (current.is2Node() || (current.is3Node() && current.getRightElement().compareTo(element) > 0)) {

				sonAscended = addElement(current.getMid(), element);

				if (sonAscended != null) { // A new split

					// The right element is empty, so we can set the ascended element in the left and the existing left element into the right
					if (current.is2Node()) {

						current.setRightElement( sonAscended.getLeftElement());
						current.setRight( sonAscended.getMid());
						current.setMid( sonAscended.getLeft());
					}
					else { // Another case we have to split again

						Node23<T> leftNode 	= new Node23<>(current.getLeftElement(), null, current.getLeft(), sonAscended.getLeft());
						Node23<T> midNode 	= new Node23<>(current.getRightElement(), null, sonAscended.getMid(), current.getRight());
						newParent 	= new Node23<>(sonAscended.getLeftElement(), null, leftNode, midNode);
					}
				}
				// The new element is bigger than the right element
			} else if (current.is3Node() && current.getRightElement().compareTo(element) < 0) {

				sonAscended = addElement(current.getRight(), element);

				if (sonAscended != null) { // Split, the right element goes up

					Node23<T> leftCopy   = new Node23<>(current.getLeftElement(), null, current.getLeft(), current.getMid());
					newParent       = new Node23<>(current.getRightElement(), null, leftCopy, sonAscended);
				}
			}
		}
		else { // We are in the deepest level

			addition = true;

			// The element already exists
			if (current.getLeftElement().compareTo(element) == 0 || (current.is3Node() && current.getRightElement().compareTo(element) == 0)) {

				addition = false;
			}
			else if (current.is2Node()) { // an easy case, there is not a right element

				// if the current left element is bigger than the new one --> we shift the left element to the right
				if (current.getLeftElement().compareTo(element) > 0) {

					current.setRightElement( current.getLeftElement());
					current.setLeftElement( element);
				}
				// if the new element is bigger, we add it in the right directly
				else if (current.getLeftElement().compareTo(element) < 0) current.setRightElement(element);
			}
			// Case 3-node: there are 2 elements in the node and we want to add another one. We have to split the node
			else newParent = split(current, element);
		}

		return newParent;
	}

    /**
     * Creates the new node structure that will be attached during the bottom up of the addElementI method.
     *
     * @param current   The node where the split takes place
     * @param element   The element we are trying to add.
     * @return          A 2-node structure with a non null left and mid node.
     */
	private Node23<T> split(Node23<T> current, T element) {
        Node23<T> newParent = null;

        // The left element is bigger, so it will go up letting the new element on the left
        if (current.getLeftElement().compareTo(element) > 0) {

            Node23<T> left   = new Node23<>(element, null);
            Node23<T> right  = new Node23<>(current.getRightElement(), null);
            newParent   = new Node23<>(current.getLeftElement(), null, left, right);

        } else if (current.getLeftElement().compareTo(element) < 0) {

            // The new element is bigger than the current on the right and less than the right element
            // The new element goes up
            if (current.getRightElement().compareTo(element) > 0) {

                Node23<T> left   = new Node23<>(current.getLeftElement(), null);
                Node23<T> right  = new Node23<>(current.getRightElement(), null);
                newParent   = new Node23<>(element, null, left, right);

            } else { // The new element is the biggest one, so the current right element goes up

                Node23<T> left   = new Node23<>(current.getLeftElement(), null);
                Node23<T> right  = new Node23<>(element, null);
                newParent   = new Node23<>(current.getRightElement(), null, left, right);
            }
        }
        return newParent;
    }

	/**
	 * Removes all of the elements from this Tree23 instance.
	 */
	public void clear() {
		this.size = 0;
		this.root = null;	// GC do the rest
	}

	/**
	 * Creates a copy of this Tree23 instance.
	 *
	 * @return A copy of this Tree23 instance
     */
	@Override
	public Tree23<T> clone() {
		Tree23<T> clone = new Tree23<>();
		if(!isEmpty()) clone(root, clone);
		return clone;
	}

	// Immersion
	private void clone(Node23<T> current, Tree23<T> clone) {
		if(current != null) {
			if(current.isLeaf()) {
				clone.add(current.getLeftElement());
				if(current.getRightElement() != null) clone.add(current.getRightElement());
			}
			else {
				clone(current.getLeft(), clone);
				clone.add(current.getLeftElement());
				clone(current.getMid(), clone);
				if(current.getRightElement() != null) {
					if(!current.isLeaf()) clone.add(current.getRightElement());
					clone(current.getRight(), clone);
				}
			}
		}
	}

	/**
	 * @param element The element to find
	 *
	 * @return true if this tree contains the specified element, false if not
	 */
	public boolean contains(T element) {
		return find(element) != null;
	}

	/**
	 * Searches an element inside of the tree.
	 *
	 * @param element The element to find
	 *
	 * @return the element found or null if it doesn't exist
	 */
	public T find(T element) {
		return find(root, element);
	}

	private T find(Node23<T> current, T element) {
		T found = null;

		if(current != null) {
			// Trivial case, we have found the element
			if (current.getLeftElement() != null && current.getLeftElement().equals(element))
				found = current.getLeftElement();
			else {
				// Second trivial case, the element to find equals the right element
				if(current.getRightElement() != null && current.getRightElement().equals(element))
					found = current.getRightElement();
				else {
					// Recursive cases
					if(current.getLeftElement().compareTo(element) > 0) {
						found = find(current.getLeft(), element);
					}
					else if(current.getRight() == null || current.getRightElement().compareTo(element) > 0) {
						found = find(current.getMid(), element);
					}
					else if (current.getRightElement().compareTo(element) < 0) {
						found = find(current.getRight(), element);
					}
					else return null;
				}
			}
		}
		return found;
	}

	/**
	 * @return The min element of the tree
	 */
	public T findMin() {
		if (isEmpty()) return null;
		return findMin(root);
	}

	// Immersion
	private T findMin(Node23<T> current) {
		if(current.getLeft() == null) return current.getLeftElement();	// trivial case
		else return findMin(current.getLeft());						// recursive case
	}

	/**
	 * @return The max element of the tree
	 */
	public T findMax() {
		if (isEmpty()) return null;
		return findMax(root);
	}

	// Immersion
	private T findMax(Node23<T> current) {
		// Recursive case
		if(current.getRightElement() != null && current.getRight() != null)
			return findMax(current.getRight());
		else if(current.getMid() != null) return findMax(current.getMid());

		// Trivial case
		if(current.getRightElement() != null) return current.getRightElement();
		else return current.getLeftElement();
	}

    /**
     * @return the number of levels of the tree (max deep)
     */
	public long getLevel() {
		Node23<T> aux = root;
        int level = 0;

        while(aux != null) {
            aux = aux.getLeft();
            level++;
        }

        return level;
	}

	/**
	 * Prints the elements of the tree in order.
	 */
	public void inOrder() {
		if (!isEmpty()) inOrder(root);    // Immersion
		else System.out.print("The tree is empty");
		System.out.println();

	}

	private void inOrder( Node23<T> current) {
		if(current != null) {
			if(current.isLeaf()) {
				System.out.print(current.getLeftElement() + " ");
				if(current.getRightElement() != null)
					System.out.print(current.getRightElement() + " ");
			}
			else {
				inOrder(current.getLeft());
				System.out.print(current.getLeftElement() + " ");
				inOrder(current.getMid());
				if(current.getRightElement() != null) {
					if(!current.isLeaf())
						System.out.print(current.getRightElement() + " ");
					inOrder(current.getRight());
				}
			}
		}
	}

	/**
	 * Prints the elements of the tree in order if they accomplish a condition.
	 *
	 * @param predicate The condition that an element must accomplish to be printed
	 */
	public void inOrder(Predicate<T> predicate) {

		if(!isEmpty()) inOrder(root, predicate);    // Immersion
		else System.out.print("The tree is empty");
		System.out.println();
	}

	private void inOrder( Node23<T> current, Predicate<T> predicate) {
		if(current != null) {
			if(current.isLeaf()) {
				if(predicate.test(current.getLeftElement()))
					System.out.print(current.getLeftElement() + " ");
				if(current.getRightElement() != null && predicate.test(current.getRightElement())) {
					System.out.print(current.getRightElement() + " ");
				}
			}
			else {
				inOrder(current.getLeft(), predicate);
				if(predicate.test(current.getLeftElement()))
					System.out.print(current.getLeftElement() + " ");
				inOrder(current.getMid(), predicate);
				if(current.getRightElement() != null) {
					if(!current.isLeaf() && predicate.test(current.getRightElement()))
						System.out.print(current.getRightElement() + " ");
					inOrder(current.getRight(), predicate);
				}
			}
		}
	}

	/**
	 * @return True if the tree is empty, false if not
	 */
	public boolean isEmpty() {
		if(root == null) return true;
		return root.getLeftElement() == null;
	}

	/**
	 * Finds an element inside the tree and modifies it.
	 *
	 * @param which  The element to be modified
	 * @param update The update of the element
	 *
	 * @return true if the element has been found or not, false if not
	 */
	public boolean modify(T which, T update) {
		boolean modified = false;

        /*
         * If the element is found, this is deleted adding it again with the modification done. It is a good way
         * because the element modification could affect the data structure organization (the attribute/field used for the indexation).
         */
		if (contains(which)) {

			modified = true;

			remove(which);
			add(update);
		}

		return modified;
	}

	/**
	 * @deprecated This function has been replaced by the find method, which is faster
	 *
	 * If the element is found, this is deleted adding it again with the modification done. It is a good way
	 * because the element modification could affect the data structure organization (the attribute/field used for the indexation).
	 *
	 * @param current The current child where we are
	 * @param element The element to modify
	 *
	 * @return If the element has been found or not
	 */
	@Deprecated
	private boolean modify(Node23<T> current, T element) {

		boolean modified = false;

		if(current != null) {

			if(current.getLeftElement().equals(element)) {

				remove(element);
				modified = true;
			}
			else {

				if(current.getRightElement() != null && current.getRightElement().equals(element)) {

					modified = true;

					remove(element);
				}
				else {

					modified = modify(current.getLeft(), element);

					if(!modified) modified = modify(current.getMid(), element);

					if(current.getRightElement() != null && !modified)
						modified = modify(current.getRight(), element);
				}
			}
		}

		return modified;
	}

	public void levelOrder() { levelOrder(root); }

	public void levelOrder( Node23<T> root) {
		if (root == null) return;
		Queue<Node23<T>> q = new LinkedList<>();
		int level = 0;
		q.add(root);
		q.add(null);
		while (!q.isEmpty()) {
			Node23<T> curr = q.poll();
			if (curr == null) {
				if (!q.isEmpty()) {
					q.add(null);
					System.out.println();
					level++;
				}
			} else {
				System.out.println("leftElement = " + curr.getLeftElement() + ", rightElement = " + curr.getRightElement() +
					", leaf=" + curr.isLeaf() + ", 2Node=" + curr.is2Node() + ", 3Node=" + curr.is3Node() +
					", level=" + level);
				if (curr.getLeft() != null && (curr.getLeftElement() != null || curr.getLeft().getRightElement() != null)) {
					q.add(curr.getLeft());
					System.out.print("\t\tleftChild: " + curr.getLeft().getLeftElement() + ", " + curr.getLeft().getRightElement());
				}
				if (curr.getMid() != null && (curr.getMid().getLeftElement() != null || curr.getMid().getRightElement() != null)) {
					q.add(curr.getMid());
					System.out.print("\t\tmiddleChild: " + curr.getMid().getLeftElement() + ", " + curr.getMid().getRightElement());
				}
				if (curr.getRight() != null && (curr.getRight().getLeftElement() != null || curr.getRight().getRightElement() != null)) {
					q.add(curr.getRight());
					System.out.print("\t\trightChild: " + curr.getRight().getLeftElement() + ", " + curr.getRight().getRightElement());
				}

				if ( !curr.isLeaf() ) System.out.println();
			}
		}
	}

	/**
     * Prints the elements of the tree in pre order.
     */
	public void preOrder() {
        if (!isEmpty()) {
            preOrder(root);
        }
        else System.out.print("The tree is empty");
		System.out.println();
    }

    private void preOrder(Node23<T> current) {
        if(current != null) {
            System.out.print(current.getLeftElement() + " ");
            preOrder(current.getLeft());
            preOrder(current.getMid());

            if (current.getRightElement() != null) {
                System.out.print(current.getRightElement() + " ");
                preOrder(current.getRight());
            }
        }
    }

    /**
     * Prints the elements of the tree in pre order mode if they accomplish a condition.
     *
     * @param predicate The condition that an element must accomplish to be printed
     */
    public void preOrder(Predicate<T> predicate) {

        if (!isEmpty()) preOrder(root, predicate);
        else System.out.println("The tree is empty");
    }

    private void preOrder(Node23<T> current, Predicate<T> predicate) {

        if(current != null) {

            if (predicate.test(current.getLeftElement()))
            	System.out.println(current.getLeftElement() + " ");
            preOrder(current.getLeft());
            preOrder(current.getMid());

            if (current.getRightElement() != null) {

                if (predicate.test(current.getRightElement()))
                	System.out.print(current.getRightElement() + " ");
                preOrder(current.getRight());
            }
        }
    }

	/**
	 * Deletes an element from the tree.
	 *
	 * @param element The element to delete
	 * 
	 * @return True if the element has been deleted, false if not (the element was not in the tree)
	 */
	public boolean remove(T element) {
		boolean deleted;

        // We decrease the number of levels at the start, if the element is not deleted, we increase the value at the end
        this.size--;

		deleted = remove(root, element); // Immersion
		
		root.rebalance();
		
		if(root.getLeftElement() == null) root = null; // We have deleted the last element of the tree

        if(!deleted) this.size++;

		return deleted;
	}

	/**
	 * In a recursive way, the algorithm tries to find the element to delete from the tree.
	 *
	 * When it finds the element, we can have one of this two situations:
	 *
	 *
	 * 		A. The element we have to delete was in the deepest level of the tree, where we know all the rebalance patterns
	 * 			(see the method "rebalance" below implemented in the private class Node) so we will not have many troubles in
	 * 			this case because there are not more levels below of the current one.
	 *
	 * 		B. The element to delete is not in the deepest level of the tree. In this situation we must force a swap.
	 * 		   What we have to do is:
	 *
	 * 		   	- If we are deleting an element in the mid side, we are gonna replace it with the min value of the branch causing
	 * 		   	  an unbalanced case but in the deepest level.
	 *
	 * 		   	- If the element to delete is in the right side of the tree, we replace that element with the max value of
	 * 		   	  the branch. Then, we have a unbalanced case but in the deepest level.
	 *
	 * 			These processes achieves easy rebalance cases, excepting the critical case (full explained in the "rebalance" method):
	 *
	 * 				- If after the deletion of the element a node has been empty and we don't have enough elements in the deepest level
	 * 				  of the tree to rebalance it, the tree will be reorganized from a higher level which increases the cost.
	 *
	 *
	 * @param current The current node where we are
	 * @param element The element to delete
	 * 
	 * @return True if the element has been deleted or false if not
	 */
	private boolean remove(Node23<T> current, T element) {
		boolean deleted = true;

		// Trivial case, we are in the deepest level of the tree but we have not found the element (it does not exist)
		if(current == null) deleted = false;

		else {
			// Recursive case, we are still finding the element to delete
			if(!current.getLeftElement().equals(element)) {

				// If there is no element in the right (2 Node) or the element to delete is less than the right element
				if(current.getRightElement() == null || current.getRightElement().compareTo(element) > 0) {
					
					// The left element is bigger than the element to delete, so we go through the left child
					if(current.getLeftElement().compareTo(element) > 0) {
						
						deleted = remove(current.getLeft(), element);
					}
					else { // If not, we go through the mid child
						
						deleted = remove(current.getMid(), element);
					}
				}
				else {

					// If the element to delete does not equals the right element, we go through the right child
					if(!current.getRightElement().equals(element)) {
						
						deleted = remove(current.getRight(), element);
					}
					else { // If not, we have found the element

						// Situation A, the element equals the right element of a leaf so we just have to delete it
						if(current.isLeaf()) current.setRightElement(null);

						else { // We found the element but it is not in the leaf, this is the situation B

							// We get the min element of the right branch, remove it from its current position and put it
							// where we found the element to delete
							T replacement = current.getRight().replaceMin();

							current.setRightElement(replacement);
						}
					}
				}
			}
			// The left element equals the element to delete
			else {
				
				if(current.isLeaf()) { // Situation A

					// The left element, the element to remove, is replaced by the right element
					if(current.getRightElement() != null) { 
						
						current.setLeftElement(current.getRightElement());
						
						current.setRightElement(null);
					}
					else { // If there is no element in the right, a rebalance will be necessary

						current.setLeftElement(null); // We let the node empty

						// We warn on the bottom up that a node has been deleted (is empty) and a rebalance is necessary
						// in THAT level of the tree
						return true;
					}
				}
				else { // Situation B

					// We move the max element of the left branch where we have found the element
					T replacement = current.getLeft().replaceMax();
					
					current.setLeftElement(replacement);
				}
			}
		}
			
		if(current != null && !current.isBalanced()) {
						
			current.rebalance();  // The bottom level have to be rebalanced
		}
		else if(current != null && !current.isLeaf()) {
			
			boolean balanced = false;
			
			while(!balanced) {
				
				if(current.getRight() == null) {

					// Critical case of the situation B at the left child
					if(current.getLeft().isLeaf() && !current.getMid().isLeaf()) {

						T replacement = current.getMid().replaceMin();

						T readdition = current.getLeftElement();

						current.setLeftElement(replacement);
						
						add(readdition);

						// Critical case of hte situation B at the right child
					} else if(!current.getLeft().isLeaf() && current.getMid().isLeaf()) {

						if(current.getRightElement() == null) {

							T replacement = current.getLeft().replaceMax();

							T readdition = current.getLeftElement();

							current.setLeftElement(replacement);
							
							add(readdition);
						}
					}
				}
				// It is important to note that we can't use the 'else' sentence because the situation could have changed in the if above
				if(current.getRight() != null) {
									
					if(current.getMid().isLeaf() && !current.getRight().isLeaf()) {

						current.getRight().rebalance();
					}
					if(current.getMid().isLeaf() && !current.getRight().isLeaf()) {

						T replacement = current.getRight().replaceMin();

						T readdition = current.getRightElement();

						current.setRightElement(replacement);
						
						add(readdition);
					}
					else balanced = true;
				}
				if(current.isBalanced()) balanced = true;
			}
		}
		
		return deleted;
	}

	/**
	 * @return The number of elements inside of the tree
	 * */
	public int size() {
		return size;
	}
}
