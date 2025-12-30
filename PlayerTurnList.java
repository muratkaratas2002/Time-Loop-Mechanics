/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

/**
 *
 * @author MURAT KARATAŞ
 */
class PlayerTurnList<T> {
    public DoubleNode<T> head;
    private int size;

    public PlayerTurnList() {
        this.head = null;
        this.size = 0;
    }

    
    public void add(T data) {
        DoubleNode<T> newNode = new DoubleNode<>(data);
        if (head == null) {
            head = newNode;
            head.next = head;
            head.previous = head;
        } else {
            DoubleNode<T> tail = head.previous;
            tail.next = newNode;
            newNode.previous = tail;
            newNode.next = head;
            head.previous = newNode;
        }
        size++;
    }

   
    public DoubleNode<T> findNode(T data) {
        if (head == null) return null;
        DoubleNode<T> current = head;
        do {
            if (current.data.equals(data)) {
                return current;
            }
            current = current.next;
        } while (current != head);
        return null;
    }

    public int size() {
        return size;
    }
    
    public void printList() {
        if (head == null) {
            System.out.println("List is empty.");
            return;
        }
        DoubleNode<T> current = head;
        do {
            System.out.print(current.data + " <-> ");
            current = current.next;
        } while (current != head);
        System.out.println("(loops back to " + head.data + ")");
    }
}