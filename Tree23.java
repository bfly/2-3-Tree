package Model;

import java.util.Scanner;

public class Tree23<T extends Comparable<T>> {

	private Node root; // L'arrel esdeve un node indefinit i invisible, del que baixa el primer nivell del 2-3 (dos nodes)
	
	private long level; // Nivell de profunditat de l'arbre
	private long elements; // Nombre d'elements dins de l'arbre
	
	private static final int ROOT_IS_BIGGER = 1;
	private static final int ROOT_IS_SMALLER = -1;
	
	private static final int CASE_DEEPEST_LEVEL_DELETION = 10;
	private static final int CASE_INTERNAL_LEVEL_DELETION = 20;
	private static final int CASE_DO_NOTHING = 0;				// Es ximple, pero va be per clarificar l'algorisme
	
	private boolean addition; // Un flag que hem de tenir com si fos una variable global
	
	
	public Tree23() {
		
		this.root = new Node();
		
		level = 0;
		
		elements = 0;
	}
	
	public Node getRoot() {
		
		return root;
	}
	
	// Retorna el nombre d'elements que hi ha en l'arbre
	public long size() {
		
		return elements;
	}
	
	public long getLevel() {
		
		return level;
	}
	
	/**
	 * @return Si no hi ha cap element en l'arbre.
	 */
	public boolean isEmpty() {
		
		if(root == null) return true;
		
		if(root.getLeftElement() == null) return true;
		
		return false;
	}
	
	
	/**
	 * Permet afegir un element dins de l'arbre i de forma balancejada.
	 * 
	 * @param element L'element a afegir
	 * 
	 * @return Si s'ha pogut afegir (true) o no (false)
	 */
	public boolean add(T element) {
		
		elements++;
		
		addition = false;
		
		if(root == null || root.getLeftElement() == null) { // Primer cas
			
			if(root == null) root = new Node();
			
			root.setLeftElement(element);
			
			addition = true;
		}
		else {
			
			Node newRoot = addElementI(root, element); // Immersio
		
			if(newRoot != null) {
				
				root = newRoot;
				
				level++;
			}
		}
		
		return addition;
	}
	
