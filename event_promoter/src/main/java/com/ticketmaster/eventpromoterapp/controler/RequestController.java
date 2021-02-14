package com.ticketmaster.eventpromoterapp.controler;


import com.google.gson.Gson;
import com.ticketmaster.eventpromoterapp.model.Event;
import com.ticketmaster.eventpromoterapp.model.Promoter;
import com.ticketmaster.eventpromoterapp.model.ResponseModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;


/* @RestController annotation to indicate this is the class who handles (controls) the various client requests
through different requests mappings
 */
@RestController
public class RequestController {

    //key to access the ticket master API
    public final String API_KEY="ZO7QX2Dsb2QEN1ryYVxYz9svpRlFLATx";

    //url to the ticketMaster API with the appended API_KEY
    String HOST="http://app.ticketmaster.com/discovery/v2/events.json?apikey="+API_KEY;

    ResponseModel responseModel=new ResponseModel();


    //this mapping ensures the method getStates() handles all the mapping requests to "/states"
    //gives statistics about each state. in our case just australia and new zealand are returned with their statistics
   //throws a exception because it won't compile otherwise due to many (risky) network requests
    @GetMapping("/stats/states")
    public String getStates() throws Exception{
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

        return mergeStats(getAUEvents(),nzEvents);

    }

    //returns an event by its id
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

    /*returns events in australia within a defined start and end date. takes a s request body a hasMap who contains
    the start and end dates in String.
     */
    @GetMapping("/events/Australia/period")
    public String getAuEventsInTimeRange(@RequestBody HashMap<String,String> startEndDate) throws IOException, InterruptedException {
        HashMap<String,String>eventOutput=new HashMap<>();
        HttpClient client = HttpClient.newHttpClient();

        //this first method returns events of australia
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(HOST+"&countryCode=au&startEndDate={"+startEndDate.get("startDate")+"&"+startEndDate.get("endDate")+"}"))
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
    /*returns events in new zealand within a defined start and end date. takes a s request body a hasMap who contains
    the start and end dates in String.
     */
    @GetMapping("/events/New_Zealand/period")
    public String getNzEventsInTimeRange(@RequestBody HashMap<String,String> startEndDate) throws IOException, InterruptedException {
        HashMap<String,String>eventOutput=new HashMap<>();
        HttpClient client = HttpClient.newHttpClient();

        //this first method returns events of australia
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(HOST+"&countryCode=nz&startEndDate={"+startEndDate.get("startDate")+"&"+startEndDate.get("endDate")+"}"))
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

    /*returns the event in the defined period with no respect to the country (state) ie. it returns events in the given period of
    australia and new zealand
     */
    @GetMapping("/events/period")
    String mergeEventPeriods(@RequestBody HashMap<String,String> startEndDate) throws IOException, InterruptedException {
        Map<String,String>totalEventsPeriod=new HashMap<>();
        totalEventsPeriod.put("Events in australia",getAuEventsInTimeRange(startEndDate));
        totalEventsPeriod.put("Events in New Zealand",getNzEventsInTimeRange(startEndDate));
        return totalEventsPeriod.toString();
    }

