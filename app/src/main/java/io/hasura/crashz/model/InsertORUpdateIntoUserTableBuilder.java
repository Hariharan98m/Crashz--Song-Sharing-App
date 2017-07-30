package io.hasura.crashz.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by HARIHARAN on 04-07-2017.
 */

public class InsertORUpdateIntoUserTableBuilder {

    String type;
    String table;

    int id;
    String description;
    String city;
    String work;
    String passion_with_music;
    String prof_name;
    String music;

    public InsertORUpdateIntoUserTableBuilder setTypeTable(String type, String table) {
        this.type = type;
        this.table=table;
        return this;
    }

    public InsertORUpdateIntoUserTableBuilder setId(int id) {
        this.id = id;
        return this;
    }

    public InsertORUpdateIntoUserTableBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    public InsertORUpdateIntoUserTableBuilder setCity(String city) {
        this.city = city;
        return this;
    }

    public InsertORUpdateIntoUserTableBuilder setWork(String work) {
        this.work = work;
        return this;
    }

    public InsertORUpdateIntoUserTableBuilder setPassion_with_music(String passion_with_music) {
        this.passion_with_music = passion_with_music;
        return this;
    }

    public InsertORUpdateIntoUserTableBuilder setProf_name(String prof_name) {
        this.prof_name = prof_name;
        return this;
    }

    public InsertORUpdateIntoUserTable build(){
         return new InsertORUpdateIntoUserTable(type,table,id,description,city,work,passion_with_music,prof_name,music);
    }

    public InsertORUpdateIntoUserTableBuilder setMusic(String music) {
        this.music = music;
        return this;
    }
}
