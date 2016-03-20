import java.util.ArrayList;

/**
 * A test class with a main to try the 2-3 Tree.
 */
public class Test {


    public void test() {

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

    /* Checks the addAll and addAllSafe methods */
    public void test2() {

        Tree23<Integer> tree23 = new Tree23<>();

        ArrayList<Integer> array = new ArrayList<Integer>();

        array.add(5);
        array.add(10);
        array.add(2);
        array.add(5);

        tree23.addAll(array);

        tree23.preOrder();

        array.add(20);
        array.add(6);
        array.add(2);

        tree23.addAllSafe(array);

        tree23.preOrder();

    }

    public static void main(String[] args) {

        Test t = new Test();

        System.out.println("Here");
        t.test2();

    }


}
