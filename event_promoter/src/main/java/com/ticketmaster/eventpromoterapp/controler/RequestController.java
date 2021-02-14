package com.ticketmaster.eventpromoterapp.controler;


import com.google.gson.Gson;
import com.ticketmaster.eventpromoterapp.filters.FilterClass;
import com.ticketmaster.eventpromoterapp.model.Event;
import com.ticketmaster.eventpromoterapp.parser.ParserClass;
import com.ticketmaster.eventpromoterapp.stats.StatsGenerator;
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
    public static final String API_KEY="ZO7QX2Dsb2QEN1ryYVxYz9svpRlFLATx";

    //url to the ticketMaster API with the appended API_KEY
   public static final String HOST="http://app.ticketmaster.com/discovery/v2/events.json?apikey="+API_KEY;

    FilterClass filterClass=new FilterClass();
    StatsGenerator statsGenerator=new StatsGenerator();



    //this mapping ensures the method getStates() handles all the mapping requests to "/states"
    //gives statistics about each state. in our case just australia and new zealand are returned with their statistics
   //throws a exception because it won't compile otherwise due to many (risky) network requests
    @GetMapping("/stats/states")
    public String getStates() throws Exception{
        return statsGenerator.mergeStats();
    }

    //returns an event by its id
    @GetMapping("/event/{id}")
    public String getEventById(@PathVariable("id")String id) throws IOException, InterruptedException {
        return statsGenerator.getEventById(id);
    }


    /*returns events in australia within a defined start and end date. takes a s request body a hasMap who contains
    the start and end dates in String.
     */
    @GetMapping("/events/Australia/period")
    public String getAuEventsInTimeRange(@RequestBody HashMap<String,String> startEndDate) throws IOException, InterruptedException {
        return filterClass.filterAUEventsByTimeRange(startEndDate);
    }


    /*returns events in new zealand within a defined start and end date. takes a s request body a hasMap who contains
    the start and end dates in String.
     */
    @GetMapping("/events/New_Zealand/period")
    public String getNzEventsInTimeRange(@RequestBody HashMap<String,String> startEndDate) throws IOException, InterruptedException {
        return filterClass.filterNZEventsByTimeRange(startEndDate);
    }

    /*returns the event in the defined period with no respect to the country (state) ie. it returns events in the given period of
    australia and new zealand
     */
    @GetMapping("/events/period")
    String mergeEventPeriods(@RequestBody HashMap<String,String> startEndDate) throws IOException, InterruptedException {
        return filterClass.mergeEventPeriods(startEndDate);
    }

    /*
     mapping to filter the states stats with path variable state_name which is in ISO code format
     example: nz for New Zealand and au for  Australia.
     */
    @GetMapping("/stats/states/{state_name}")
    public String filterStatesStats(@PathVariable("state_name")String state) throws Exception {
        return filterClass.filterStatesStats(state);
    }

    /*
    since an id is unique across all the entire system, no need to precise which country to look for promoter
    because the id will provide explicit information about the events with the provided promoterId
    this mapping returns the event that are mentored (promoted) by this promoter.
     */
    @GetMapping("/events/promoter/{promoterId}")
    String getEventByPromoterId(@PathVariable("promoterId")String promoterId) throws IOException, InterruptedException {
        return filterClass.filterEventsByPromoterId(promoterId);
    }

    //return promoter stats by promoterId
    @GetMapping("/stats/promoter/{promoterId}")
    String getStatsByPromoterId(@PathVariable("promoterId")String promoterId) throws IOException, InterruptedException {
        return filterClass.filterStatsByPromoterId(promoterId);
    }

    /*
    since segment is considered to be the primary event genre, this mapping returns event by segment name in other words,
    this mapping filters events by event genre.
     */
    @GetMapping("/events/genre/{segment_name}")
    public String getEventsBySegmentName(@PathVariable("segment_name")String segmentName) throws IOException, InterruptedException {
        return filterClass.filterByEventGenre(segmentName);
    }


    }

