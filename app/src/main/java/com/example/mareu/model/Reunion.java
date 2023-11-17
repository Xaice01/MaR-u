package com.example.mareu.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

/**
 * Model object representing a Reunion
 */
public class Reunion {

    /**
     * Identifier
     */
    private Long id;

    /**
     * Name
     */
    private String name;

    /**
     * Date and Hour
     */
    private Calendar date;

    /**
     * Duration in minute
     */
    private long duration;

    /**
     * venue
     */
    private Salle venue;

    /**
     * list of email_Person
     */
    private List<String> email_Person;


    /**
     * Constructor
     *
     * @param id
     * @param name
     * @param date
     * @param duration
     * @param venue
     * @param email_Person
     */
    public Reunion(Long id, String name, Calendar date, long duration, Salle venue, List<String> email_Person) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.duration = duration;
        this.venue = venue;
        this.email_Person = email_Person;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public Salle getVenue() {
        return venue;
    }

    public void setVenue(Salle venue) {
        this.venue = venue;
    }

    public List<String> getEmail_Person() {
        return email_Person;
    }

    public void setEmail_Person(List<String> email_Person) {
        this.email_Person = email_Person;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reunion reunion = (Reunion) o;
        return duration == reunion.duration && id.equals(reunion.id) && name.equals(reunion.name) && date.equals(reunion.date) && venue.equals(reunion.venue) && email_Person.equals(reunion.email_Person);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, date, duration, venue, email_Person);
    }
}
