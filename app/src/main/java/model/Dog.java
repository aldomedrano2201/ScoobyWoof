package model;

import interfaces.GetClassesProperties;

public class Dog implements GetClassesProperties {
    private String id;
    private String name;
    private String breed;
    private String description;

    public Dog(String id, String name) {
        this.name = name;
        this.id = id;
    }



    public Dog(String id, String name, String breed, String description) {
        this.id = id;
        this.name = name;
        this.breed = breed;
        this.description = description;
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

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }





    @Override
    public String name() {
        return null;
    }

    @Override
    public String dogName() {
        return name;
    }

    @Override
    public String id() {
        return id;
    }
}
