package com.example.group.arouter.data;

import android.os.Parcel;
import android.os.Parcelable;

public class Person implements Parcelable {

    public String name;
    public int age;
    public float weight;

    public Person() {

    }

    protected Person(Parcel in) {
        name = in.readString();
        age = in.readInt();
        weight = in.readFloat();
    }

    public static final Creator<Person> CREATOR = new Creator<Person>() {
        @Override
        public Person createFromParcel(Parcel in) {
            return new Person(in);
        }

        @Override
        public Person[] newArray(int size) {
            return new Person[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(age);
        dest.writeFloat(weight);
    }

    @Override
    public String toString() {
        return "Person{name=" + name + ", age=" + age + ", weight=" + weight + "}";
    }
}