    /*
     mapping to filter the states stats with path variable state_name which is in ISO code format
     example: nz for New Zealand and au for  Australia.
     */
    @GetMapping("/stats/states/{state_name}")
    public String filterStatesStats(@PathVariable("state_name")String state) throws Exception {

        //if state name (ISO code) is that of australia returns only australia statistics
        if (state.equalsIgnoreCase("au")){
            ArrayList<Event>events=new ArrayList<>();
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
            List<String>eventList=new Gson().fromJson(eventsReader,ArrayList.class);
            for (int i=0;i<eventList.size();i++){
                String eventReader=new Gson().toJson(eventList.get(i));
                HashMap<String,String>event=new Gson().fromJson(eventReader,HashMap.class);
                events.add(new Event(event.get("name"),event.get("id"),event.get("type")));
            }

            return auStatsOutput(events);

        }
        //if state name (ISO code) is that of New Zealand returns only australia statistics
        else if (state.equalsIgnoreCase("nz")){
            ArrayList<Event>events=new ArrayList<>();
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
            List<String>eventList=new Gson().fromJson(eventsReader,ArrayList.class);
            for (int i=0;i<eventList.size();i++){
                String eventReader=new Gson().toJson(eventList.get(i));
                HashMap<String,String>event=new Gson().fromJson(eventReader,HashMap.class);
                events.add(new Event(event.get("name"),event.get("id"),event.get("type")));
            }

            return nzStatsOutput(events);

        }

        //return unknown state if state name (ISO code) is neither nz nor au
        return "Unknown state name or ISO code";

    }
    /*
    since an id is unique across all the entire system, no need to precise which country to look for promoter
    because the id will provide explicit information about the events with the provided promoterId
    this mapping returns the event that are mentored (promoted) by this promoter.
     */
    @GetMapping("/events/promoter/{promoterId}")
    String getEventByPromoterId(@PathVariable("promoterId")String promoterId) throws IOException, InterruptedException {
        HashMap<String,String>eventByPromoterId=new HashMap<>();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(HOST+"&promoterId="+promoterId))
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
            eventByPromoterId.put("name",event.get("name"));
            eventByPromoterId.put("id",event.get("id"));
            eventByPromoterId.put("type",event.get("type"));
        }

        return new Gson().toJson(eventByPromoterId);
    }

    //return promoter stats by promoterId
    @GetMapping("/stats/promoter/{promoterId}")
    String getStatsByPromoterId(@PathVariable("promoterId")String promoterId) throws IOException, InterruptedException {
        ArrayList<String>totalEventsMentored=new ArrayList<>();
        HashMap<String,String>promoterStats=new HashMap<>();
        HashMap<String,String>eventByPromoterId=new HashMap<>();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(HOST+"&promoterId="+promoterId))
                .GET() // GET is default
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());
        Map<String,String>rawResponse=new Gson().fromJson(response.body(),HashMap.class);
        String embeddedReader=new Gson().toJson(rawResponse.get("_embedded"));
        Map<String,String>embeddedResponse=new Gson().fromJson(embeddedReader,HashMap.class);
        String eventsReader=new Gson().toJson(embeddedResponse.get("events"));
        ArrayList<String>eventList=new Gson().fromJson(eventsReader,ArrayList.class);
        for (int i=0;i<eventList.size();i++){
            String eventReader=new Gson().toJson(eventList.get(i));
            HashMap<String,String>event=new Gson().fromJson(eventReader,HashMap.class);
            eventByPromoterId.put("name",event.get("name"));
            eventByPromoterId.put("id",event.get("id"));
            eventByPromoterId.put("type",event.get("type"));
            totalEventsMentored.add(eventByPromoterId.toString());
        }
        promoterStats.put("Total events mentored",totalEventsMentored.toString());
        promoterStats.put("Total events per type ",responseModel.getPromotersPerEventGenre(eventList).toString());

        return promoterStats.toString();
    }

    /*
    since segment is considered to be the primary event genre, this mapping returns event by segment name in other words,
    this mapping filters events by event genre.
     */
    @GetMapping("/events/genre/{segment_name}")
    public String getEventsBySegmentName(@PathVariable("segment_name")String segmentName) throws IOException, InterruptedException {
        HashMap<String,String>eventsBySegmentName=new HashMap<>();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(HOST+"&segmentName="+segmentName))
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
            eventsBySegmentName.put("name",event.get("name"));
            eventsBySegmentName.put("id",event.get("id"));
            eventsBySegmentName.put("type",event.get("type"));
        }

        return new Gson().toJson(eventsBySegmentName);
    }

    //returns australia events
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


    //returns australia promoters
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
        Map<String,String>promoterStats=new HashMap<>();
        promoterStats.put("total Promoters", parsePromoters(responseModel.getTotalPromoters(events)));
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
        clientResponse.put("total Promoters", parsePromoters(responseModel.getTotalPromoters(events)));
        clientResponse.put("total promoters per genre",responseModel.getPromotersPerEventGenre(events).toString());

        return clientResponse.toString();
    }



    //parses promoters in HasMap (Key Value) style
    String parsePromoters(ArrayList<Promoter> promoters){
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

    //returns new Zealand Statistics still in a Key value style
    String nzStatsOutput(List<Event>events) throws Exception {
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
    String auStatsOutput(List<Event>events) throws Exception {
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

    //merges statistics of all states, two (New Zealand and Australia) in our case and returns in a key value style
    String mergeStats(List<Event>auEvents,List<Event>nzEvents) throws Exception {
       ArrayList<String>overallStats=new ArrayList<>();
       overallStats.add(auStatsOutput(auEvents));
       overallStats.add(nzStatsOutput(nzEvents));
       return overallStats.toString();
    }
}
