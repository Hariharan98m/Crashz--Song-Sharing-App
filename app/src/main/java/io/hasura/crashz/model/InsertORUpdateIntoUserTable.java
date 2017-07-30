package io.hasura.crashz.model;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

import static android.R.attr.id;

/**
 * Created by HARIHARAN on 30-06-2017.
 */

public class InsertORUpdateIntoUserTable {
    @SerializedName("type")
    String type;

    @SerializedName("args")
    Args args;

    public InsertORUpdateIntoUserTable(String type, String table, int id, String description, String city, String work, String passion_with_music, String prof_name, String music) {
        this.type = type;
        args=new Args(table, id, description,  city,  work,  passion_with_music,  prof_name, music);
    }

    public class Args{
        @SerializedName("table")
        String table;

        @SerializedName("objects")
        Objects[] object;

        @SerializedName("columns")
        String[] columns;

        @SerializedName("$set")
        Objects set;

        @SerializedName("where")
        ObjectId where;

        public Args(String table, int id, String description, String city, String work, String passion_with_music, String prof_name, String music) {
            this.table=table;

            if(type=="insert"){
                this.object= new Objects[1];
                this.object[0]= new Objects(id, description, city, work, passion_with_music, prof_name,music);
            }
            else if (type=="update"){
                this.set=new Objects(id, description, city, work, passion_with_music, prof_name,music);
                this.where= new ObjectId(id);
            }
        }

        private class ObjectId {

            @SerializedName("id")
            int user_id;

            public ObjectId(int user_id) {
                this.user_id = user_id;
            }
        }

        public class Objects{
            @SerializedName("id")
            public
            int id;

            @SerializedName("description")
            public
            String description;

            @SerializedName("city")
            public
            String city;

            @SerializedName("work")
            public
            String work;

            @SerializedName("passion_with_music")
            public
            String passion_with_music;

            @SerializedName("prof_name")
            public
            String prof_name;

            @SerializedName("music_style")
            public
            String music;

            public Objects(int id, String description, String city, String work, String passion_with_music, String prof_name, String music) {
                this.id = id;
                this.description = description;
                this.city = city;
                this.work = work;
                this.passion_with_music = passion_with_music;
                this.prof_name = prof_name;
                this.music=music;
            }

        }

    }
}
