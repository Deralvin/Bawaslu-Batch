package id.pptik.bawaslubatch.networks;
import id.pptik.bawaslubatch.networks.POJO.DataItem;
import id.pptik.bawaslubatch.networks.POJO.Response;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;



public interface RestServiceInterface {


    @Headers("Content-Type: application/x-www-form-urlencoded")
    @GET("Pemilu/32/report/TOC.json")
    Call<Response> dataItem();


}