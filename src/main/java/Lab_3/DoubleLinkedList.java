package Lab_3;

/**
 * Created by Aphex on 07.05.2016.
 */
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Random;

public class DoubleLinkedList<Item extends  Comparable<Item>> implements Iterable<Item> {
    private int N;        // number of elements on list
    private Node sent;     // sentinel node


    public DoubleLinkedList() {
        sent  = new Node();
        sent.next = sent;
        sent.prev = sent;
    }

    // linked list node helper data type
    private class Node {
        private Item item;
        private Node next;
        private Node prev;
    }

    public boolean isEmpty()    { return N == 0; }
    public int size()           { return N;      }

    // add the item to the list
    public void add(Item item) {
        Node last = sent.prev;
        Node x = new Node();
        x.item = item;
        x.next = sent;
        x.prev = last;
        sent.prev = x;
        last.next = x;
        N++;
    }

    public ListIterator<Item> iterator()  { return new DoubleLinkedListIterator(); }

    // assumes no calls to DoubleLinkedList.add() during iteration
    private class DoubleLinkedListIterator implements ListIterator<Item> {
        private Node current      = sent.next;  // the node that is returned by next()
        private Node lastAccessed = null;      // the last node to be returned by prev() or next()
        // reset to null upon intervening remove() or add()
        private int index = 0;

        public boolean hasNext()      { return index < N;}
        public boolean hasPrevious()  { return index > 0; }
        public int previousIndex()    { return (index > 0)?index - 1:N-1; }
        public int nextIndex()        { return index;     }

        public Item next() {
            if ((index==N)||(lastAccessed==current)){ index=0; current=sent.next; }
            Item item = current.item;
            lastAccessed = current;
            current = (index<N-1)? current.next:sent.next;
            index++;
            return item;
        }

        public Item previous() {
            if (index==N){current=sent;}
            if (hasPrevious()){
                current = current.prev;
                index--;
            }else {
                current = sent.prev;
                index = N-1;
            }
            lastAccessed = current;
            return current.item;
        }

        // replace the item of the element that was last accessed by next() or previous()
        // condition: no calls to remove() or add() after last call to next() or previous()
        public void set(Item item) {
            if (lastAccessed == null) throw new IllegalStateException();
            lastAccessed.item = item;
        }

        // remove the element that was last accessed by next() or previous()
        // condition: no calls to remove() or add() after last call to next() or previous()
        public void remove() {
            if (lastAccessed == null) throw new IllegalStateException();
            Node x = lastAccessed.prev;
            Node y = lastAccessed.next;
            x.next = y;
            y.prev = x;
            N--;
            if (current == lastAccessed)
                current = y;
            else
                index--;
            lastAccessed = null;
        }

        // add element to list 
        public void add(Item item) {
            Node first = current.prev;
            Node node = new Node();
            Node last = current;
            node.item = item;
            first.next = node;
            node.next = last;
            last.prev = node;
            node.prev = first;
            N++;
            index++;
            lastAccessed = null;
        }

    }

    //block of basic list functions


    //get by index
    public Item get(int index){
        if (!((index>=0)&&(index<N))) throw new NoSuchElementException();
        ListIterator <Item> listIterator= iterator();
        if (index<=(N/2)){
            while (listIterator.nextIndex()<index){
                listIterator.next();
            }
            return listIterator.next();
        }else{
            while (listIterator.previousIndex()>index){
                listIterator.previous();
            }
            return listIterator.previous();
        }
    }


    //remove by index
    public void remove(int index){
        if (!((index>=0)&&(index<N))) throw new NoSuchElementException();
        ListIterator <Item> listIterator= iterator();
        if (index<=(N/2)){
            while (listIterator.nextIndex()<=index){
                listIterator.next();
            }
        }else{
            while (listIterator.previousIndex()>=index){
                listIterator.previous();
            }
        }
        listIterator.remove();
    }


    //add by index
    public void add(int index, Item item){
        if (!((index>=0)&&(index<=N))) throw new NoSuchElementException();
        if (index==N){
            add(item);
        }else {
            ListIterator <Item> listIterator= iterator();
            if (index<=(N/2)){
                while (listIterator.nextIndex()<=index){
                    listIterator.next();
                }
            }else{
                while (listIterator.previousIndex()>=index){
                    listIterator.previous();
                }
            }
            listIterator.add(item);
        }
    }


    //toString implementation
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (Item item : this)
            s.append(item + " ");
        return s.toString();
    }

    public ArrayList<Item> toArray(){
        ArrayList<Item> array=new ArrayList<Item>();
        for (Item item : this)
            array.add(item);
        return array;
    }

    //quick sort implementation
    public  void sort() {
        ArrayList<Item> array = toArray();
        int startIndex = 0;
        int endIndex = array.size() - 1;
        doSort(startIndex, endIndex,array);
        ListIterator <Item> listIterator= iterator();
        for (Item item : array){
            listIterator.next();
            listIterator.set(item);
        }
    }

    private  void doSort(int start, int end, ArrayList<Item> array ) {
        if (start >= end)
            return;
        int i = start, j = end;
        int cur = i - (i - j) / 2;
        while (i < j) {
            while (i < cur && (array.get(i).compareTo(array.get(cur))<=0)) {
                i++;
            }
            while (j > cur && (array.get(cur).compareTo(array.get(j))<=0)) {
                j--;
            }
            if (i < j) {
                Item temp = array.get(i);
                array.set(i, array.get(j));
                array.set(j, temp);
                if (i == cur)
                    cur = j;
                else if (j == cur)
                    cur = i;
            }
        }
        doSort(start, cur, array);
        doSort(cur+1, end, array);
    }



    // a test client
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
    }
}
