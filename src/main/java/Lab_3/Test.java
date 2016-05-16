package Lab_3;

import java.util.ListIterator;
import java.util.Random;

/**
 * Created by Aphex on 16.05.2016.
 */
public class Test {// a test client
    public static void main(String[] args) {
        int N  = 10;

        // add elements 1, ..., N
        System.out.println(N + " random integers between 0 and 99");
        DoubleLinkedList<Integer> list = new DoubleLinkedList<Integer>();
        Random generator = new Random();
        for (int i = 0; i < N; i++)
            list.add(generator.nextInt(100));
        System.out.println(list);
        System.out.println();

        ListIterator<Integer> iterator = list.iterator();

        // go forwards with next() and set()
        System.out.println("add 1 to each element via next() and set()");
        while (iterator.hasNext()) {
            int x = iterator.next();
            iterator.set(x + 1);
        }
        System.out.println(list);
        System.out.println();

        // go backwards with previous() and set()
        System.out.println("multiply each element by 3 via previous() and set()");
        while (iterator.hasPrevious()) {
            int x = iterator.previous();
            iterator.set(x + x + x);
        }
        System.out.println(list);
        System.out.println();


        // remove all elements that are multiples of 4 via next() and remove()
        System.out.println("remove elements that are a multiple of 4 via next() and remove()");
        while (iterator.hasNext()) {
            int x = iterator.next();
            if (x % 4 == 0) iterator.remove();
        }
        System.out.println(list);
        System.out.println();


        // remove all even elements via previous() and remove()
        System.out.println("remove elements that are even via previous() and remove()");
        while (iterator.hasPrevious()) {
            int x = iterator.previous();
            if (x % 2 == 0) iterator.remove();
        }
        System.out.println(list);
        System.out.println();


        // add elements via next() and add()
        System.out.println("add elements via next() and add()");
        while (iterator.hasNext()) {
            int x = iterator.next();
            iterator.add(x + 1);
        }
        System.out.println(list);
        System.out.println();

        // add elements via previous() and add()
        System.out.println("add elements via previous() and add()");
        while (iterator.hasPrevious()) {
            int x = iterator.previous();
            iterator.add(x * 10);
            iterator.previous();
        }
        System.out.println(list);
        System.out.println();
        System.out.println("previous() on first node returns last");
        System.out.println(iterator.previous());
        System.out.println();
        System.out.println("next() on last node returns first");
        System.out.println(iterator.next());
        System.out.println();
        System.out.println("print size");
        System.out.println(list.size());
        System.out.println();
        System.out.println("print element[size/2]");
        System.out.println(list.get(list.size()/2));
        System.out.println();
        System.out.println("print element[size/2+1]");
        System.out.println(list.get(list.size()/2+1));
        System.out.println();
        System.out.println("remove element[size/2]");
        list.remove(list.size() / 2);
        System.out.println(list);
        System.out.println();
        System.out.println("add element to position [size/2]");
        list.add(list.size() / 2, list.size());
        System.out.println(list);
        System.out.println();
        System.out.println("remove element[size/2+1]");
        list.remove(list.size() / 2+1);
        System.out.println(list);
        System.out.println();
        System.out.println("add element to position [size/2+1]");
        list.add(list.size() / 2+1,list.size()*100);
        System.out.println(list);
        System.out.println();
        System.out.println("sort implements quick sort (also toArray() method)");
        list.sort();
        System.out.println(list);
        System.out.println();
        System.out.println("map implementation, returns some hash codes");
        DoubleLinkedList<String> newlist = list.map(new TestFunction());
        System.out.println(newlist);
        System.out.println();
        System.out.println("test for concurency");
        try {
            for (int x: list){
                list.add(0);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
