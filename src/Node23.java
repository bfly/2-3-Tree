/**
 * The 2-3 tree is formed by nodes that stores the elements of the structure.
 * <p>
 * Each node contains at most two elements and one at least. In case there is only one element
 * in a node, this is <b>always in the left</b>, so in this case the <b>right element is null</b>.
 * <p>
 * The 2-3 Tree structure defines two type of Nodes/children:
 * <p>
 * - 2 Node : This node only has two children, <b>always left and mid</b>. The right element is empty (null) and the
 * right node/child is also null.
 * <p>
 * - 3 Node : This node has the two elements, so it also has 3 children: left, mid and right. It is full.
 */
public class Node23<T> {

    private T leftElement;
    private T rightElement;
    private Node23<T> left;
    private Node23<T> mid;
    private Node23<T> right;

    /**
     * Creates an empty node/child
     */
    public Node23() {
        left = null;
        mid = null;
        right = null;
        leftElement = null;
        rightElement = null;
    }

    /**
     * Constructor of a 3 Node without the children defined yet (null references).
     *
     * @param leftElement  the element in the left
     * @param rightElement the element in the right
     */
// Precondition: The left element must be less than the right element
//               This is a private class but if you want to make it externally accessible
//               I recommend to use the IllegalArgumentException
    public Node23( T leftElement, T rightElement ) {
        this.leftElement = leftElement;
        this.rightElement = rightElement;
        left = null;
        mid = null;
        right = null;
    }

    /**
     * Constructor of a 3 Node with the left and mid nodes/children defined.
     *
     * @param leftElement  the element in the left
     * @param rightElement the element in the right
     * @param left         the left child
     * @param mid          the mid child
     */
// Precondition: The left element must be less than the right element
//               This is a private class but if you want to make it externally accessible
//               I recommend to use the IllegalArgumentException
    public Node23( T leftElement, T rightElement, Node23<T> left, Node23<T> mid ) {
        this.leftElement = leftElement;
        this.rightElement = rightElement;
        this.left = left;
        this.mid = mid;
    }

    public T getLeftElement() {
        return leftElement;
    }

    public void setLeftElement( T leftElement ) {
        this.leftElement = leftElement;
    }

    public T getRightElement() {
        return rightElement;
    }

    public void setRightElement( T rightElement ) {
        this.rightElement = rightElement;
    }

    public Node23<T> getLeft() {
        return left;
    }

    public void setLeft( Node23<T> left ) {
        this.left = left;
    }

    public Node23<T> getMid() {
        return mid;
    }

    public void setMid( Node23<T> mid ) {
        this.mid = mid;
    }

    public Node23<T> getRight() {
        return right;
    }

    public void setRight( Node23<T> right ) {
        this.right = right;
    }

    /**
     * @return true if we are on the deepest level of the tree (a leaf) or false if not
     */
    public boolean isLeaf() {
        return left == null && mid == null && right == null;
    }

    public boolean is2Node() {
        return rightElement == null; // also, right node is null but this will be always true if rightElement == null
    }

    public boolean is3Node() {

        return rightElement != null; // also, right node is not null but this will be always true if rightElement <> null
    }

    /**
     * Checks if the tree is well-balanced or not
     *
     * @return true if the tree is well-balanced, false if not
     */
    public boolean isBalanced() {
        boolean balanced = false;

        if ( isLeaf() ) { // If we are at the deepest level (the leaf), it is well-balanced for sure

            balanced = true;
        }
        // There are two cases: 2 Node or 3 Node
        else if ( left.getLeftElement() != null && mid.getLeftElement() != null ) {

            if ( rightElement != null ) { // 3 Node

                if ( right.getLeftElement() != null ) {

                    balanced = true;
                }
            } else {  // 2 Node

                balanced = true;
            }
        }

        return balanced;
    }


    public T replaceMax() {

        T max;

        if ( !isLeaf() ) { // Recursive case, we are not on the deepest level

            if ( getRightElement() != null )
                 max = right.replaceMax(); // If there is an element on the right, we continue on the right
            else max = mid.replaceMax();  // else, we continue on the mid

        } else {    // Trivial case, we are on the deepest level of the tree

            if ( getRightElement() != null ) {

                max = getRightElement();

                setRightElement(null);
                // We are luck, we don't need to rebalance anything
            } else {

                max = getLeftElement();

                setLeftElement(null);

                // On the first up of the recursive function, there will be a rebalance
            }
        }

        if ( !isBalanced() ) rebalance(); // Keep calm and rebalance

        return max;
    }

