import com.jayway.restassured.RestAssured.given
import com.jayway.restassured.builder.RequestSpecBuilder
import com.jayway.restassured.filter.log.RequestLoggingFilter
import com.jayway.restassured.filter.log.ResponseLoggingFilter
import com.jayway.restassured.specification.RequestSpecification
import org.junit.*
import java.util.*

class StatusTest {
    private val authenticator = Authenticator()

    companion object {
        private val resourceBundle = ResourceBundle.getBundle("keys")
        lateinit var spec: RequestSpecification
        @BeforeClass
        @JvmStatic fun initSpec() {
            spec = RequestSpecBuilder()
                    .setBaseUri("https://dsx.uk/tapi/v2/order/status")
                    .addHeader("Key", resourceBundle.getString("apiPublic"))
                    .addFilter(ResponseLoggingFilter())
                    .addFilter(RequestLoggingFilter())
                    .build()
        }
    }

    @Test
    fun createdOrderOK() {
        val validOrderId = 9009797
        val params = hashMapOf<String, Number>("orderId" to validOrderId, "nonce" to System.nanoTime())
        given()
                .spec(spec)
                .header("Sign", authenticator.getSignature(params))
                .params(params)
        .`when`()
                .post()
        .then()
                .statusCode(200)
    }

    @Test
    fun InvalidOrder() {
        val validOrderId = -1
        val params = hashMapOf<String, Number>("orderId" to validOrderId, "nonce" to System.nanoTime())
        given()
                .spec(spec)
                .header("Sign", authenticator.getSignature(params))
                .params(params)
        .`when`()
                .post()
        .then()
                .statusCode(200)
    }
}