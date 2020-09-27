import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Tree23Test {

    @Test
    void testIntegerTree() {
        Tree23<Integer> tree = new Tree23<>();

        tree.addAll(List.of(10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 110, 120, 130));

        System.out.println("Tree size: " + tree.size());
        System.out.print("InOrder: ");   tree.inOrder();   System.out.println();
        System.out.print("PreOrder: ");  tree.preOrder();  System.out.println();
        System.out.println();

        tree.levelOrder();
   }

    @Test
    void testOneAtTim() {
        Tree23<Integer> tree = new Tree23<>();
        tree.add(10);
        assertEquals(1, tree.size());
        tree.levelOrder();
        System.out.println("------");
        tree.add(20);
        assertEquals(2, tree.size());
        tree.levelOrder();
        System.out.println("------");
        tree.add(30);
        assertEquals(3, tree.size());
        tree.levelOrder();
        System.out.println("------");
        tree.add(40);
        assertEquals(4, tree.size());
        System.out.println("Tree size: " + tree.size());
        System.out.print("InOrder: ");   tree.inOrder();   System.out.println();
        tree.levelOrder();
    }

    @Test
    void testStringTree() {
        Tree23<String> tree = new Tree23<>();

        tree.addAll(List.of("A","B","C","D","E","F","G","H","I","J","K","L","M"));

        System.out.println("Tree size: " + tree.size());
        System.out.print("InOrder: ");   tree.inOrder();   System.out.println();
        System.out.print("PreOrder: ");  tree.preOrder();  System.out.println();
        System.out.println();

        tree.levelOrder();
    }

//    @Test
//    void testCardTree() {
//        Tree23<Card> tree = new Tree23<>();
//
//        tree.addAll(new Card[]{
//            new Card(0, 0),     // two of clubs
//            new Card(1, 1),     // three of diamonds
//            new Card(2, 2),     // four of hearts
//            new Card(3, 3),     // five of spades
//            new Card(9, 0),     // jack of clubs
//            new Card(10, 1),    // queen of diamonds
//            new Card(11, 2),    // king of hearts
//            new Card(12, 3)     // ace of spades
//        });
//
//        System.out.println("Tree size: " + tree.size());
//        System.out.print("InOrder: ");   tree.inOrder();   System.out.println();
//        System.out.print("PreOrder: ");  tree.preOrder();  System.out.println();
//        System.out.print("PostOrder: "); tree.postOrder(); System.out.println();
//        System.out.println();
//
//        tree.levelOrder();
//    }
}