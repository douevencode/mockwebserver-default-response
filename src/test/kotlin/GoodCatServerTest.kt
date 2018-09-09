
import Breed.BRITISH
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.QueueDispatcher
import org.junit.After
import org.junit.Before
import org.junit.Test

class GoodCatServerTest {

    val server = MockWebServer()

    lateinit var catsServer: BlockingCatsServer

    @Before
    fun setUp() {
        server.start()
        BlockingCatsServer.URL = server.url("").toString()
        catsServer = BlockingCatsServer()

        val queueDispatcher = QueueDispatcher()
        queueDispatcher.setFailFast(MockResponse())
        server.setDispatcher(queueDispatcher)
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
    fun `sending cat images and then retrieving these images`() {
        catsServer.sendCatImages(listOf(CatImage("google.com/cat.jpg", BRITISH)))
        assert(server.takeRequest().path.endsWith("cats/images"))

        catsServer.sendCatImages(listOf(CatImage("google.com/cat.jpg", BRITISH)))
        assert(server.takeRequest().path.endsWith("cats/images"))

        server.enqueue(MockResponse().setBody("""
            [{"url":"google.com/cat.jpg","breed":"BRITISH"}]
            """))

        catsServer.getCatImages()
    }
}