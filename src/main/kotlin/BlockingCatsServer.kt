import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class BlockingCatsServer {

    companion object {
        var URL = "http://localhost"
    }

    private val catsServerApi = Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CatsServerApi::class.java)

    fun getCatImages() = catsServerApi.getCatImages().execute()

    fun sendCatImages(images: List<CatImage>) = catsServerApi.sendCatImages(images).execute()
}