	/**
	 * Metode recursiu que s'encarrega d'afegir un element en l'arbre 2-3 de forma balancejada.
	 * 
	 * L'element es posiciona per l'arbre en funcio del valor que tingui i l'ordre depen de com es programi la funcio
	 * compareTo, ja que l'element pot ser de qualsevol tipus (generic).
	 * 
	 * En la baixada de la recursiva, es busca arribar al nivell mes profund de l'arbre i, un cop arribat, s'insereix el nou element.
	 * 
	 * En la pujada de la recursiva, si es dona la situacio de que l'element inserit no hi cap en el lloc on ha anat a parar perque ja 
	 * hi ha dos elements, aleshores vol dir que s'incrementa un nivell l'arbre, fent pujar el node que tingui el valor mig dels 3 (sigui un valor mes petit que un element i mes gran que l'altre).
	 * A nivell codi, aquesta situacio es dona quan es retorna un node, que vol dir que s'ha reconfigurat el nivell mes profund amb els 3 elements
	 * ben col·locats. Si en ves d'aixo es retorna null, vol dir que no hem de reconfigurar res, ja que s'ha pogut afegir l'element 
	 * sense cap problema (nomes hi havia un element en aquell node).
	 * 
	 * Es tambe en la pujada on es comprova que no s'hagi donat un desbalanceig de l'arbre.
	 * 
	 * @param current El subnode actual sobre el que ens trobem
	 * @param element L'element a insertar
	 * 
	 * @return Si s'ha donat un increment de nivell (node != null) o be no hem de fer res (node == null)
	 */
	private Node addElementI(Node current, T element) {
		
		Node newParent = null;
		Node sonAscended = null;
				
		// PART DE DESPLAÇAMENT DINS DE L'ARBRE ---> Anirem baixant fins que trobem el nivell mes profund
		if(!current.deepestLevel()) { // Mentre no arribem a l'ultim nivell, anem baixant
			
			//System.out.println("--- Still not in the deepest level! ----\n");
			
			if(current.getLeftElement().compareTo(element) == 0 || (current.getRightElement() != null && current.getRightElement().compareTo(element) == 0)) {
				
				// AlreadyExists
			}	
			else if(current.getLeftElement().compareTo(element) == ROOT_IS_BIGGER) {
				
				//System.out.println("Current element " + element.toString() + " is smaller than current left element " + current.getLeftElement().toString());
				
				sonAscended = addElementI(current.left, element);
				
				if(sonAscended != null) { // Un fill puja per la branca esquerra
					
					if(current.getRightElement() == null) {
						
						// L'element que puja en aquest cas sempre sera mes petit que current.left
												
						current.setRightElement(current.getLeftElement());
							
						current.setLeftElement(sonAscended.getLeftElement());
							
						current.setRightSon(current.getMidSon());
							
						current.setMidSon(sonAscended.getMidSon());
							
						current.setLeftSon(sonAscended.getLeftSon());
					}
					else { // Liada parda, hi haura un nou split... En aquest cas puja l'element existent a l'esquerra
						
						// Copiem la part dreta del subarbre
						Node rightCopy = new Node(current.getRightElement(), null, current.getMidSon(), current.getRightSon());
						
						// Creem la nova estructura, que enganxarem on toqui
						newParent = new Node(current.getLeftElement(), null, sonAscended, rightCopy);
						
					}
				}
				
				
			// Cas: element mes gran que el de l'esquerra i mes petit que el de la dreta
			} else if(current.getRightElement() == null || (current.getRightElement() != null && current.getRightElement().compareTo(element) == ROOT_IS_BIGGER)) {
				
				
			//	System.out.print("Current element " + element.toString() + " is bigger than current left element " + current.getLeftElement().toString()
				//		+ " and smaller than current right element ");
				
				//if(current.getRightElement() == null) System.out.println("[Right's null]");
				//else System.out.println(current.getRightElement().toString());

				sonAscended = addElementI(current.mid, element);
				
				if(sonAscended != null) { // Comensa la festa del split pujant pel mig
					
					if(current.getRightElement() == null) { // Hi ha lloc a la dreta, ho enganxem amb facilitat
						
						//System.out.println(sonAscended.getLeftSon().getLeftElement().toString());
						
						current.setRightElement(sonAscended.getLeftElement());
						
						current.setRightSon(sonAscended.getMidSon());
						
						current.setMidSon(sonAscended.getLeftSon());
						
					}
					else { // Hem de tornar a fer split... Ull
						
						// Dividim els fills del node que puja, fent que el fill esquerra del que puja passi a ser
						// fill del mig del current.left i el fill del mig del que puja passa a ser fill de l'esquerra
						// del current.right
						Node left = new Node(current.getLeftElement(), null, current.getLeftSon(), sonAscended.getLeftSon());
						
						Node mid = new Node(current.getRightElement(), null, sonAscended.getMidSon(), current.getRightSon());
						
						newParent = new Node(sonAscended.getLeftElement(), null, left, mid);
						
					}
				}
				
			} else if(current.getRightElement() != null && current.getRightElement().compareTo(element) == ROOT_IS_SMALLER) {
				
				//System.out.println("Current element " + element.toString() + " is bigger than the current right element " 
				//		+ current.getRightElement().toString());
				
				sonAscended = addElementI(current.right, element);
				
				
				if(sonAscended != null) { // Split, cas: puja per la dreta
					
					// En aquest cas no hi ha res null, i el que puja es el current.right
					
					Node leftCopy = new Node(current.getLeftElement(), null, current.getLeftSon(), current.getMidSon());
					
					//Node right = new Node(sonAscended.getLeftElement(), null, sonAscended.getLeftSon(), sonAscended.getMidSon());
					
					newParent = new Node(current.getRightElement(), null, leftCopy, sonAscended);
				}
			}
		}
		else { // Hem arribat al nivell mes baix i, per tant, hem d'inserir
			
			//System.out.println("\n---- We're in the deepest level of " + element.toString() + " insertion! ----\n");

			addition = true;
			
			if(current.getLeftElement().compareTo(element) == 0 || (current.getRightElement() != null && current.getRightElement().compareTo(element) == 0)) {
								
				addition = false;
				// AlreadyExists
			}
			else if(current.getRightElement() == null) { // Cas facil, no hi ha element a la dreta
				
				// L'element de l'esquerra es mes gran que el nou element -> desplacem l'element
				// existent a la dreta i inserim a l'esquerra el nou
				if(current.getLeftElement().compareTo(element) == ROOT_IS_BIGGER) {
					
					current.setRightElement(current.getLeftElement());
					
					current.setLeftElement(element);
				
				}
				else if(current.getLeftElement().compareTo(element) == ROOT_IS_SMALLER) { // Si el nou element es mes gran, l'inserim a la dreta i ja esta
					
					current.setRightElement(element);
					
				}
			}
			else { // Cas maco per dir-ho d'alguna manera: tres elements en el mateix node, hem de fer split
				
				// L'element de l'esquerra es mes gran, i per tant, pujara a un nivell superior,
				// deixant el nou element a l'esquerra
				if(current.getLeftElement().compareTo(element) == ROOT_IS_BIGGER) {
					
					Node left = new Node(element, null);
					
					Node right = new Node(current.getRightElement(), null);
					
					newParent = new Node(current.getLeftElement(), null, left, right);
					
				}
				else if(current.getLeftElement().compareTo(element) == ROOT_IS_SMALLER) {
					
					// El nou element es mes gran que el de l'esquerra i mes petit que el de la dreta
					if(current.getRightElement().compareTo(element) == ROOT_IS_BIGGER) {
						
						//System.out.println("Mid insertion ----> element: " + element.toString() + " current left: " + current.getLeftElement().toString() + " current right: " + current.getRightElement().toString());
						Node left = new Node(current.getLeftElement(), null);
						
						Node right = new Node(current.getRightElement(), null);
						
						newParent = new Node(element, null, left, right);
					}
					else { // El nou element es mes gran que el de l'esquerra i el de la dreta
						
						//System.out.println("Right insertion ----> element: " + element.toString() + " current left: " + current.getLeftElement().toString() + " current right: " + current.getRightElement().toString());

						Node left = new Node(current.getLeftElement(), null);
						
						Node right = new Node(element, null);
						
						newParent = new Node(current.getRightElement(), null, left, right);
					}
				}
				
			}
			
			
		}
		
		if(newParent != null) {
			// debug
			//System.out.println("Returning newParent --> " + newParent.getLeftElement() + " from " + element + " adding call");
		}
		
		return newParent;
	}
	
