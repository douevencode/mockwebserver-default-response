import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface CatsServerApi {

    @POST("cats/images")
    fun sendCatImages(@Body images: List<CatImage>): Call<Void>

    @GET("cats/images")
    fun getCatImages(): Call<List<CatImage>>
}