package com.ticketmaster.eventpromoterapp.controler;


import com.google.gson.Gson;
import com.ticketmaster.eventpromoterapp.model.Event;
import com.ticketmaster.eventpromoterapp.model.Promoter;
import com.ticketmaster.eventpromoterapp.model.ResponseModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/* @RestController annotation to indicate this is the class who handles (controls) the various client requests
through different requests mappings
 */
@RestController
public class RequestController {

    //key to access the ticket master API
    public final String API_KEY="ZO7QX2Dsb2QEN1ryYVxYz9svpRlFLATx";

    //url to the ticketMaster API with the appended API_KEY
    String HOST="http://app.ticketmaster.com/discovery/v2/events.json?apikey="+API_KEY;


    //this mapping ensures the method getStates() handles all the mapping requests to "/states"
    //gives statistics about each state. in our case just australia and new zealand are returned with their statistics
   //throws a exception because it won't compile otherwise due to many (risky) network requests
    @GetMapping("/stats/states")
    public String getStates() throws Exception{
        ArrayList<Event>nzEvents=new ArrayList<>(),auEvents=new ArrayList<>();

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

        return mergeStats(getAUEvents(),nzEvents);

    }
    @GetMapping("/event/{id}")
    public String getEventById(@PathVariable("id")String id) throws IOException, InterruptedException {

        HashMap<String,String>eventOutput=new HashMap<>();

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(HOST + "&id="+id))
                .GET() // GET is default
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());


        Map<String,String>rawResponse=new Gson().fromJson(response.body(),HashMap.class);
        String embeddedReader=new Gson().toJson(rawResponse.get("_embedded"));
        Map<String,String>embeddedResponse=new Gson().fromJson(embeddedReader,HashMap.class);
        String eventsReader=new Gson().toJson(embeddedResponse.get("events"));
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

    @GetMapping("event/period/{startDate}/{endDate}")
    public void getEventsInTimeRange(@PathVariable("startDate")String startDate,@PathVariable("endDate")String endDate){

    }


    public List<Event> getAUEvents() throws Exception {
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
            String eventReader=new Gson().toJson(events.get(i));
            Map<String, String> eventKeyValues = new Gson().fromJson(eventReader, HashMap.class);
            auEvents.add(new Event(eventKeyValues.get("name"),eventKeyValues.get("id"), eventKeyValues.get("type")));
        }

        return auEvents;

    }


    public String getAUPromoters() throws Exception{

        ResponseModel responseModel=new ResponseModel();

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
        Map<String,String>clientResponse=new HashMap<>();
        clientResponse.put("total Promoters",convertPromoters(responseModel.getTotalPromoters(events)));
        clientResponse.put("total promoters per genre",responseModel.getPromotersPerEventGenre(events).toString());

        return clientResponse.toString();
    }

    public String getNZPromoters() throws Exception{

        ResponseModel responseModel=new ResponseModel();

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
        clientResponse.put("total Promoters",convertPromoters(responseModel.getTotalPromoters(events)));
        clientResponse.put("total promoters per genre",responseModel.getPromotersPerEventGenre(events).toString());

        return clientResponse.toString();
    }




    String convertPromoters(ArrayList<Promoter> promoters){
        ArrayList<String>convertedPromoters=new ArrayList<>();
        HashMap<String,String>promoter=new HashMap<>();
        for (int i=0;i<promoters.size();i++){
            promoter.put("id",promoters.get(i).getId());
            promoter.put("name",promoters.get(i).getName());
            promoter.put("description",promoters.get(i).getDescription());
            convertedPromoters.add(promoter.toString());
        }
        return convertedPromoters.toString();
    }

    String nzStatsOutput(List<Event>events){
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
        return stateStats.toString();
    }

    String auStatsOutput(List<Event>events){
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
        return stateStats.toString();
    }
    String mergeStats(List<Event>auEvents,List<Event>nzEvents) throws Exception {
       ArrayList<String>overallStats=new ArrayList<>();
       overallStats.add(auStatsOutput(auEvents)+","+getAUPromoters());
       overallStats.add(nzStatsOutput(nzEvents)+","+getNZPromoters());
       return overallStats.toString();
    }






}
