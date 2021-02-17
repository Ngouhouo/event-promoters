package com.ticketmaster.eventpromoterapp.filters;

import com.google.gson.Gson;
import com.ticketmaster.eventpromoterapp.model.Event;
import com.ticketmaster.eventpromoterapp.parser.ParserClass;
import com.ticketmaster.eventpromoterapp.stats.StatsGenerator;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

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

public class FilterClass {

    StatsGenerator statsGenerator = new StatsGenerator();
    ParserClass parserClass=new ParserClass();

    public String filterByEventGenre(String segmentName) throws IOException, InterruptedException {
        HashMap<String, String> eventsBySegmentName = new HashMap<>();
        ArrayList<String>events=new ArrayList<>();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(HOST + "&segmentName=" + segmentName))
                .GET() // GET is default
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());
        Map<String, String> rawResponse = new Gson().fromJson(response.body(), HashMap.class);
        if (rawResponse.get("_embedded")==null)
            return "wrong segment Name or Segment Name not existing";
        String embeddedReader = new Gson().toJson(rawResponse.get("_embedded"));
        Map<String, String> embeddedResponse = new Gson().fromJson(embeddedReader, HashMap.class);
        String eventsReader = new Gson().toJson(embeddedResponse.get("events"));
        List<String> eventList = new Gson().fromJson(eventsReader, ArrayList.class);
        for (int i = 0; i < eventList.size(); i++) {
            String eventReader = new Gson().toJson(eventList.get(i));
            HashMap<String, String> event = new Gson().fromJson(eventReader, HashMap.class);
            eventsBySegmentName.put("name", event.get("name"));
            eventsBySegmentName.put("id", event.get("id"));
            eventsBySegmentName.put("type", event.get("type"));
            events.add(eventsBySegmentName.toString());
        }

        return events.toString();
    }

    public String filterStatesStats(String state) throws Exception {

            //if state name (ISO code) is that of australia returns only australia statistics
            if (state.equalsIgnoreCase("au")) {
                ArrayList<Event> events = new ArrayList<>();
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
                List<String> eventList = new Gson().fromJson(eventsReader, ArrayList.class);
                for (int i = 0; i < eventList.size(); i++) {
                    String eventReader = new Gson().toJson(eventList.get(i));
                    HashMap<String, String> event = new Gson().fromJson(eventReader, HashMap.class);
                    events.add(new Event(event.get("name"), event.get("id"), event.get("type")));
                }

                return statsGenerator.auStatsOutput(events);

            }
            //if state name (ISO code) is that of New Zealand returns only australia statistics
            else if (state.equalsIgnoreCase("nz")) {
                ArrayList<Event> events = new ArrayList<>();
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(HOST + "&countryCode=nz"))
                        .GET() // GET is default
                        .build();

                HttpResponse<String> response =
                        client.send(request, HttpResponse.BodyHandlers.ofString());


                Map<String, String> rawResponse = new Gson().fromJson(response.body(), HashMap.class);
                String embeddedReader = new Gson().toJson(rawResponse.get("_embedded"));
                Map<String, String> embeddedResponse = new Gson().fromJson(embeddedReader, HashMap.class);
                String eventsReader = new Gson().toJson(embeddedResponse.get("events"));
                List<String> eventList = new Gson().fromJson(eventsReader, ArrayList.class);
                for (int i = 0; i < eventList.size(); i++) {
                    String eventReader = new Gson().toJson(eventList.get(i));
                    HashMap<String, String> event = new Gson().fromJson(eventReader, HashMap.class);
                    events.add(new Event(event.get("name"), event.get("id"), event.get("type")));
                }

                return statsGenerator.nzStatsOutput(events);

            }

            //return unknown state if state name (ISO code) is neither nz nor au
            return "Unknown state name or ISO code";

        }

   public String filterEventsByPromoterId(String promoterId) throws IOException, InterruptedException {
        HashMap<String,String>eventByPromoterId=new HashMap<>();
        ArrayList<String>events=new ArrayList<>();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(HOST+"&promoterId="+promoterId))
                .GET() // GET is default
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());
        Map<String,String>rawResponse=new Gson().fromJson(response.body(),HashMap.class);
       if (rawResponse.get("_embedded")==null)
           return "wrong promoter id";

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
            events.add(eventByPromoterId.toString());
        }

        return events.toString();
    }

   public String filterStatsByPromoterId(String promoterId) throws IOException, InterruptedException {
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
       if (rawResponse.get("_embedded")==null)
           return "wrong promoter id";

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
        promoterStats.put("Total events per type ", parserClass.getPromotersPerEventGenre(eventList).toString());

        return promoterStats.toString();
    }

    public String filterNZEventsByTimeRange(HashMap<String,String> startEndDate) throws IOException, InterruptedException {
        HashMap<String,String>eventOutput=new HashMap<>();
        HttpClient client = HttpClient.newHttpClient();
        ArrayList<String>totalEvents=new ArrayList<>();

        //this first method returns events of australia
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(HOST+"&countryCode=nz&startEndDateTime="+startEndDate.get("startDate")+"T00:00:00Z,"+startEndDate.get("endDate")+"T00:00:00Z"))
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
            totalEvents.add(eventOutput.toString());
        }

        return totalEvents.toString();

    }

    public String filterAUEventsByTimeRange(HashMap<String,String> startEndDate) throws IOException, InterruptedException {
        HashMap<String,String>eventOutput=new HashMap<>();
        ArrayList<String>totalEvents=new ArrayList<>();
        HttpClient client = HttpClient.newHttpClient();

        //this first method returns events of australia
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(HOST+"&countryCode=au&startEndDateTime="+startEndDate.get("startDate")+"T00:00:00Z,"+startEndDate.get("endDate")+"T00:00:00Z"))
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
            totalEvents.add(eventOutput.toString());
        }

        return totalEvents.toString();

    }

  public  String mergeEventPeriods(HashMap<String,String> startEndDate) throws IOException, InterruptedException {
        Map<String,String>totalEventsPeriod=new HashMap<>();
        totalEventsPeriod.put("Events in australia",filterAUEventsByTimeRange(startEndDate));
        totalEventsPeriod.put("Events in New Zealand",filterNZEventsByTimeRange(startEndDate));
        return totalEventsPeriod.toString();
    }


}
