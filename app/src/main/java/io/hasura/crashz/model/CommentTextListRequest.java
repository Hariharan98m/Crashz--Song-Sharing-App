package io.hasura.crashz.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by HARIHARAN on 11-07-2017.
 */

public class CommentTextListRequest {

    @SerializedName("type")
    String type;

    @SerializedName("args")
    Args args;

    public CommentTextListRequest(String type, String table, int song_id, int user_id, String comment_text, String user_name) {
        this.type = type;
        this.args = new Args(table, song_id, user_id, comment_text, user_name);

    }

    private class Args{

        @SerializedName("where")
        ObjectId where;

        @SerializedName("table")
        String table;

        @SerializedName("columns")
        String[] col;

        @SerializedName("objects")
        Objects[] objects;

        public Args(String table, int song_id, int user_id, String comment_text, String user_name) {
            if(type=="select") {
                this.col = new String[]{"*"};
                this.table = table;
                where = new ObjectId(song_id);
            }
            else if(type=="insert"){
                this.table=table;
                this.objects=new Objects[1];
                objects[0]= new Objects(song_id, user_id, comment_text, user_name);
            }
        }


        private class ObjectId {
            @SerializedName("song_id")
            int song_id;

            public ObjectId(int id) {
                this.song_id = id;
            }
        }

        private class Objects{
            @SerializedName("comment_text")
            String comment_text;

            @SerializedName("song_id")
            int song_id;

            @SerializedName("user_id")
            int user_id;

            @SerializedName("user_name")
            String user_name;

            public Objects(int song_id, int user_id, String comment_text, String user_name) {
                this.comment_text = comment_text;
                this.song_id = song_id;
                this.user_id = user_id;
                this.user_name = user_name;
            }
        }

    }
}
