package com.example.jidelnicek;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Lunch implements Parcelable {

    private int Lunch_id;
    private String Name;
    private int Span;
    private String Type;

    public Lunch(int lunch_id, String name, int span, String type){
        setLunch_id(lunch_id);
        setName(name);
        setSpan(span);
        setType(type);
    }

    protected Lunch(Parcel in) {
        Lunch_id = in.readInt();
        Name = in.readString();
        Span = in.readInt();
        Type = in.readString();
    }

    public static final Creator<Lunch> CREATOR = new Creator<Lunch>() {
        @Override
        public Lunch createFromParcel(Parcel in) {
            return new Lunch(in);
        }

        @Override
        public Lunch[] newArray(int size) {
            return new Lunch[size];
        }
    };

    public String getName() { return Name; }

    public void setName(String name) {
        Name = name;
    }

    public int getSpan() {
        return Span;
    }

    public void setSpan(int span) {
        Span = span;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public int getLunch_id() { return Lunch_id; }

    public void setLunch_id(int lunch_id) {
        Lunch_id = lunch_id;
    }

    @NonNull
    @Override
    public String toString() {
        return "Name: " + this.Name + "\n" +
               "Span: " + this.Span + "\n" +
               "Type: " + this.Type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(Lunch_id);
        parcel.writeString(Name);
        parcel.writeInt(Span);
        parcel.writeString(Type);
    }
}
