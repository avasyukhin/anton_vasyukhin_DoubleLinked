package Lab_3;

/**
 * Created by Aphex on 07.05.2016.
 */
import java.io.PrintWriter;
import java.util.*;

public class DoubleLinkedList<E extends  Comparable<E>> implements Iterable<E> {
    private int size;        // number of elements on list
    private Node sent;     // sentinel node
    private int modCount = 0; //modCount for comodification checks

    public DoubleLinkedList() {
        sent  = new Node();
        sent.next = sent;
        sent.prev = sent;
    }

    // linked list node helper data type
    private class Node {
        private E e;
        private Node next;
        private Node prev;
    }

    public boolean isEmpty()    { return size == 0; }
    public int size()           { return size;      }

    // add the e to the list
    public void add(E e) {
        Node last = sent.prev;
        Node x = new Node();
        x.e = e;
        x.next = sent;
        x.prev = last;
        sent.prev = x;
        last.next = x;
        size++;
        modCount++;
    }

    public ListIterator<E> iterator()  { return new DoubleLinkedListIterator(); }

    // assumes no calls to DoubleLinkedList.add() during iteration
    private class DoubleLinkedListIterator implements ListIterator<E> {
        private Node current      = sent.next;  // the node that is returned by next()
        private Node lastAccessed = null;      // the last node to be returned by prev() or next()
        // reset to null upon intervening remove() or add()
        private int index = 0;
        int expectedModCount = modCount;

        public boolean hasNext()      { return index < size;}
        public boolean hasPrevious()  { return index > 0; }
        public int previousIndex()    { return (index > 0)?index - 1:size-1; }
        public int nextIndex()        { return index;     }

        final void checkForComodification() { //Concurrent Modification check
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
        }

        public E next() {
            checkForComodification();
            if ((index==size)||(lastAccessed==current)){ index=0; current=sent.next; }
            E e = current.e;
            lastAccessed = current;
            current = (index<size-1)? current.next:sent.next;
            index++;
            return e;
        }

        public E previous() {
            checkForComodification();
            if (index==size){current=sent;}
            if (hasPrevious()){
                current = current.prev;
                index--;
            }else {
                current = sent.prev;
                index = size-1;
            }
            lastAccessed = current;
            return current.e;
        }

        // replace the e of the element that was last accessed by next() or previous()
        // condition: no calls to remove() or add() after last call to next() or previous()
        public void set(E e) {
            checkForComodification();
            if (lastAccessed == null) throw new IllegalStateException();
            lastAccessed.e = e;
        }

        // remove the element that was last accessed by next() or previous()
        // condition: no calls to remove() or add() after last call to next() or previous()
        public void remove() {
            checkForComodification();
            if (lastAccessed == null) throw new IllegalStateException();
            Node x = lastAccessed.prev;
            Node y = lastAccessed.next;
            x.next = y;
            y.prev = x;
            size--;
            if (current == lastAccessed)
                current = y;
            else
                index--;
            lastAccessed = null;
            expectedModCount=++modCount;
        }

        // add element to list 
        public void add(E e) {
            Node first = current.prev;
            Node node = new Node();
            Node last = current;
            node.e = e;
            first.next = node;
            node.next = last;
            last.prev = node;
            node.prev = first;
            size++;
            index++;
            lastAccessed = null;
            expectedModCount=++modCount;
        }

    }

    //block of basic list functions


    //get by index
    public E get(int index){
        if (!((index>=0)&&(index<size))) throw new NoSuchElementException();
        ListIterator <E> listIterator= iterator();
        if (index<=(size/2)){
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
        if (!((index>=0)&&(index<size))) throw new NoSuchElementException();
        ListIterator <E> listIterator= iterator();
        if (index<=(size/2)){
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
    public void add(int index, E e){
        if (!((index>=0)&&(index<=size))) throw new NoSuchElementException();
        if (index==size){
            add(e);
        }else {
            ListIterator <E> listIterator= iterator();
            if (index<=(size/2)){
                while (listIterator.nextIndex()<=index){
                    listIterator.next();
                }
            }else{
                while (listIterator.previousIndex()>=index){
                    listIterator.previous();
                }
            }
            listIterator.add(e);
        }
    }


    //toString implementation
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (E e : this)
            s.append(e + " ");
        return s.toString();
    }

    public ArrayList<E> toArray(){
        ArrayList<E> array=new ArrayList<E>();
        for (E e : this)
            array.add(e);
        return array;
    }

    //quick sort implementation
    public  void sort() {
        ArrayList<E> array = toArray();
        int startIndex = 0;
        int endIndex = array.size() - 1;
        doSort(startIndex, endIndex,array);
        ListIterator <E> listIterator= iterator();
        for (E e : array){
            listIterator.next();
            listIterator.set(e);
        }
    }

    private  void doSort(int start, int end, ArrayList<E> array ) {
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
                E temp = array.get(i);
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

    public <T extends Comparable<T>> DoubleLinkedList<T> map(AbstractFunction<E,T> function){
        DoubleLinkedList<T> transformedList = new DoubleLinkedList<>();
        for(E e:this){
            transformedList.add(function.apply(e));
        }
        return transformedList;
    }



}
