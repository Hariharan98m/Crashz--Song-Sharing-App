package io.hasura.crashz.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by HARIHARAN on 16-07-2017.
 */

public class DpInsert {
    @SerializedName("type")
    String type;

    @SerializedName("args")
    Args args;

    public DpInsert(String type, String table, int id,String link) {
            this.type = type;
            this.args= new Args(table, id, link);
    }

        class Args {
            @SerializedName("table")
            String table;

            @SerializedName("objects")
            Object[] objects;

            @SerializedName("$set")
            Object set;

            @SerializedName("where")
            ObjectWhere where;

            public Args(String table, int id,String link) {
                this.table = table;
                if(type=="update"){
                    this.set=new Object(id,link);
                    this.where= new ObjectWhere(id);
                }
                else {
                    this.objects = new Object[1];
                    this.objects[0] = new Object(id, link);
                }
            }

            class ObjectWhere{
                @SerializedName("user_id")
                int id;

                public ObjectWhere(int id) {
                    this.id = id;
                }
            }

            class Object {
                @SerializedName("user_id")
                int id;

                @SerializedName("profile_image_link")
                String link;

                public Object(int id, String link) {
                    this.id= id;
                    this.link=link;
                }
            }
        }
    }
