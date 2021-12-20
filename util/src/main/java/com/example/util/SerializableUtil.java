package com.example.util;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class SerializableUtil {
    private static final String TAG = "SerializableUtil";

    public static void saveObject() {
        try {
            User user = new User("test", 30);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(new File("/sdcard/a.txt")));
            objectOutputStream.writeObject(user);
            Log.i(TAG, "saveObject: " + user.toString());
        } catch (IOException e) {
            Log.e(TAG, "saveObject: e = " + e.getMessage());
        }
    }

    public static void getObject() {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(new File("/sdcard/a.txt")));
            User object = (User) objectInputStream.readObject();
            Log.i(TAG, "getObject: " + object.toString());
        } catch (IOException | ClassNotFoundException e) {
            Log.e(TAG, "getObject: " + e.getMessage());
        }
    }

    public static class User implements Serializable {

        private String name;
        private int age;

        public User(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        @Override
        public String toString() {
            return "User{name=" + name + ", age=" + age + '}';
        }
    }
}
