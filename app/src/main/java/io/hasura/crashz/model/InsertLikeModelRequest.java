package io.hasura.crashz.model;

import com.google.gson.annotations.SerializedName;

import static android.R.attr.id;

/**
 * Created by HARIHARAN on 12-07-2017.
 */

public class InsertLikeModelRequest {
    @SerializedName("type")
    String type;

    @SerializedName("args")
    Args args;

    public InsertLikeModelRequest(String type, String table, int user_id,int song_id, boolean like) {
        this.type = type;
        this.args = new Args(table, user_id, song_id, like);
    }

    private class Args{

        @SerializedName("where")
        Objects where;

        @SerializedName("table")
        String table;

        @SerializedName("objects")
        Objects[] objects;

        public Args(String table, int user_id, int song_id, boolean like) {
            if(type=="insert"&&like==true) {
                this.table = table;
                objects= new Objects[1];
                this.objects[0]=new Objects(user_id, song_id);
            }
            else if (type=="delete"&&like==false){
                this.table = table;
                where = new Objects(user_id, song_id);
            }
        }

        private class Objects {

            public Objects(int user_id, int song_id) {
                this.song_id = song_id;
                this.user_id = user_id;
            }

            @SerializedName("song_id")
            int song_id;

            @SerializedName("user_id")
            int user_id;
        }

    }

}
