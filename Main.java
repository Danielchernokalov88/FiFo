package com.company;

import java.util.LinkedList;
import java.util.Random;

class Node<T>
{
    T data;
    Node<T> prev, next;

    public Node (T d, Node<T> p, Node<T> n)
    {
        data = d;
        prev = p;
        next = n;
    }
}

class Queue<T>
{
    Node<T> head, tail;
    Object signal = new Object();

    public Queue ()
    {
        head = null;
        tail = null;
    }

    public void enque(T data)
    {
        synchronized (signal) {
            Node<T> newNode = new Node<T>(data, tail, null);
            tail = newNode;
            if (head == null)
                head = tail;

            signal.notify();
        }
    }

    public T deque() {
        synchronized (signal) {
            if (head == null) {
                try {
                    signal.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            Node<T> tmp = head;

            head = head.next;
            if (head != null)
                head.prev = null;

            return tmp.data;
        }
    }

    public void display()
    {
        System.out.println("-- dumping queue contents --");
        for (Node<T> tmp = head; tmp != null; tmp = tmp.next)
            System.out.println(tmp.data);
        System.out.println("----------------------------");
    }
}

class ConcreteRunnable implements Runnable {
    Queue<Integer> que;
    public ConcreteRunnable(Queue<Integer> que)
    {
        this.que = que;
    }

    @Override
    public void run()
    {
        Random r = new Random(System.currentTimeMillis());

        // for (int i = 0; i < 1; i ++)
        {
            try {
                Thread.sleep(1000);
                que.enque(r.nextInt(100));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

public class Main {

    public static void main(String[] args) {
	    Queue<Integer> que = new Queue<Integer>();

	    ConcreteRunnable r = new ConcreteRunnable(que);
	    Thread test = new Thread(r);
	    test.start();

	    System.out.println(que.deque());

	    que.enque(34);
        que.enque(24);
        que.enque(134);
        que.enque(5);
        que.display();

        System.out.println(que.deque() == 34 ? "pass" : "fail");
        System.out.println(que.deque() == 24 ? "pass" : "fail");

        que.enque(127);
        System.out.println(que.deque() == 134 ? "pass" : "fail");

        System.out.println(que.deque() == 5 ? "pass" : "fail");
        System.out.println(que.deque() == 127 ? "pass" : "fail");
    }
}
