package com.ticketmaster.eventpromoterapp.model;

public class Promoter {
    //promoterId
    String id,name,description;

    //constructor to set initialize th promoterId
    public Promoter(String id,String description,String name){
        this.id=id;
        this.name=name;
        this.description=description;
    }

    //String method to return the id of this promoter
    public String getId(){
        return id;
    }

    //String method to return the description of this promoter
    public String getDescription() {
        return description;
    }

    //String method to return the name of this promoter
    public String getName() {
        return name;
    }
}
