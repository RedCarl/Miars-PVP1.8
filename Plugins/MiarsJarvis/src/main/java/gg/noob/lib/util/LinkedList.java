package gg.noob.lib.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.AbstractSequentialList;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class LinkedList<E> extends AbstractSequentialList<E> implements List<E>, Deque<E>, Cloneable, Serializable {
  private transient Entry<E> header = new Entry<>(null, null, null);

  private transient int size = 0;

  private static final long serialVersionUID = 876323262645176354L;

  public LinkedList() {
    Entry<E> header = this.header;
    Entry<E> header2 = this.header;
    Entry<E> header3 = this.header;
    header2.previous = header3;
    header.next = header3;
  }

  public LinkedList(Collection<? extends E> c) {
    this();
    addAll(c);
  }
  @Override
  public E getFirst() {
    if (this.size == 0) {
      throw new NoSuchElementException();
    }
    return this.header.next.element;
  }

  @Override
  public E getLast() {
    if (this.size == 0) {
      throw new NoSuchElementException();
    }
    return this.header.previous.element;
  }
  @Override
  public E removeFirst() {
    return remove(this.header.next);
  }
  @Override
  public E removeLast() {
    return remove(this.header.previous);
  }
  @Override
  public void addFirst(E e) {
    addBefore(e, this.header.next);
  }
  @Override
  public void addLast(E e) {
    addBefore(e, this.header);
  }
  @Override
  public boolean contains(Object o) {
    return (indexOf(o) != -1);
  }
  @Override
  public int size() {
    return this.size;
  }
  @Override
  public boolean add(E e) {
    addBefore(e, this.header);
    return true;
  }
  @Override
  public boolean remove(Object o) {
    if (o == null) {
      for (Entry<E> e = this.header.next; e != this.header; e = e.next) {
        if (e.element == null) {
          remove(e);
          return true;
        }
      }
    } else {
      for (Entry<E> e = this.header.next; e != this.header; e = e.next) {
        if (o.equals(e.element)) {
          remove(e);
          return true;
        }
      }
    }
    return false;
  }
  @Override
  public boolean addAll(Collection<? extends E> c) {
    return addAll(this.size, c);
  }
  @Override
  public boolean addAll(int index, Collection<? extends E> c) {
    if (index < 0 || index > this.size) {
      throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + this.size);
    }
    Object[] a = c.toArray();
    int numNew = a.length;
    if (numNew == 0) {
      return false;
    }
    this.modCount++;
    Entry<E> successor = (index == this.size) ? this.header : entry(index);
    Entry<E> predecessor = successor.previous;
    for (int i = 0; i < numNew; i++) {
      Entry<E> e = new Entry<>((E)a[i], successor, predecessor);
      predecessor.next = e;
      predecessor = e;
    }
    successor.previous = predecessor;
    this.size += numNew;
    return true;
  }
  @Override
  public void clear() {
    this.modCount++;
    this.header = new Entry<>(null, null, null);
    Entry<E> header = this.header;
    Entry<E> header2 = this.header;
    Entry<E> header3 = this.header;
    header2.previous = header3;
    header.next = header3;
    this.size = 0;
  }
  @Override
  public E get(int index) {
    return (entry(index)).element;
  }
  @Override
  public E set(int index, E element) {
    Entry<E> e = entry(index);
    E oldVal = e.element;
    e.element = element;
    return oldVal;
  }
  @Override
  public void add(int index, E element) {
    addBefore(element, (index == this.size) ? this.header : entry(index));
  }
  @Override
  public E remove(int index) {
    return remove(entry(index));
  }

  private Entry<E> entry(int index) {
    if (index < 0 || index >= this.size) {
      throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + this.size);
    }
    Entry<E> e = this.header;
    if (index < this.size >> 1) {
      for (int i = 0; i <= index; i++) {
        e = e.next;
      }
    } else {
      for (int i = this.size; i > index; i--) {
        e = e.previous;
      }
    }
    return e;
  }
  @Override
  public int indexOf(Object o) {
    int index = 0;
    if (o == null) {
      for (Entry<E> e = this.header.next; e != this.header; e = e.next) {
        if (e.element == null) {
          return index;
        }
        index++;
      }
    } else {
      for (Entry<E> e = this.header.next; e != this.header; e = e.next) {
        if (o.equals(e.element)) {
          return index;
        }
        index++;
      }
    }
    return -1;
  }
  @Override
  public int lastIndexOf(Object o) {
    int index = this.size;
    if (o == null) {
      for (Entry<E> e = this.header.previous; e != this.header; e = e.previous) {
        index--;
        if (e.element == null) {
          return index;
        }
      }
    } else {
      for (Entry<E> e = this.header.previous; e != this.header; e = e.previous) {
        index--;
        if (o.equals(e.element)) {
          return index;
        }
      }
    }
    return -1;
  }
  @Override
  public E peek() {
    if (this.size == 0) {
      return null;
    }
    return getFirst();
  }
  @Override
  public E element() {
    return getFirst();
  }
  @Override
  public E poll() {
    if (this.size == 0) {
      return null;
    }
    return removeFirst();
  }
  @Override
  public E remove() {
    return removeFirst();
  }
  @Override
  public boolean offer(E e) {
    return add(e);
  }
  @Override
  public boolean offerFirst(E e) {
    addFirst(e);
    return true;
  }
  @Override
  public boolean offerLast(E e) {
    addLast(e);
    return true;
  }
  @Override
  public E peekFirst() {
    if (this.size == 0) {
      return null;
    }
    return getFirst();
  }
  @Override
  public E peekLast() {
    if (this.size == 0) {
      return null;
    }
    return getLast();
  }
  @Override
  public E pollFirst() {
    if (this.size == 0) {
      return null;
    }
    return removeFirst();
  }
  @Override
  public E pollLast() {
    if (this.size == 0) {
      return null;
    }
    return removeLast();
  }
  @Override
  public void push(E e) {
    addFirst(e);
  }
  @Override
  public E pop() {
    return removeFirst();
  }
  @Override
  public boolean removeFirstOccurrence(Object o) {
    return remove(o);
  }
  @Override
  public boolean removeLastOccurrence(Object o) {
    if (o == null) {
      for (Entry<E> e = this.header.previous; e != this.header; e = e.previous) {
        if (e.element == null) {
          remove(e);
          return true;
        }
      }
    } else {
      for (Entry<E> e = this.header.previous; e != this.header; e = e.previous) {
        if (o.equals(e.element)) {
          remove(e);
          return true;
        }
      }
    }
    return false;
  }
  @Override
  public ListIterator<E> listIterator(int index) {
    return new ListItr(index);
  }

  private Entry<E> addBefore(E e, Entry<E> entry) {
    Entry<E> newEntry = new Entry<>(e, entry, entry.previous);
    newEntry.previous.next = newEntry;
    newEntry.next.previous = newEntry;
    this.size++;
    this.modCount++;
    return newEntry;
  }

  private E remove(Entry<E> e) {
    if (e == this.header) {
      throw new NoSuchElementException();
    }
    E result = e.element;
    e.previous.next = e.next;
    e.next.previous = e.previous;
    Entry<E> entry = null;
    e.previous = entry;
    e.next = entry;
    e.element = null;
    this.size--;
    this.modCount++;
    return result;
  }
  @Override
  public Iterator<E> descendingIterator() {
    return new DescendingIterator();
  }
  @Override
  public Object clone() {
    LinkedList<E> clone = null;
    try {
      clone = (LinkedList<E>)super.clone();
    } catch (CloneNotSupportedException e2) {
      throw new InternalError();
    }
    clone.header = new Entry<>(null, null, null);
    Entry<E> header = clone.header;
    Entry<E> header2 = clone.header;
    Entry<E> header3 = clone.header;
    header2.previous = header3;
    header.next = header3;
    clone.size = 0;
    clone.modCount = 0;
    for (Entry<E> e = this.header.next; e != this.header; e = e.next) {
      clone.add(e.element);
    }
    return clone;
  }
  @Override
  public Object[] toArray() {
    Object[] result = new Object[this.size];
    int i = 0;
    for (Entry<E> e = this.header.next; e != this.header; e = e.next) {
      result[i++] = e.element;
    }
    return result;
  }
  @Override
  public <T> T[] toArray(T[] a) {
    if (a.length < this.size) {
      a = (T[])Array.newInstance(a.getClass().getComponentType(), this.size);
    }
    int i = 0;
    T[] arrayOfT = a;
    for (Entry<E> e = this.header.next; e != this.header; e = e.next) {
      arrayOfT[i++] = (T)e.element;
    }
    if (a.length > this.size) {
      a[this.size] = null;
    }
    return a;
  }

  private void writeObject(ObjectOutputStream s) throws IOException {
    s.defaultWriteObject();
    s.writeInt(this.size);
    for (Entry<E> e = this.header.next; e != this.header; e = e.next) {
      s.writeObject(e.element);
    }
  }

  private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
    s.defaultReadObject();
    int size = s.readInt();
    this.header = new Entry<>(null, null, null);
    Entry<E> header = this.header;
    Entry<E> header2 = this.header;
    Entry<E> header3 = this.header;
    header2.previous = header3;
    header.next = header3;
    for (int i = 0; i < size; i++) {
      addBefore((E)s.readObject(), this.header);
    }
  }

  private class ListItr implements ListIterator<E> {
    private LinkedList.Entry<E> lastReturned = LinkedList.this.header;

    private LinkedList.Entry<E> next;

    private int nextIndex;

    private int expectedModCount = LinkedList.this.modCount;

    ListItr(int index) {
      if (index < 0 || index > LinkedList.this.size) {
        throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + LinkedList.this.size);
      }
      if (index < LinkedList.this.size >> 1) {
        this.next = LinkedList.this.header.next;
        this.nextIndex = 0;
        while (this.nextIndex < index) {
          this.next = this.next.next;
          this.nextIndex++;
        }
      } else {
        this.next = LinkedList.this.header;
        this.nextIndex = LinkedList.this.size;
        while (this.nextIndex > index) {
          this.next = this.next.previous;
          this.nextIndex--;
        }
      }
    }

    @Override
    public boolean hasNext() {
      return (this.nextIndex != LinkedList.this.size);
    }

    @Override
    public E next() {
      checkForComodification();
      if (this.nextIndex == LinkedList.this.size) {
        throw new NoSuchElementException();
      }
      this.lastReturned = this.next;
      this.next = this.next.next;
      this.nextIndex++;
      return this.lastReturned.element;
    }

    @Override
    public boolean hasPrevious() {
      return (this.nextIndex != 0);
    }

    @Override
    public E previous() {
      if (this.nextIndex == 0) {
        throw new NoSuchElementException();
      }
      LinkedList.Entry<E> previous = this.next.previous;
      this.next = previous;
      this.lastReturned = previous;
      this.nextIndex--;
      checkForComodification();
      return this.lastReturned.element;
    }

    @Override
    public int nextIndex() {
      return this.nextIndex;
    }

    @Override
    public int previousIndex() {
      return this.nextIndex - 1;
    }

    @Override
    public void remove() {
      checkForComodification();
      LinkedList.Entry<E> lastNext = this.lastReturned.next;
      try {
        LinkedList.this.remove(this.lastReturned);
      } catch (NoSuchElementException e) {
        throw new IllegalStateException();
      }
      if (this.next == this.lastReturned) {
        this.next = lastNext;
      } else {
        this.nextIndex--;
      }
      this.lastReturned = LinkedList.this.header;
      this.expectedModCount++;
    }

    @Override
    public void set(E e) {
      if (this.lastReturned == LinkedList.this.header) {
        throw new IllegalStateException();
      }
      checkForComodification();
      this.lastReturned.element = e;
    }

    @Override
    public void add(E e) {
      checkForComodification();
      this.lastReturned = LinkedList.this.header;
      LinkedList.this.addBefore(e, this.next);
      this.nextIndex++;
      this.expectedModCount++;
    }

    final void checkForComodification() {
      if (LinkedList.this.modCount != this.expectedModCount) {
        throw new ConcurrentModificationException();
      }
    }
  }

  private static class Entry<E> {
    E element;

    Entry<E> next;

    Entry<E> previous;

    Entry(E element, Entry<E> next, Entry<E> previous) {
      this.element = element;
      this.next = next;
      this.previous = previous;
    }
  }

  private class DescendingIterator implements Iterator {
    final LinkedList<E>.ListItr itr = new LinkedList.ListItr(LinkedList.this.size());

    @Override
    public boolean hasNext() {
      return this.itr.hasPrevious();
    }

    @Override
    public E next() {
      return this.itr.previous();
    }

    @Override
    public void remove() {
      this.itr.remove();
    }

    private DescendingIterator() {}
  }
}