    public T replaceMin() {

        T min;

        if ( !isLeaf() ) { // Recursive case, as long as we do not reach the deepest level we always go down to the left

            min = left.replaceMin();

        } else { // Trivial case, we take the item and try to leave it all pretty

            min = leftElement;

            leftElement = null;

            if ( rightElement != null ) { // There was element on the right, we passed it on the left and nothing happened here!

                leftElement = rightElement;

                rightElement = null;

            }
        }

        if ( !isBalanced() ) { // This situation occurs when there was no element on the right, in the 1st rebalancing rise

            rebalance();
        }

        return min;
    }


    /**
     * Rebalances the deepest level of the tree from the second deepest.
     * <p>
     * The algorithm tries to put one element in each child, but there is a critical case where we must balance the
     * tree from a higher level removing the current level.
     */
    public void rebalance() {

        while (!isBalanced()) {

            if ( getLeft().getLeftElement() == null ) { // The unbalance is in the left child

                // We put the left element of current node as the left element of the left child
                getLeft().setLeftElement(getLeftElement());

                // Now we replace the left element of the mid child as the left element of the current node
                setLeftElement(getMid().getLeftElement());

                // If a right element on the mid child exists, we shift it to the left
                if ( getMid().getRightElement() != null ) {

                    getMid().setLeftElement(getMid().getRightElement());

                    getMid().setRightElement(null);
                }

                // Else, we let the mid child EMPTY, so the next iteration may solve this situation
                // if not, the party of the critical case starts here!
                else {

                    getMid().setLeftElement(null);
                }

            } else if ( getMid().getLeftElement() == null ) { // The unbalance is in the right child

                // CRITICAL CASE, each node (child) of the deepest level have just one element (the right is empty)
                // the algorithm will have to rebalance from a higher level of the tree
                if ( getRightElement() == null ) {

                    if ( getLeft().getLeftElement() != null && getLeft().getRightElement() == null && getMid().getLeftElement() == null ) {

                        setRightElement(getLeftElement());

                        setLeftElement(getLeft().getLeftElement());

                        // Let the party starts, we remove the current child
                        setLeft(null);
                        setMid(null);
                        setRight(null);
                    } else {
                        getMid().setLeftElement(getLeftElement());

                        if ( getLeft().getRightElement() == null ) {

                            setLeftElement(getLeft().getLeftElement());

                            getLeft().setLeftElement(null);

                        } else {

                            setLeftElement(getLeft().getRightElement());

                            getLeft().setRightElement(null);
                        }

                        if ( getLeft().getLeftElement() == null && getMid().getLeftElement() == null ) {

                            // The other but same case the party starts
                            setLeft(null);
                            setMid(null);
                            setRight(null);
                        }
                    }
                } else {

                    // We put the right element of the current node as the left element of the mid son
                    getMid().setLeftElement(getRightElement());

                    // We put the left element of the right child as the right element of the current node
                    setRightElement(getRight().getLeftElement());

                    // If the right child, where we have taken the last element has a right element, we move it
                    // into the left of the same child
                    if ( getRight().getRightElement() != null ) {

                        getRight().setLeftElement(getRight().getRightElement());

                        getRight().setRightElement(null);
                    } else { // Else, we let the right child EMPTY

                        getRight().setLeftElement(null);
                    }
                }

                // Unbalance in the right child
            } else if ( getRightElement() != null && getRight().getLeftElement() == null ) {


                /*
                 * In this case we can have two situations:
                 *
                 * (1) The mid child is full, so we have to do a shift of the elements to the right
                 *
                 * (2) The mid child only has the left element, then we have to put the right element
                 * 	   of the current node as the right element of the mid child
                 */
                if ( getMid().getRightElement() != null ) { // (1)

                    getRight().setLeftElement(getRightElement());

                    setRightElement(getMid().getRightElement());

                    getMid().setRightElement(null);
                } else { // (2)

                    getMid().setRightElement(getRightElement());

                    setRightElement(null);
                }
            }
        }
    }
}
