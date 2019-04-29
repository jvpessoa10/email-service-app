package com.joao.emailservice.utils;

import android.util.Log;

import com.joao.emailservice.models.Email;
import com.joao.emailservice.models.Node;

public class LinkedListUtils {

    public static Node<Email> removeEqualsFromHead(Node<Email> head){


        Node<Email> start = head;

        Log.d("Chegou:",head.toString());

        while(start != null){
            Node<Email> target = start.getNext();

            while(target != null && target.getNext() != null){

                String startString = start.getData().toString();
                String  targetString = target.getNext().getData().toString();

                if (startString.equals(targetString)){
                    target.setNext(target.getNext().getNext());

                }
                target = target.getNext();

            }

            start = start.getNext();

        }

        return head;
    }
}
