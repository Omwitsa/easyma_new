package Rest;

import java.util.ArrayList;

import model.ProductResp;
import model.Response;
import model.Supplier;
import model.SynchData;
import model.TCollection;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {
    @POST("webservice/users/productIntake")
    Call<Response> productIntake(@Body ArrayList<SynchData> intakes);
    @POST("webservice/users/transporterIntake")
    Call<Response> transporterIntake(@Body ArrayList<TCollection> collections);
    @GET("webservice/users/getItems")
    Call<ProductResp> getItems(@Query("saccoCode") String saccoCode);
    @POST("webservice/users/registerSupplier")
    Call<Response> registerSupplier(@Body Supplier Supplier);
    @GET("webservice/users/getSupplierNo")
    Call<Response> getSupplierNo();
}
