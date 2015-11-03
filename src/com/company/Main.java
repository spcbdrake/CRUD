package com.company;

import spark.ModelAndView;
import spark.Session;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) {
        ArrayList<Place> places = new ArrayList();

        Spark.get(
                "/",
                ((request, response) -> {
                    Session session = request.session();
                    String username = session.attribute("username");
                    if (username == null) {
                        return new ModelAndView(new HashMap(), "login.html");
                    }
                    HashMap m = new HashMap();
                    m.put("username", username);
                    m.put("places", places);
                    return new ModelAndView(m, "loggedin.html");
                }),
                new MustacheTemplateEngine()
        );


        Spark.post(
                "/login",
                ((request, response) -> {
                    String username =  request.queryParams("username");
                    Session session = request.session();
                    session.attribute("username", username);
                    response.redirect("/");
                    return "";
                })
        );

        Spark.post(
                "/add-place",
                ((request, response) -> {
                    Place place = new Place();
                    place.id = places.size() +1;
                    place.location = request.queryParams("location");
                    place.year = request.queryParams("year");
                    places.add(place);
                    response.redirect("/");
                    return "";
                })
        );

        Spark.post(
                "/delete-location",
                ((request, response) -> {
                    String id = request.queryParams("locid");
                    try {
                        int idNum = Integer.valueOf(id);
                        places.remove(idNum-1);
                        for (int i = 0; i < places.size(); i ++) {
                            places.get(i).id= i+1;
                        }
                    } catch (Exception e) {

                    }
                    response.redirect("/");
                    return "";
                })
        );

    }
}
