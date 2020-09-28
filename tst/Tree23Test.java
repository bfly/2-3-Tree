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

    @Test
    void testCardTree() {
        Tree23<Card> tree = new Tree23<>();

        tree.addAll(List.of(
            new Card(Card.Rank.Two,   Card.Suit.Clubs),
            new Card(Card.Rank.Three, Card.Suit.Clubs),
            new Card(Card.Rank.Four,  Card.Suit.Clubs),
            new Card(Card.Rank.Five,  Card.Suit.Clubs),
            new Card(Card.Rank.Six,   Card.Suit.Clubs),
            new Card(Card.Rank.Seven, Card.Suit.Clubs),
            new Card(Card.Rank.Eight, Card.Suit.Clubs),
            new Card(Card.Rank.Nine,  Card.Suit.Clubs),
            new Card(Card.Rank.Ten,   Card.Suit.Clubs),
            new Card(Card.Rank.Jack,  Card.Suit.Clubs),
            new Card(Card.Rank.Queen, Card.Suit.Clubs),
            new Card(Card.Rank.King,  Card.Suit.Clubs),
            new Card(Card.Rank.Ace,   Card.Suit.Clubs),
            new Card(Card.Rank.Two,   Card.Suit.Diamonds),
            new Card(Card.Rank.Three, Card.Suit.Diamonds),
            new Card(Card.Rank.Four,  Card.Suit.Diamonds),
            new Card(Card.Rank.Five,  Card.Suit.Diamonds),
            new Card(Card.Rank.Six,   Card.Suit.Diamonds),
            new Card(Card.Rank.Seven, Card.Suit.Diamonds),
            new Card(Card.Rank.Eight, Card.Suit.Diamonds),
            new Card(Card.Rank.Nine,  Card.Suit.Diamonds),
            new Card(Card.Rank.Ten,   Card.Suit.Diamonds),
            new Card(Card.Rank.Jack,  Card.Suit.Diamonds),
            new Card(Card.Rank.Queen, Card.Suit.Diamonds),
            new Card(Card.Rank.King,  Card.Suit.Diamonds),
            new Card(Card.Rank.Ace,   Card.Suit.Diamonds),
            new Card(Card.Rank.Two,   Card.Suit.Hearts),
            new Card(Card.Rank.Three, Card.Suit.Hearts),
            new Card(Card.Rank.Four,  Card.Suit.Hearts),
            new Card(Card.Rank.Five,  Card.Suit.Hearts),
            new Card(Card.Rank.Six,   Card.Suit.Hearts),
            new Card(Card.Rank.Seven, Card.Suit.Hearts),
            new Card(Card.Rank.Eight, Card.Suit.Hearts),
            new Card(Card.Rank.Nine,  Card.Suit.Hearts),
            new Card(Card.Rank.Ten,   Card.Suit.Hearts),
            new Card(Card.Rank.Jack,  Card.Suit.Hearts),
            new Card(Card.Rank.Queen, Card.Suit.Hearts),
            new Card(Card.Rank.King,  Card.Suit.Hearts),
            new Card(Card.Rank.Ace,   Card.Suit.Hearts),
            new Card(Card.Rank.Two,   Card.Suit.Spades),
            new Card(Card.Rank.Three, Card.Suit.Spades),
            new Card(Card.Rank.Four,  Card.Suit.Spades),
            new Card(Card.Rank.Five,  Card.Suit.Spades),
            new Card(Card.Rank.Six,   Card.Suit.Spades),
            new Card(Card.Rank.Seven, Card.Suit.Spades),
            new Card(Card.Rank.Eight, Card.Suit.Spades),
            new Card(Card.Rank.Nine,  Card.Suit.Spades),
            new Card(Card.Rank.Ten,   Card.Suit.Spades),
            new Card(Card.Rank.Jack,  Card.Suit.Spades),
            new Card(Card.Rank.Queen, Card.Suit.Spades),
            new Card(Card.Rank.King,  Card.Suit.Spades),
            new Card(Card.Rank.Ace,   Card.Suit.Spades)
        ));

        System.out.println("Tree size: " + tree.size());
        System.out.print("InOrder: ");   tree.inOrder();   System.out.println();
        System.out.print("PreOrder: ");  tree.preOrder();  System.out.println();
        System.out.print("Height: ");  System.out.println(tree.getLevel());
        System.out.println();
        tree.levelOrder();
    }
}