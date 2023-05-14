package com.example.ticket_center_client.requests;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import tuvarna.ticket_center_common.models.AssignDistributorModel;
import tuvarna.ticket_center_common.models.ChangePasswordModel;
import tuvarna.ticket_center_common.models.EventModel;
import tuvarna.ticket_center_common.models.LoginModel;
import tuvarna.ticket_center_common.models.PurchaseTicketModel;
import tuvarna.ticket_center_common.models.RatingModel;
import tuvarna.ticket_center_common.models.RegistrationModel;
import tuvarna.ticket_center_common.models.UserModel;
import tuvarna.ticket_center_common.models.VerificationModel;
import tuvarna.ticket_center_common.responses.CommonResponse;

public interface TicketCenterAPI {

    String API_URL = "http://10.0.2.2:8080";

    @PUT("users/login")
    Call<CommonResponse> login(@Body LoginModel loginModel);

    @POST("users/register")
    Call<CommonResponse> register(@Body RegistrationModel registrationModel);

    @PUT("users/verify")
    Call<CommonResponse> verify(@Body VerificationModel verificationModel);

    @PUT("users/change-password")
    Call<CommonResponse> changePassword(@Body ChangePasswordModel changePasswordModel);

    @PUT("users")
    Call<CommonResponse> editUser(@Body UserModel userModel);

    @GET("users")
    Call<CommonResponse> getUsers();

    @POST("events")
    Call<CommonResponse> createEvent(@Body EventModel eventModel);

    @GET("distributors")
    Call<CommonResponse> getDistributors();

    @POST("ratings")
    Call<CommonResponse> addRating(@Body RatingModel ratingModel);

    @GET("ratings/{email}")
    Call<CommonResponse> getRatings(@Path("email") String email);

    @GET("events/{email}")
    Call<CommonResponse> getEvents(@Path("email") String email);

    @GET("distributors/assigned/{eventId}")
    Call<CommonResponse> getAssignedDistributors(@Path("eventId") Long eventId);

    @GET("distributors/free/{eventId}")
    Call<CommonResponse> getFreeDistributors(@Path("eventId") Long eventId);

    @POST("distributors/assign")
    Call<CommonResponse> assignDistributor(@Body AssignDistributorModel assignDistributorModel);

    @GET("distributors/{email}")
    Call<CommonResponse> getDistributor(@Path("email") String email);

    @GET("events/tickets/{eventId}/{ticketKindName}")
    Call<CommonResponse> getTickets(@Path("eventId") Long eventId, @Path("ticketKindName") String ticketKindName);

    @POST("events/tickets/check-in/{ticketId}")
    Call<CommonResponse> checkInTicket(@Path("ticketId") Long ticketId);

    @DELETE("events/tickets/check-out/{ticketId}")
    Call<CommonResponse> checkOutTicket(@Path("ticketId") Long ticketId);

    @POST("events/tickets/purchase")
    Call<CommonResponse> purchaseTicket(@Body PurchaseTicketModel purchaseTicketModel);

    @GET("events/organizer/{email}")
    Call<CommonResponse> getEventsByOrganizer(@Path("email") String email);
}
