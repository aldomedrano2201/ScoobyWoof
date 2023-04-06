package generics;

import java.io.Serializable;

import interfaces.GetClassesProperties;
import model.Dog;
import model.Request;

public class GenericClass<T> implements Serializable {
    private T value;
    private String name;
    private String dogName;
    private String id;
    private String description;
    private String phoneNumber;

    public GenericClass(T value) {
        this.value = value;
    }


    public GenericClass(String name, String phoneNumber, String id, String dogName, String description, T value) {
        this.phoneNumber = phoneNumber;
        this.name = name;
        this.dogName = dogName;
        this.id = id;
        this.description = description;
        this.value = value;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public String getDogName() {
        return dogName;
    }

    public void setDogName(String dogName) {
        this.dogName = dogName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
