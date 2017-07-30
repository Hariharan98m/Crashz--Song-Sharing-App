package io.hasura.crashz.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by HARIHARAN on 09-07-2017.
 */

public class InsertIntoRequestORConfirm {
    @SerializedName("type")
    String type;

    @SerializedName("args")
    Args args;

    public InsertIntoRequestORConfirm(String type, String table, int user_id, int friend_id, boolean request, boolean confirm) {
        this.type = type;
        args = new InsertIntoRequestORConfirm.Args(type, table, user_id, friend_id, request, confirm);
    }

    public class Args {
        @SerializedName("table")
        String table;

        @SerializedName("objects")
        Objects[] object;

        @SerializedName("$set")
        Objects set;

        @SerializedName("where")
        ObjectId where;

        public Args(String type, String table, int user_id, int friend_id, boolean request, boolean confirm) {
            this.table = table;
            if(type=="insert") {
                this.object = new Objects[1];
                this.object[0] = new Objects(user_id, friend_id, request, confirm);
            }
            else{
                set = new Objects(user_id, friend_id,request, confirm);
                where= new ObjectId(user_id, friend_id);
            }
        }

        class ObjectId{
            @SerializedName("user_id")
            int user_id;

            @SerializedName("friend_id")
            int friend_id;

            public ObjectId(int user_id, int friend_id) {
                this.user_id = user_id;
                this.friend_id=friend_id;
            }
        }
        class Objects {
            @SerializedName("user_id")
            int user_id;

            @SerializedName("friend_id")
            int friend_id;

            @SerializedName("request")
            boolean request;

            @SerializedName("confirm")
            boolean confirm;

            public Objects(int user_id, int friend_id, boolean request, boolean confirm) {
                this.user_id = user_id;
                this.friend_id = friend_id;
                this.request = request;
                this.confirm = confirm;
            }

        }
    }
}
