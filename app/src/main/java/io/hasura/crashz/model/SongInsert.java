package io.hasura.crashz.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by HARIHARAN on 10-07-2017.
 */

public class SongInsert {
    @SerializedName("type")
    String type;

    @SerializedName("args")
    Args args;

    public SongInsert(String type, String table, String song_name, String composer_name, String song_link, int id) {
        this.type = type;
        this.args= new Args(table, song_name, composer_name,song_link, id);
    }

    class Args {
        @SerializedName("table")
        String table;

        @SerializedName("objects")
        Object[] objects;

        public Args(String table, String song_name, String composer_name, String song_link, int id) {
            this.table = table;
            this.objects = new Object[1];
            this.objects[0]=new Object(song_name, composer_name,song_link, id);
        }

        class Object {
            @SerializedName("composer_name")
            String composer_name;

            @SerializedName("song_name")
            String song_name;

            @SerializedName("song_link")
            String song_link;

            @SerializedName("user_id")
            int user_id;

            public Object(String song_name, String composer_name, String song_link, int id) {
                this.composer_name = composer_name;
                this.song_name = song_name;
                this.song_link = song_link;
                this.user_id= id;
            }
        }
    }
}