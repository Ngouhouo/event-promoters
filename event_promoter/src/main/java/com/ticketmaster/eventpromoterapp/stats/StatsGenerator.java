package com.ticketmaster.eventpromoterapp.stats;

import com.google.gson.Gson;
import com.ticketmaster.eventpromoterapp.model.Event;
import com.ticketmaster.eventpromoterapp.parser.ParserClass;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ticketmaster.eventpromoterapp.controler.RequestController.HOST;

public class StatsGenerator {
    ParserClass parserClass=new ParserClass();

    //returns australia events
    public ArrayList<Event> getAUEvents() throws Exception {
        ArrayList<Event> auEvents = new ArrayList<>();

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(HOST + "&countryCode=au"))
                .GET() // GET is default
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        Map<String, String> rawResponse = new Gson().fromJson(response.body(), HashMap.class);
        String embeddedReader = new Gson().toJson(rawResponse.get("_embedded"));
        Map<String, String> embeddedResponse = new Gson().fromJson(embeddedReader, HashMap.class);
        String eventsReader = new Gson().toJson(embeddedResponse.get("events"));
        ArrayList<String> events = new Gson().fromJson(eventsReader, ArrayList.class);
        for (int i = 0; i < events.size(); i++) {
            String eventReader = new Gson().toJson(events.get(i));
            Map<String, String> eventKeyValues = new Gson().fromJson(eventReader, HashMap.class);
            auEvents.add(new Event(eventKeyValues.get("name"), eventKeyValues.get("id"), eventKeyValues.get("type")));
        }
        return auEvents;
    }


    //returns new zealand events
    public ArrayList<Event> getNZEvents() throws Exception {
        ArrayList<Event>nzEvents=new ArrayList<>();

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(HOST+"&countryCode=nz"))
                .GET() // GET is default
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        Map<String,String>rawResponse=new Gson().fromJson(response.body(),HashMap.class);
        String embeddedReader=new Gson().toJson(rawResponse.get("_embedded"));
        Map<String,String>embeddedResponse=new Gson().fromJson(embeddedReader,HashMap.class);
        String eventsReader=new Gson().toJson(embeddedResponse.get("events"));
        ArrayList<String>events=new Gson().fromJson(eventsReader,ArrayList.class);
        for (int i=0;i<events.size();i++){
            String eventReader=new Gson().toJson(events.get(i));
            HashMap<String,String>eventKeyValues=new Gson().fromJson(eventReader,HashMap.class);
            nzEvents.add(new Event(eventKeyValues.get("name"),eventKeyValues.get("id"), eventKeyValues.get("type")));
        }

        return nzEvents;

    }

    //merges statistics of all states, two (New Zealand and Australia) in our case and returns in a key value style
   public String mergeStats() throws Exception {
        ArrayList<String>overallStats=new ArrayList<>();
        overallStats.add(auStatsOutput(getAUEvents()));
        overallStats.add(nzStatsOutput(getNZEvents()));
        return overallStats.toString();
    }

    //returns new Zealand Statistics still in a Key value style
    public String nzStatsOutput(List<Event> events) throws Exception {
        Map<String,String>eventsResponse=new HashMap<>();
        Map<String,String> stateStats=new HashMap<>();
        ArrayList<String>eventListResponse=new ArrayList<>();
        stateStats.put("State Name","New Zealand");
        for (int i=0;i<events.size();i++){
            eventsResponse.put("name",events.get(i).getName());
            eventsResponse.put("id",events.get(i).getId());
            eventsResponse.put("type",events.get(i).getType());
            eventListResponse.add(eventsResponse.toString());
        }
        stateStats.put("events",eventListResponse.toString());
        stateStats.put("Promoters Stats",getNZPromoters());
        return stateStats.toString();
    }

    //returns Australia Statistics still in a Key value style
    public String auStatsOutput(List<Event> events) throws Exception {
        Map<String,String>eventsResponse=new HashMap<>();
        Map<String,String>stateStats=new HashMap<>();
        ArrayList<String>eventListResponse=new ArrayList<>();
        stateStats.put("State Name","Australia");
        for (int i=0;i<events.size();i++){
            eventsResponse.put("name",events.get(i).getName());
            eventsResponse.put("id",events.get(i).getId());
            eventsResponse.put("type",events.get(i).getType());
            eventListResponse.add(eventsResponse.toString());
        }
        stateStats.put("events",eventListResponse.toString());
        stateStats.put("Promoters Stats",getAUPromoters());
        return stateStats.toString();
    }


    //returns australia promoters
    public String getAUPromoters() throws Exception{

        ParserClass responseModel=new ParserClass();

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(HOST+"&countryCode=au"))
                .GET() // GET is default
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());
        Map<String,String>rawResponse=new Gson().fromJson(response.body(),HashMap.class);
        String embeddedReader=new Gson().toJson(rawResponse.get("_embedded"));
        Map<String,String>embeddedResponse=new Gson().fromJson(embeddedReader,HashMap.class);
        String eventsReader=new Gson().toJson(embeddedResponse.get("events"));
        ArrayList<String>events=new Gson().fromJson(eventsReader,ArrayList.class);
        Map<String,String>promoterStats=new HashMap<>();
        promoterStats.put("total Promoters", parserClass.parsePromoters(responseModel.getTotalPromoters(events)));
        promoterStats.put("total promoters per genre",responseModel.getPromotersPerEventGenre(events).toString());

        return promoterStats.toString();
    }

    //returns New Zealand Promoters
    public String getNZPromoters() throws Exception{

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(HOST+"&countryCode=nz"))
                .GET() // GET is default
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());
        HashMap<String,String>rawResponse=new Gson().fromJson(response.body(),HashMap.class);
        String embeddedReader=new Gson().toJson(rawResponse.get("_embedded"));
        HashMap<String,String>embeddedResponse=new Gson().fromJson(embeddedReader,HashMap.class);
        String eventsReader=new Gson().toJson(embeddedResponse.get("events"));
        ArrayList<String>events=new Gson().fromJson(eventsReader,ArrayList.class);
        System.out.println((Object) events.get(1));
        HashMap<String,String>clientResponse=new HashMap<>();
        clientResponse.put("total Promoters", parserClass.parsePromoters(parserClass.getTotalPromoters(events)));
        clientResponse.put("total promoters per genre",parserClass.getPromotersPerEventGenre(events).toString());

        return clientResponse.toString();
    }

    public String getEventById(String id) throws IOException, InterruptedException {

        HashMap<String,String>eventOutput=new HashMap<>();

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(HOST + "&id="+id))
                .GET() // GET is default
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());


        Map<String,String>rawResponse=new Gson().fromJson(response.body(),HashMap.class);

        //get the embedded Object
        String embeddedReader=new Gson().toJson(rawResponse.get("_embedded"));
        Map<String,String>embeddedResponse=new Gson().fromJson(embeddedReader,HashMap.class);

        //get the event object
        String eventsReader=new Gson().toJson(embeddedResponse.get("events"));

        //parsing the event object
        List<String>eventList=new Gson().fromJson(eventsReader,ArrayList.class);
        for (int i=0;i<eventList.size();i++){
            String eventReader=new Gson().toJson(eventList.get(i));
            HashMap<String,String>event=new Gson().fromJson(eventReader,HashMap.class);
            eventOutput.put("name",event.get("name"));
            eventOutput.put("id",event.get("id"));
            eventOutput.put("type",event.get("type"));
        }

        return new Gson().toJson(eventOutput);
    }

}
