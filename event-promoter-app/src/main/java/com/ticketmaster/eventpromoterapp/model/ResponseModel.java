package com.ticketmaster.eventpromoterapp.model;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ResponseModel {
    ArrayList<Promoter>totalPromoters=new ArrayList<>();
    List<String>ids=new ArrayList<>();
    List<String>id2=new ArrayList<>();
    ArrayList<String>promotersPerGenre=new ArrayList<>();
    ArrayList<String>genres=new ArrayList<>();
    int realLength;

    public ArrayList<Promoter>getTotalPromoters(ArrayList<String> events){
        HashMap<String,String>event;
        ArrayList<String>promoters;
        HashMap<String,String>promoter;
        for (int i=0;i<events.size();i++){
            String eventReader=new Gson().toJson(events.get(i));
            event=new Gson().fromJson(eventReader,HashMap.class);
            String promotersReader=new Gson().toJson(event.get("promoters"));
            promoters=new Gson().fromJson(promotersReader,ArrayList.class);
            if (promoters!=null)
            for (int j=0;j<promoters.size();j++){
                String promoterReader=new Gson().toJson(promoters.get(j));
                promoter=new Gson().fromJson(promoterReader,HashMap.class);
                if (!ids.contains(promoter.get("id")))
                totalPromoters.add(new Promoter(promoter.get("id"),promoter.get("name"),promoter.get("description")));

                ids.add(promoter.get("id"));

            }
        }

        return totalPromoters;

    }

    public HashMap<String,String>getPromotersPerEventGenre(ArrayList<String>events){
        HashMap<String,String>event;
        HashMap<String,String>eventGenrePromoters=new HashMap<>();
        ArrayList<String>promoters;
        HashMap<String,String>promoter;
        for (int i=0;i<events.size();i++){
            String eventReader=new Gson().toJson(events.get(i));
            event=new Gson().fromJson(eventReader,HashMap.class);

            String classificationsReader=new Gson().toJson(event.get("classifications"));
            ArrayList<String>classifications;
            classifications=new Gson().fromJson(classificationsReader,ArrayList.class);

            String promotersPerEventGenreReader=new Gson().toJson(event.get("promoters"));
            ArrayList<String>promotersPerEventGenre=new Gson().fromJson(promotersPerEventGenreReader,ArrayList.class);

            for (int j=0;j<classifications.size();j++) {
                String classificationReader = new Gson().toJson(classifications.get(j));
                HashMap<String, String> classification = new Gson().fromJson(classificationReader, HashMap.class);
                String segmentReader = new Gson().toJson(classification.get("segment"));
                HashMap<String, String> segment = new Gson().fromJson(segmentReader, HashMap.class);
                String promotersReader=new Gson().toJson(event.get("promoters"));
                promoters=new Gson().fromJson(promotersReader,ArrayList.class);
                if (!genres.contains(segment.get("name"))&&promoters!=null)

                for (int k=0;k<promoters.size();k++){
                    String promoterReader=new Gson().toJson(promoters.get(k));
                    promoter=new Gson().fromJson(promoterReader,HashMap.class);

                    if (!id2.contains(promoter.get("id"))){
                        promotersPerGenre.add(promoter.toString());
                        eventGenrePromoters.put(segment.get("name"),promotersPerGenre.toString());
                    }
                    id2.add(promoter.get("id"));
                }
                genres.add(segment.get("name"));
            }

        }
       return eventGenrePromoters;
    }

}
