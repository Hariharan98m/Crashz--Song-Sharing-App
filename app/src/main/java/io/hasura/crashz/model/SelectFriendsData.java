package io.hasura.crashz.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by HARIHARAN on 07-07-2017.
 */

public class SelectFriendsData {

    @SerializedName("type")
    String type;

    @SerializedName("args")
    Args args;

    public SelectFriendsData(String type, String table, int id) {
        this.type = type;
        this.args = new Args(table, id);
    }

    private class Args{

        @SerializedName("where")
        ObjectId where;

        @SerializedName("table")
        String table;

        @SerializedName("columns")
        String[] col;


        public Args(String table, int id) {
            this.col=new String[]{"*"};
            this.table= table;
            where= new ObjectId(id);
        }


        private class ObjectId {
            @SerializedName("your_id")
            int id;


            public ObjectId(int id) {
                this.id = id;
            }
        }

    }
}
