package com.ticketmaster.eventpromoterapp.parser;

import com.google.gson.Gson;
import com.ticketmaster.eventpromoterapp.model.Promoter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


   /*
   This class is essentially based on the Gson parser.
   Because of its structure, once a parse is done, to reparse the already parsed object, a reconversion from the parsed object back to Json
   is needed. for example: if we want to parse a Json who contains Arraylist and Maps, we first need to parse the first time as a String reader
   then we use this reader to parse to parse to the desired datatype (Map,list,array...etc) that's why at many places i first parsed the objects
   to Json (String reader) then parse again to the desired type. If this was not done, a Runtime exception will be thrown saying inconvertible types.
    */

public class ParserClass {
    ArrayList<Promoter>totalPromoters=new ArrayList<>();
    List<String>ids=new ArrayList<>();
    List<String>id2=new ArrayList<>();
    ArrayList<String>promotersPerGenre=new ArrayList<>();
    ArrayList<String>genres=new ArrayList<>();

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

    //parses promoters in HasMap (Key Value) style
    public String parsePromoters(ArrayList<Promoter> promoters) {
        ArrayList<String> convertedPromoters = new ArrayList<>();
        HashMap<String, String> promoter = new HashMap<>();
        for (int i = 0; i < promoters.size(); i++) {
            promoter.put("id", promoters.get(i).getId());
            promoter.put("name", promoters.get(i).getName());
            promoter.put("description", promoters.get(i).getDescription());
            convertedPromoters.add(promoter.toString());
        }
        return convertedPromoters.toString();
    }

}
