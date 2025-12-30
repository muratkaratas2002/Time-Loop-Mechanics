/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

/**
 *
 * @author MURAT KARATAŞ
 */
class MyLinkedList<T> {
    public Node<T> first;
    public Node<T> last;
    private int size;

    public MyLinkedList() {
        this.first = null;
        this.last = null;
        this.size = 0;
    }

    
    public void add(T data) {
        Node<T> newNode = new Node<>(data);
        if (first == null) {
            first = newNode;
            last = newNode;
        } else {
            last.next = newNode;
            last = newNode;
        }
        size++;
    }

  
    public boolean remove(T data) {
        if (first == null) return false;

        if (first.data.equals(data)) {
            first = first.next;
            if (first == null) {
                last = null;
            }
            size--;
            return true;
        }

        Node<T> current = first;
        while (current.next != null && !current.next.data.equals(data)) {
            current = current.next;
        }

        if (current.next != null) {
            Node<T> nodeToRemove = current.next;
            current.next = nodeToRemove.next;
            
            if (current.next == null) {
                last = current;
            }
            size--;
            return true;
        }
        return false;
    }

    
    public boolean contains(T data) {
        Node<T> current = first;
        while (current != null) {
            if (current.data.equals(data)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    
    public T get(int index) {
        if (index < 0 || index >= size) {
            return null; 
        }
        Node<T> current = first;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current.data;
    }


    public MyLinkedList<T> copy() {
        MyLinkedList<T> newList = new MyLinkedList<>();
        Node<T> current = first;
        while (current != null) {
            newList.add(current.data);
            current = current.next;
        }
        return newList;
    }


    public MyLinkedList<T> copyUpTo(int endIndex) {
        MyLinkedList<T> newList = new MyLinkedList<>();
        if (endIndex < 0 || first == null) {
            return newList;
        }

        Node<T> current = first;
        for (int i = 0; i <= endIndex && current != null; i++) {
            newList.add(current.data);
            current = current.next;
        }
        return newList;
    }

    public int size() {
        return size;
    }

    public void printList() {
        Node<T> current = first;
        while (current != null) {
            System.out.print(current.data + " -> ");
            current = current.next;
        }
        System.out.println("null");
    }
}
