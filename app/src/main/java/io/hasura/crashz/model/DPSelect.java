package io.hasura.crashz.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by HARIHARAN on 16-07-2017.
 */

public class DPSelect {
        @SerializedName("type")
        String type;

        @SerializedName("args")
        Args args;

        public DPSelect(String type, String table, int id) {
            this.type = type;
            this.args= new Args(table, id);
        }

        class Args {
            @SerializedName("table")
            String table;

            @SerializedName("columns")
            String[] columns;

            @SerializedName("where")
            Object where;


            public Args(String table, int id) {
                this.table = table;
                this.columns = new String[]{"*"};
                this.where= new Object(id);
            }

            class Object{
                @SerializedName("user_id")
                int user_id;

                public Object(int id){
                    this.user_id=id;
                }
            }
        }
}
