package io.hasura.crashz.network;

import java.util.List;

import io.hasura.crashz.model.AuthenticationRequest;
import io.hasura.crashz.model.BitmapResponse;
import io.hasura.crashz.model.CommentTextListRequest;
import io.hasura.crashz.model.DPSelect;
import io.hasura.crashz.model.DataHandlingResponse;
import io.hasura.crashz.model.DpInsert;
import io.hasura.crashz.model.ErrorResponse;
import io.hasura.crashz.model.FriendSongsList;
import io.hasura.crashz.model.HomePageFriendsReqORConfirm;
import io.hasura.crashz.model.InsertIntoRequestORConfirm;
import io.hasura.crashz.model.InsertLikeModelRequest;
import io.hasura.crashz.model.InsertORUpdateIntoUserTable;
import io.hasura.crashz.model.MessageResponse;
import io.hasura.crashz.model.SelectFriendsData;
import io.hasura.crashz.model.SelectFriendsSongsRequest;
import io.hasura.crashz.model.SongComments;
import io.hasura.crashz.model.SongInsert;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by HARIHARAN on 27-06-2017.
 */

public interface ApiInterface {

    @POST(NetworkURL.LOGIN_URL)
    Call<MessageResponse> login(@Body AuthenticationRequest body);

    @POST(NetworkURL.REGISTER)
    Call<MessageResponse> register(@Body AuthenticationRequest body);

    @POST(NetworkURL.MOB_CONFIRM)
    Call<MessageResponse> mconfirm(@Body AuthenticationRequest body);

    @GET(NetworkURL.USER_ACCOUNT_INFO)
    Call<MessageResponse> user_account_info();

    @POST(NetworkURL.QUERY)
    Call<DataHandlingResponse> insert_or_update(@Body InsertORUpdateIntoUserTable body);

    @POST(NetworkURL.QUERY)
    Call<DataHandlingResponse> insert_into_request_or_confirm(@Body InsertIntoRequestORConfirm body);

    @POST(NetworkURL.QUERY)
    Call<List<HomePageFriendsReqORConfirm>> select(@Body SelectFriendsData body);

    @POST(NetworkURL.QUERY)
    Call<List<FriendSongsList>> get_songs_for_this_friend(@Body SelectFriendsSongsRequest body);

    @POST(NetworkURL.QUERY)
    Call<List<SongComments>> get_comments_for_this_song(@Body CommentTextListRequest body);

    @POST(NetworkURL.QUERY)
    Call<DataHandlingResponse> insert_comments_for_this_song(@Body CommentTextListRequest body);

    @POST(NetworkURL.QUERY)
    Call<DataHandlingResponse> insert_song(@Body SongInsert body);

    @GET(NetworkURL.LOGOUT)
    Call<ErrorResponse> logout();

    @POST(NetworkURL.QUERY)
    Call<DataHandlingResponse> insert_like(@Body InsertLikeModelRequest body);

    @POST(NetworkURL.QUERY)
    Call<DataHandlingResponse> delete_like(@Body InsertLikeModelRequest body);



    @POST(NetworkURL.QUERY)
    Call<DataHandlingResponse> insert_dp(@Body DpInsert body);

    @POST(NetworkURL.QUERY)
    Call<List<BitmapResponse>> select_dp_link(@Body DPSelect body);


    //@POST(NetworkURL.ARTICLES)
    //Call<List<ArticlesResponse>> fetch_articles(@Body ArticleListRequest body);
}
