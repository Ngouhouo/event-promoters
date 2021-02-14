package com.ticketmaster.eventpromoterapp.model;

import java.util.Date;

public class Event{
    String name,id,type;

    public Event(String name, String id, String type){
        this.id=id;
        this.name=name;
        this.type=type;

    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }


    public String getType() {
        return type;
    }
}