	/**
	 * Funcio de debug de l'arbre, va be per anar mapejant.
	 */
	public void debug_tree() {
		
		System.out.println("\nIniciant debug!\n-----------------");
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
	 * Metode per el·liminar un element que es trobi en l'arbre.
	 * 
	 * @param element L'element a el·liminar.
	 * 
	 * @return Si s'ha pogut el·liminar (true) o no (false)
	 */
	public boolean remove(T element) {
		boolean deleted;
		
		this.elements--; // Disminueixo de principi el nombre d'elements
		
		deleted = removeI(root, element); // Immersio
		
		root.rebalance();
		
		if(root.getLeftElement() == null) root = null; // Hem el·liminat l'ultim element
		
		return deleted;
		
	}
	
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
	 *  	- Si en el nivell profund no podem rebalancejar perque un node s'ha quedat sense elements i no tenim suficients elements en el ultim
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
			this.elements++; // Com ho havia decrementat d'inici, torno a incrementar
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
						if(current.deepestLevel()) {
							
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
				
				if(current.deepestLevel()) { // Cas nivell profund -> haurem d'ajustar els nodes d'abaix pel balanceig
					
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
		else if(current != null && !current.deepestLevel()) {
			
			boolean balanced = false;
			
			while(!balanced) {
				
				if(current.getRightSon() == null) {
					
					// Hi ha un desbalanceig CRITIC en el fill inferior esquerra
					if(current.getLeftSon().deepestLevel() && !current.getMidSon().deepestLevel()) {
						
						this.level--;
						
						T replacement = current.getMidSon().replaceMin();
						
						//System.out.println("---------------- REPLACEMENT of " + replacement.toString() + "----------------");
	
						T readdition = current.getLeftElement();
						
						//System.out.println("---------------- READDITION of " + readdition.toString() + "----------------");
	
						current.setLeftElement(replacement);
						
						add(readdition);
						
						// Hi ha un desbalanceig CRITIC en el fill inferior dret
					} else if(!current.getLeftSon().deepestLevel() && current.getMidSon().deepestLevel()) {

						
						if(current.getRightElement() != null) {
							
							
						}
						else {
							
							this.level--;
							
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
									
					if(current.getMidSon().deepestLevel() && !current.getRightSon().deepestLevel()) {

						current.getRightSon().rebalance();
					}
					if(current.getMidSon().deepestLevel() && !current.getRightSon().deepestLevel()) {
						
						this.level--;
						
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
					
					found = findI(current.left, element);
					
					// Mentre seguim sense trobar-lo, haurem de fer mes crides, pero si ja l'hem trobat llavors no fa falta
					if(found == null) found = findI(current.mid, element);
					
					if(current.getRightSon() != null && found == null) found = findI(current.right, element);
				}
			}
		}
		
		return found;
	}
	
	/**
	 * Metode que cerca un element en l'arbre per modificar-lo. 
	 * 
	 * Si el troba, el·limina l'element actual de l'arbre i seguidament 
	 * el torna a afegir ja que la modificacio pot haver afectat a 
	 * l'estructura que hi havia actualment.
	 * 
	 * @param which L'element que es vol modificar
	 * @param update L'actualitzacio de l'element
	 * 
	 * @return Si s'ha pogut modificar, es a dir, si s'ha trobat l'element en l'arbre (true) o no (false)
	 */
	public boolean modify(T which, T update) {
		boolean modified = false;
		
		if (modifyI(root, which)) {
			
			modified = true;
			
			add(update);
		}
		
		return modified;
		
	}
	
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
		else System.out.println("L'arbre està buit.");
	}
	
	private void preOrderI(Node current) {
		
		
		if(current != null) {
			
			if(current.deepestLevel()) {
				
				System.out.println(current.getLeftElement().toString());
				if(current.getRightElement() != null) System.out.println(current.getRightElement().toString());
			}
			else {
				
				preOrderI(current.getLeftSon());
				System.out.println(current.getLeftElement().toString());
	
				preOrderI(current.getMidSon());
				
				if(current.getRightElement() != null) {
					
					if(!current.deepestLevel()) System.out.println(current.getRightElement().toString());
					
					preOrderI(current.getRightSon());
					
				}
			}
			
			
		}
	}
	
	
	private class Node {
		
		protected Node left;
		protected Node mid;
		protected Node right;
		protected T leftElement;
		protected T rightElement;
		
		
		public Node() {
			
			left = null;
			mid = null;
			right = null;
			leftElement = null;
			rightElement = null;
		}
		
		public Node(T leftElement, T rightElement) {
			
			this.leftElement = leftElement;
			this.rightElement = rightElement;
			left = null;
			mid = null;
			right = null;
		}
		
		public Node(T leftElement, T rightElement, Node left, Node mid) {
			
			this.leftElement = leftElement;
			this.rightElement = rightElement;			
			this.left = left;
			this.mid = mid;
		}
		
		public T getLeftElement() {
			
			return leftElement;
		}
		
		public T getRightElement() {
			
			return rightElement;
		}
		
		public void setLeftElement(T element) {
			
			this.leftElement = element;
		}
		
		public void setRightElement(T element) {
			
			this.rightElement = element;
		}
		
		public void setLeftSon(Node son) {
			
			this.left = son;
		}
		
		public Node getLeftSon() {
			
			return left;
		}
		
		public void setMidSon(Node son) {
			
			this.mid = son;
		}
		
		public Node getMidSon() {
			
			return mid;
		}
		
		public void setRightSon(Node son) {
			
			this.right = son;
		}
		
		public Node getRightSon() {
			
			return right;
		}
		
		/**
		 * @return Si s'ha arribat a l'ultim nivell del arbre, es a dir, si no hi ha continuacio (true) o encara hi ha (false)
		 */
		public boolean deepestLevel() {

			return left == null && mid == null && right == null;
		}
		
		/**
		 * @return Si l'arbre o subarbre es troba balancejat, es a dir, hi ha tres fills
		 */
		public boolean isBalanced() {
			
			boolean balanced = false;
			
			if(deepestLevel()) { // Si estem en el nivell mes profund, esta balancejat segur
				
				balanced = true;
			}
			else if(left.getLeftElement() != null && mid.getLeftElement() != null) {
				
				if(rightElement != null) { 
					
					if(right.getLeftElement() != null) {
						
						balanced = true;
					}
				}
				else {
					
					balanced = true;
				}
				
			}
			
			return balanced;
		}
		
	
		protected T replaceMax() {
			
			T max = null;
			
			if(!deepestLevel()) { // Cas recursiu, no hem arribat al nivell mes profund
				
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
		
		protected T replaceMin() {
			
			T min = null;
			
			if(!deepestLevel()) { // Cas recursiu, mentre no arribem al nivell mes profund anem baixant per l'esquerra sempre
				
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
					 * (1) El fill del mig esta ple, i per tant hem de fer un desplacament dels elements 
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
	
	public static void main(String[] args) {
		
		Tree23<Integer> tree = new Tree23<Integer>();
		
		System.out.println("Tree created");
		
		tree.add(5);
		
		System.out.println("First element added");
		
		tree.add(10);
		tree.add(4);
		tree.add(8);
		tree.add(20);
		tree.add(30);
		tree.add(40);
		tree.add(50);
		tree.add(60);
		tree.add(2);
		tree.add(70);
		tree.add(200);
		tree.add(65);
		tree.add(1);
		tree.add(62);
		tree.add(6);
		
		int i = 0;
		while(i < 100) {
			
			tree.add((int) (Math.random()*200+0));
			i++;
		}
		
		tree.preOrder();
		
		//tree.debug_tree();
		
		int element = 62;
		
		i = 0;
		while(i < 20) {
			
			element = (int) (Math.random()*200+0);
			System.out.println("Removing: " + element + "\n");
			
			if(tree.remove(element)) System.out.println("Removed!\n");
			else System.out.println("Not found");
			
			i++;
		}
		
		
		
		//System.out.println("Removing: " + element + "\n");
		//tree.remove(element);
		
		//tree.debug_tree();
		
		tree.preOrder();
		
	}
}
