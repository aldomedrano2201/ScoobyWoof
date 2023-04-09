package model;

import android.widget.ListView;

import androidx.lifecycle.ViewModel;

import generics.GenericAdapter;
import generics.GenericClass;

import java.util.ArrayList;

public class GeneralListViews extends ViewModel {

    public static ArrayList<Dog> dogsList= new ArrayList<>();


    public static GenericAdapter adapter;

    public static ListView lvGenericlist;

    public static ArrayList<Request> requestList = new ArrayList<>();

    public static ArrayList<Notification> notificationsList= new ArrayList<>();

}
