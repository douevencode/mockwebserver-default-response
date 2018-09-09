
import Breed.BRITISH
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.After
import org.junit.Before
import org.junit.Test

class BadCatServerTest {

    val server = MockWebServer()

    lateinit var catsServer: BlockingCatsServer

    @Before
    fun setUp() {
        server.start()
        BlockingCatsServer.URL = server.url("").toString()
        catsServer = BlockingCatsServer()
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun `sending cat images has correct url`() {
        catsServer.sendCatImages(listOf(CatImage("google.com/cat.jpg", BRITISH)))

        assert(server.takeRequest().path.endsWith("cats/images"))
    }

    @Test
    fun `sending cat images has correct url every time`() {
        server.enqueue(MockResponse())

        catsServer.sendCatImages(listOf(CatImage("google.com/cat.jpg", BRITISH)))
        assert(server.takeRequest().path.endsWith("cats/images"))

        catsServer.sendCatImages(listOf(CatImage("google.com/cat.jpg", BRITISH)))
        assert(server.takeRequest().path.endsWith("cats/images"))
    }

    @Test
    fun `sending cat images and then retrieving these images`() {
        val dispatcher = object : Dispatcher() {

            val defaultResponse = MockResponse()

            var response: MockResponse? = null

            override fun dispatch(request: RecordedRequest?): MockResponse {
                return if (response == null) defaultResponse else response!!
            }
        }
        server.setDispatcher(dispatcher)

        catsServer.sendCatImages(listOf(CatImage("google.com/cat.jpg", BRITISH)))
        assert(server.takeRequest().path.endsWith("cats/images"))

        catsServer.sendCatImages(listOf(CatImage("google.com/cat.jpg", BRITISH)))
        assert(server.takeRequest().path.endsWith("cats/images"))

        dispatcher.response = MockResponse().setBody("""
            [{"url":"google.com/cat.jpg","breed":"BRITISH"}]
            """)
        catsServer.getCatImages()
    }

}