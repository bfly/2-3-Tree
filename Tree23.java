import java.util.Collection;

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
 * <p>Check this <a href="https://en.wikipedia.org/wiki/2–3_tree"> link</a> for a deepest explanation
 * of how it works.</p>
 *
 * @param <T> Generic element
 *
 * @version 1.2.2 : Search enhanced and other minor improvements
 */

public class Tree23<T extends Comparable<T>> {

	private Node root;              // The root of the tree
	
	private int size;              // Number of size inside of the tree
	
	private static final int    ROOT_IS_BIGGER = 1;
	private static final int    ROOT_IS_SMALLER = -1;

	private boolean addition;       // A flag to know if the last element has been added correctly or not

	public Tree23() {
		
		this.root = new Node();

		size = 0;
	}

    public Tree23(Collection<T> elements) {

        this.root = new Node();

        this.size = 0;

        for(T e : elements) add(e);

    }

    /**
     * @return The number of elements inside of the tree
     */

    public int size() {
		
		return size;
	}


    /**
     * @return the number of levels of the tree (max deep)
     */
	public long getLevel() {
		Node aux = root;
        int level = 0;

        while(aux != null) {

            aux = root.getLeftSon();
            level++;
        }

        return level;
	}


	/**
	 * @return True if the tree is empty, false if not
	 */
	public boolean isEmpty() {
		
		if(root == null) return true;
		
		if(root.getLeftElement() == null) return true;
		
		return false;
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
			
			if(root == null) root = new Node();
			
			root.setLeftElement(element);
			
			addition = true;
		}
		else {
			
			Node newRoot = addElementI(root, element); // Immersion
		
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
	private Node addElementI(Node current, T element) {
		
		Node newParent = null;
		Node sonAscended = null;
				
		// We aren't in the deepest level yet
		if(!current.isLeaf()) {

            if(current.getLeftElement().compareTo(element) == 0 || (current.getRightElement() != null && current.getRightElement().compareTo(element) == 0)) {
				
				// Already exists. This condition can be modified for the particular needs of any programmer
			}
            // The new element is smaller than the left element
			else if(current.getLeftElement().compareTo(element) == ROOT_IS_BIGGER) {

				sonAscended = addElementI(current.left, element);

                // Case sonAscended != null --> the element has been added on a 3-node (there were 2 elements)
				if(sonAscended != null) { // A new node comes from the left branch

                    // The new element, in this case, is always less than the current.left
                    if(current.getRightElement() == null) {

						current.setRightElement(current.getLeftElement());  // shift the current left element to the right
							
						current.setLeftElement(sonAscended.getLeftElement());
							
						current.setRightSon(current.getMidSon());
							
						current.setMidSon(sonAscended.getMidSon());
							
						current.setLeftSon(sonAscended.getLeftSon());
					}
					else { // In this case we have a new split, so the current element in the left will go up

						// We copy the right part of the subtree
						Node rightCopy = new Node(current.getRightElement(), null, current.getMidSon(), current.getRightSon());
						
						// Now we create the new "structure", pasting the right part
						newParent = new Node(current.getLeftElement(), null, sonAscended, rightCopy);
					}
				}
				
		    // Case: the ascended element is bigger than the left element and less than the right element
			} else if(current.getRightElement() == null || (current.getRightElement() != null && current.getRightElement().compareTo(element) == ROOT_IS_BIGGER)) {

				sonAscended = addElementI(current.mid, element);
				
				if(sonAscended != null) { // A new split

                    // The right element is empty, so we can set the ascended element in the left and the existing left element into the right
					if(current.getRightElement() == null) {

						current.setRightElement(sonAscended.getLeftElement());
						
						current.setRightSon(sonAscended.getMidSon());
						
						current.setMidSon(sonAscended.getLeftSon());
					}
					else { // Another case we have to split
						
						Node left = new Node(current.getLeftElement(), null, current.getLeftSon(), sonAscended.getLeftSon());
						
						Node mid = new Node(current.getRightElement(), null, sonAscended.getMidSon(), current.getRightSon());
						
						newParent = new Node(sonAscended.getLeftElement(), null, left, mid);
					}
				}

                // The new element is bigger than the right element
			} else if(current.getRightElement() != null && current.getRightElement().compareTo(element) == ROOT_IS_SMALLER) {

				sonAscended = addElementI(current.right, element);

				if(sonAscended != null) { // Split, the right element goes up
					
					Node leftCopy = new Node(current.getLeftElement(), null, current.getLeftSon(), current.getMidSon());

                    newParent = new Node(current.getRightElement(), null, leftCopy, sonAscended);
				}
			}
		}
		else { // We are in the deepest level

			addition = true;

            // The element already exists
			if(current.getLeftElement().compareTo(element) == 0 || (current.getRightElement() != null && current.getRightElement().compareTo(element) == 0)) {
								
				addition = false;
			}
			else if(current.getRightElement() == null) { // an easy case, there is not a right element

                // if the current left element is bigger than the new one --> we shift the left element to the right
				if(current.getLeftElement().compareTo(element) == ROOT_IS_BIGGER) {
					
					current.setRightElement(current.getLeftElement());

                    current.setLeftElement(element);
				}
                // if the new element is bigger, we add it in the right directly
				else if(current.getLeftElement().compareTo(element) == ROOT_IS_SMALLER) {
					
					current.setRightElement(element);
				}
			}
            // Case 3-node: there are 2 elements in the node and we want to add another one. We have to split the node
			else {

                // The left element is bigger, so it will go up letting the new element on the left
                if (current.getLeftElement().compareTo(element) == ROOT_IS_BIGGER) {

                    Node left = new Node(element, null);

                    Node right = new Node(current.getRightElement(), null);

                    newParent = new Node(current.getLeftElement(), null, left, right);

                } else if (current.getLeftElement().compareTo(element) == ROOT_IS_SMALLER) {

                    // The new element is bigger than the current on the right and less than the right element
                    // The new element goes up
                    if (current.getRightElement().compareTo(element) == ROOT_IS_BIGGER) {

                        Node left = new Node(current.getLeftElement(), null);

                        Node right = new Node(current.getRightElement(), null);

                        newParent = new Node(element, null, left, right);

                    } else { // The new element is the biggest one, so the current right element goes up

                        Node left = new Node(current.getLeftElement(), null);

                        Node right = new Node(element, null);

                        newParent = new Node(current.getRightElement(), null, left, right);
                    }
                }
            }
		}

		return newParent;
	}


	/**
	 * Debug of the tree
	 */
	public void debug_tree() {
		
		System.out.println("\nStarting debug!\n-----------------");
		debug_treeI(root);
	}
	
	private void debug_treeI(Node node) {
		
		if(node != null) {
			
			// Primer cas

			System.out.print("\nLeft Element : " + node.leftElement.toString());

			if(node.rightElement != null) System.out.println(" and right Element : " + node.rightElement.toString());
			else System.out.println("");
			
			System.out.println("---------------------------------------\n");
			
			System.out.println("Left from " + node.leftElement.toString());
			debug_treeI(node.left);

			System.out.print("Mid from " + node.leftElement);
			
			if(node.rightElement != null) System.out.println(" and " + node.rightElement.toString());
			else System.out.println("");
			debug_treeI(node.mid);
			
			if(node.right != null && node.rightElement != null) {
				
				System.out.println("Right from " + node.rightElement.toString());
				debug_treeI(node.right);
				
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

		deleted = removeI(root, element); // Immersion
		
		root.rebalance();
		
		if(root.getLeftElement() == null) root = null; // We have deleted the last element of the tree

        if(!deleted) this.size++;

		return deleted;
		
	}

    //TODO: translate this comment to English!!
	/**
	 * De forma recursiva, s'encarrega de buscar l'element que ha d'el·liminar de l'arbre 2-3.
	 * 
	 * Un cop el troba, ens podem trobar en dues situacions:
	 * 
	 * 		A. L'element que haviem d'el·liminar es trobava al nivell mes profund de l'arbre, d'on coneixem 
	 * 		   tots els patrons de rebalanceig i no tenim (gaires) complicacions ja que no hi ha nivells inferiors.
	 * 
	 * 		B. L'element es troba entre mig de l'arbre. En aquesta situacio hem de forçar una substitucio d'elements.
	 * 		   El que fem es que si el·liminem un node entre mig pel costat del mig, substituirem el valor de l'element
	 *         que anavem a el·liminar pel valor del node mes baix de l'arbre. D'aquesta manera provoquem un desbalanceig 
	 *         en l'ultim nivell.
	 *         
	 *         En el cas d'un element per la dreta, el substituim per l'element mes gran del arbre/subarbre (on ens trobem).
	 *         D'aquesta forma tenim el mateix, un desbalanceig real en el nivell mes profund i res mes.
	 *         
	 *     Tot aixo fa que el rebalanceig sigui bastant senzill, amb l'excepcio d'un cas on es complica tant per les situacions A com B:
	 *     
	 *  	- Si en el nivell profund no podem rebalancejar perque un node s'ha quedat sense size i no tenim suficients size en el ultim
	 *  	  i penultim nivell com per balancejar-lo, vol dir que hem de reorganitzar mes a munt, fent que es compliqui la reorganitzacio
	 *        i que en la pujada de la recursiva haguem de anar comprovant que tot s'esta remuntant correctament.
	 * 
	 * 
	 * @param current El node sobre el qual ens trobem
	 * @param element L'element a el·liminar
	 * 
	 * @return Si s'ha pogut el·liminar (true) o no (false)
	 */
	private boolean removeI(Node current, T element) {
		boolean deleted = true;

		if(current == null) { // Cas trivial, hem arribat al nivell mes profund pero no hem trobat l'element -> no existeix
			
			deleted = false;
			this.size++; // Com ho havia decrementat d'inici, torno a incrementar
		}
		else {
			// Tractem el cas recursiu, on l'element que ens han donat per el·liminar encara no l'hem trobat
			if(!current.getLeftElement().equals(element)) {
				
				// Si no hi ha element dret o be l'element es mes petit que l'element de la dreta, ens mourem pel fill esquerra o mig
				if(current.getRightElement() == null || current.getRightElement().compareTo(element) == ROOT_IS_BIGGER) {
					
					// L'element de l'esquerra es mes gran, hem de seguir pel fill esquerra
					if(current.getLeftElement().compareTo(element) == ROOT_IS_BIGGER) {
						
						deleted = removeI(current.left, element);
					}
					else { // Seguim pel fill del mig
						
						deleted = removeI(current.mid, element);
					}
				}
				else {
					
					// Si l'element de la dreta no coincideix amb el que busquem, llavors hem de seguir pel fill dret
					if(!current.getRightElement().equals(element)) {
						
						deleted = removeI(current.right, element);
					}
					else { // Hem trobat l'element i esta posicionat en un element de la dreta del node en el que ens trobem
						
						
						// Cas facil, l'element es trobava al final de l'arbre i per tant nomes l'hem d'el·liminar
						if(current.isLeaf()) {
							
							current.setRightElement(null); // Treiem l'element i ja esta, no afecta a cap node ni desbalanceja
						}
						else { // Aqui potser tenim una festa maca, l'arbre es desbalancejara... 
							
							// (!!!!) Cosa interessant aqui
							
							// Extraiem el minim node per la dreta, el col·loquem en la posicio on s'anava a el·liminar
							T replacement = current.getRightSon().replaceMin();
							
							current.setRightElement(replacement);
						}
					}
				}
			}
			else { // L'element de l'esquerra coincideix amb el que haviem de trobar, ara hem de mirar quin dels dos casos es
				
				if(current.isLeaf()) { // Cas nivell profund -> haurem d'ajustar els nodes d'abaix pel balanceig
					
					// Desplacem l'element de la dreta a l'esquerra, ja que el de l'esquerra es queda lliure
					if(current.getRightElement() != null) { 
						
						current.setLeftElement(current.getRightElement());
						
						current.setRightElement(null);
					}
					else { // No hi ha element a la dreta i per tant, al el·liminar el de l'esquerra hi haura desbalanceig...
						
						current.setLeftElement(null); // El·linem el node
						
						// Avisem a la crida en pujada de que s'ha el·liminat un node de l'ultim nivell, ja que
						// es aquella crida l'encarregada de gestionar la situacio
						// (!!!) nota : despres aquella crida haura de retornar CASE_DO_NOTHING
						
						return true;
					}
				}
				else { // Cas horrible -> esta pel mig
					
					// (!!!)
					
					// Recol·loquem el maxim per l'esquerra on anavem a el·liminar
					
					T replacement = current.getLeftSon().replaceMax();
					
					current.setLeftElement(replacement);
				}
			}
		}
			
		//TODO check this line --> added current != null
		if(current != null && !current.isBalanced()) {
						
			current.rebalance();  // Hem de rebalancejar el nivell d'abaix
		}
		else if(current != null && !current.isLeaf()) {
			
			boolean balanced = false;
			
			while(!balanced) {
				
				if(current.getRightSon() == null) {
					
					// Hi ha un desbalanceig CRITIC en el fill inferior esquerra
					if(current.getLeftSon().isLeaf() && !current.getMidSon().isLeaf()) {

						T replacement = current.getMidSon().replaceMin();
						
						//System.out.println("---------------- REPLACEMENT of " + replacement.toString() + "----------------");
	
						T readdition = current.getLeftElement();
						
						//System.out.println("---------------- READDITION of " + readdition.toString() + "----------------");
	
						current.setLeftElement(replacement);
						
						add(readdition);
						
						// Hi ha un desbalanceig CRITIC en el fill inferior dret
					} else if(!current.getLeftSon().isLeaf() && current.getMidSon().isLeaf()) {

						
						if(current.getRightElement() != null) {
							
							
						}
						else {

							T replacement = current.getLeftSon().replaceMax();
							
							//System.out.println("---------------- REPLACEMENT of " + replacement.toString() + "----------------");
	
							T readdition = current.getLeftElement();
							
							//System.out.println("---------------- READDITION of " + readdition.toString() + "----------------");
	
							current.setLeftElement(replacement);
							
							add(readdition); //(!!!!!!!!)
						}
						
					}
				}
				if(current.getRightSon() != null) { // No podem possar else perque la situacio pot haver canviat en el if d'abans
									
					if(current.getMidSon().isLeaf() && !current.getRightSon().isLeaf()) {

						current.getRightSon().rebalance();
					}
					if(current.getMidSon().isLeaf() && !current.getRightSon().isLeaf()) {

						T replacement = current.getRightSon().replaceMin();
						
						//System.out.println("---------------- REPLACEMENT of " + replacement.toString() + "----------------");
	
						T readdition = current.getRightElement();
						
						//System.out.println("---------------- READDITION of " + readdition.toString() + "----------------");
	
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
     * @param element The element to find
     *
     * @return true if this tree contains the specified element, false if not
     */
    public boolean contains(T element) {

        return find(element) != null;
    }

    /**
     * Search an element inside of the tree.
     *
     * @param element The element to find
     *
     * @return the element found or null if it doesn't exist
     */
	public T find(T element) {
		
		return findI(root, element);
	}
	
	private T findI(Node current, T element) {
	
		T found = null;
		
		// Seguim buscant mentre no arribem a cas trivial de que hem recorregut tot l'arbre
		if(current != null) {
 		
			// Cas trivial, trobem l'element
			if(current.getLeftElement() != null && current.getLeftElement().equals(element)) found = current.getLeftElement();
			
			else {
				
				// Altre cas trivial, trobem que es igual a l'element de la dreta
				if(current.getRightElement() != null && current.getRightElement().equals(element)) found = current.getRightElement();
				
				else {


                    if(current.getLeftElement() != null && current.getLeftElement().compareTo(element) == ROOT_IS_BIGGER) {

                        found = findI(current.left, element);
                    }
                    else if(current.getRightSon() == null || current.getRightElement().compareTo(element) == ROOT_IS_BIGGER) {

                        found = findI(current.mid, element);
                    }
                    else found = findI(current.right, element);

				}
			}
		}
		
		return found;
	}

	/**
	 * Finds an element inside the tree and modifies it.
     *
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
     *
     * @deprecated This function has been replaced by the find method, which is faster
     *
     * If the element is found, this is deleted adding it again with the modification done. It is a good way
     * because the element modification could affect the data structure organization (the attribute/field used for the indexation).
     *
     *
     * @param current The current child where we are
     * @param element The element to modify
     *
     * @return If the element has been found or not
     */
    @Deprecated
	private boolean modifyI(Node current, T element) {
		
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
					
					modified = modifyI(current.left, element);
					
					if(!modified) modified = modifyI(current.mid, element);
					
					if(current.getRightElement() != null && !modified) modified = modifyI(current.right, element);
					
				}
			}
		}
		
		return modified;
	}
	
	public void preOrder() {
		
		if(!isEmpty()) {
			
			preOrderI(root);

		}
		else System.out.println("The tree is empty");
	}
	
	private void preOrderI(Node current) {

		if(current != null) {
			
			if(current.isLeaf()) {
				
				System.out.println(current.getLeftElement().toString());
				if(current.getRightElement() != null) System.out.println(current.getRightElement().toString());
			}
			else {
				
				preOrderI(current.getLeftSon());
				System.out.println(current.getLeftElement().toString());
	
				preOrderI(current.getMidSon());
				
				if(current.getRightElement() != null) {
					
					if(!current.isLeaf()) System.out.println(current.getRightElement().toString());
					
					preOrderI(current.getRightSon());
					
				}
			}
		}
	}


    /**
     * The 2-3 tree is formed by nodes that stores the elements of the structure.
     *
     * Each node contains at most two elements and one at least. In case there is only one element
     * in a node, this is <b>always in the left</b>, so in this case the <b>right element is null</b>.
     *
     * The 2-3 Tree structure defines two type of Nodes/childs:
     *
     *  - 2 Node : This node only has two childs, <b>always left and mid</b>. The right element is empty (null) and the
     *             right node/child is also null.
     *
     *  - 3 Node : This node has the two elements, so it also has 3 childs: left, mid and right. It is full.
     */
	private class Node {
		
		private Node left;
		private Node mid;
		private Node right;
		private T leftElement;
		private T rightElement;


        /**
         * Creates an empty node/child
         */
        private Node() {
			
			left = null;
			mid = null;
			right = null;
			leftElement = null;
			rightElement = null;
		}

        /**
         * Constructor of a 3 Node without the childs defined yet (null references).
         *
         * @param leftElement   the element in the left
         * @param rightElement  the element in the right
         *
         */
        // Precondition: The left element must be less than the right element
        //               This is a private class but if you want to make it externally accessible
        //               I recommend to use the IllegalArgumentException
		private Node(T leftElement, T rightElement) {
			
			this.leftElement = leftElement;
			this.rightElement = rightElement;
			left = null;
			mid = null;
			right = null;
		}

        /**
         * Constructor of a 3 Node with the left and mid nodes/childs defined.
         *
         * @param leftElement   the element in the left
         * @param rightElement  the element in the right
         * @param left          the left child
         * @param mid           the mid child
         */
        // Precondition: The left element must be less than the right element
        //               This is a private class but if you want to make it externally accessible
        //               I recommend to use the IllegalArgumentException
		private Node(T leftElement, T rightElement, Node left, Node mid) {
			
			this.leftElement = leftElement;
			this.rightElement = rightElement;			
			this.left = left;
			this.mid = mid;
		}

        private T getLeftElement() {
			
			return leftElement;
		}
		
		private T getRightElement() {
			
			return rightElement;
		}
		
		private void setLeftElement(T element) {
			
			this.leftElement = element;
		}
		
		private void setRightElement(T element) {
			
			this.rightElement = element;
		}
		
		private void setLeftSon(Node son) {
			
			this.left = son;
		}
		
		private Node getLeftSon() {
			
			return left;
		}
		
		private void setMidSon(Node son) {
			
			this.mid = son;
		}
		
		private Node getMidSon() {
			
			return mid;
		}
		
		private void setRightSon(Node son) {
			
			this.right = son;
		}
		
		private Node getRightSon() {
			
			return right;
		}
		
		/**
		 * @return true if we are on the deepest level of the tree (a leaf) or false if not
		 */
		private boolean isLeaf() {

			return left == null && mid == null && right == null;
		}
		
		/**
         * Checks if the tree is well-balanced or not
         *
		 * @return true if the tree is well-balanced, false if not
		 */
		private boolean isBalanced() {
			
			boolean balanced = false;
			
			if(isLeaf()) { // If we are at the deepest level (the leaf), it is well-balanced for sure
				
				balanced = true;
			}
            // There are two cases: 2 Node or 3 Node
			else if(left.getLeftElement() != null && mid.getLeftElement() != null) {
				
				if(rightElement != null) { // 3 Node
					
					if(right.getLeftElement() != null) {
						
						balanced = true;
					}
				}
				else {  // 2 Node
					
					balanced = true;
				}
			}
			
			return balanced;
		}
		
	
		private T replaceMax() {
			
			T max = null;
			
			if(!isLeaf()) { // Cas recursiu, no hem arribat al nivell mes profund
				
				if(getRightElement() != null) {
					
					max = right.replaceMax(); // Si hi ha element a la dreta, seguim per la dreta
				}
				else max = mid.replaceMax();  // Sino, seguim pel mig
				
			}
			else {			// Cas trivial, hem arribat al nivell mes profund
				
				if(getRightElement() != null) {
					
					max = getRightElement();
					
					setRightElement(null);
					
					// Aqui tenim sort i no hem de balancejar res de res
				}
				else {
					
					max = getLeftElement();
					
					setLeftElement(null);
					
					// A la primera pujada es produira un rebalanceig
				}
			}
			
			// PUJADA DE LA RECURSIVA ---> Hem d'anar comprovant balanceig, no sabem que ha passat al extreure
			// La realitat es que nomes es comprovara balanceig en el nivell superior a la subcrida
			if(!isBalanced()) {
					
				rebalance(); // Rebalancegem i llestos
				
			}
			
			return max;
		}
		
		private T replaceMin() {
			
			T min = null;
			
			if(!isLeaf()) { // Cas recursiu, mentre no arribem al nivell mes profund anem baixant per l'esquerra sempre
				
				min = left.replaceMin();
				
			}
			else { // Cas trivial, agafem l'element i ho intentem deixar tot maco
				
				min = leftElement; 
				
				leftElement = null;
				
				if(rightElement != null) { // Hi havia element a la dreta, el passem a l'esquerra i aqui no ha passat res!
					
					leftElement = rightElement;
					
					rightElement = null;
					
				}
			}
			
			if(!isBalanced()) { // Aquesta situacio es dona quan a la dreta no hi havia element, en la 1a pujada rebalancejara
				
				rebalance();
			}
			
			return min;
		}
		

        //TODO: Translate this comment to English
		/**
		 * S'encarrega de balancejar l'ultim nivell de l'arbre des del penultim. 
		 * 
		 * Intenta que quedi un element minim en cada lloc, pero si no es pot vol dir que ens hem trobat en el cas critic
		 * i hem de rebalancejar des de mes a munt, rao per la qual destrueix un nivell i surt.
		 * 
		 */
		private void rebalance() {
			
			while(!isBalanced()) {
				
			
				//System.out.println("\nBalancejant des del node amb l'element: " + getLeftElement());
				
				if(getLeftSon().getLeftElement() == null) { // El desbalanceig es troba en el fill esquerra
					
					//System.out.println("\nBalanceig esquerra des de " + getLeftElement());
					
					// L'element esquerra del fill del mig el col·loquem com a element esquerra del fill esquerra
					getLeftSon().setLeftElement(getLeftElement());
					
					// Ara desplacem l'element de l'esquerra del fill mig a dalt a l'esquerra
					setLeftElement(getMidSon().getLeftElement());
					
					// Si en el fill del mig hi havia element a la dreta, el desplacem a l'esquerra
					if(getMidSon().getRightElement() != null) {
						
						getMidSon().setLeftElement(getMidSon().getRightElement());
						
						getMidSon().setRightElement(null);
					} 
					// Sino, deixem el fill del mig BUIT, pero comença la festa!!!
					
					else {
						
						getMidSon().setLeftElement(null);
					}
					
				}
				else if(getMidSon().getLeftElement() == null) { // El desbalanceig es troba en el fill del mig
					
					//System.out.println("Balanceig mig " + getLeftElement().toString());
					
					
					// SITUACIO MES CRITICA, cada node del nivell mes profund te
					// sol un element -> el sistema s'ha de remuntar des de mes a munt... 
					if(getRightElement() == null) {
						
						//System.out.println("Fill mig es null" + "fills esquerra: " + getLeftSon().getLeftElement());
						//if(getLeftSon().getRightElement() != null) System.out.println("Fill dret: " + getLeftSon().getRightElement().toString()
							//	);
						
						if(getLeftSon().getLeftElement() != null && getLeftSon().getRightElement() == null && getMidSon().getLeftElement() == null) {
							
							setRightElement(getLeftElement());
							
							setLeftElement(getLeftSon().getLeftElement());
							
							// Fem que comenci la festa
							setLeftSon(null); 
							setMidSon(null);
							setRightSon(null);
						}
						
						else {
							getMidSon().setLeftElement(getLeftElement());
													
							if(getLeftSon().getRightElement() == null) {
						
								setLeftElement(getLeftSon().getLeftElement());
								
								getLeftSon().setLeftElement(null);
	
							}
							else {
	
								setLeftElement(getLeftSon().getRightElement());
								
								getLeftSon().setRightElement(null);
							}
							
							if(getLeftSon().getLeftElement() == null && getMidSon().getLeftElement() == null) {
								
								// Fem que comenci la festa
								setLeftSon(null); 
								setMidSon(null);
								setRightSon(null);
							}
						}
					}
					else {
						
						// Desplacem l'element de la dreta del node en el que ESTEM com a element esquerra del fill del mig
						getMidSon().setLeftElement(getRightElement());
						
						// Desplacem l'element esquerra del fill dret com a element de la dreta del node ACTUAL
						setRightElement(getRightSon().getLeftElement());
						
						// Si del fill dret, d'on hem extret en la lina abans l'element, teniem element a la dreta, 
						// el desplacem a l'esquerra com a element esquerra del fill dret
						if(getRightSon().getRightElement() != null) {
							
							getRightSon().setLeftElement(getRightSon().getRightElement());
							
							getRightSon().setRightElement(null);
						}
						else { // Si no hi havia, aleshores ho deixem BUIT
							
							getRightSon().setLeftElement(null);
						}
					}
					
				// Desbalanceig en el fill de la dreta
				} else if(getRightElement() != null && getRightSon().getLeftElement() == null) {
					
					//System.out.println("Balanceig dreta");
					
					/* En el cas de la dreta es poden donar dues situacions:
					 *
					 * (1) El fill del mig esta ple, i per tant hem de fer un desplacament dels size
					 *     cap a la dreta
					 *     
					 * (2) El fill del mig nomes te l'element de l'esquerra, hem de fer un desplacament cap
					 *     a l'esquerra de l'element de la dreta del node ACTUAL
					 */
					
					if(getMidSon().getRightElement() != null) { // (1)
						
						getRightSon().setLeftElement(getRightElement());
						
						setRightElement(getMidSon().getRightElement());
						
						getMidSon().setRightElement(null);
					}
					else { // (2)
						
						getMidSon().setRightElement(getRightElement());
						
						setRightElement(null);
					}
				}
			}			
		}
	}
}
