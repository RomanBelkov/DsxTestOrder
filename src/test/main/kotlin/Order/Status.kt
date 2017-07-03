import com.jayway.restassured.RestAssured.given
import com.jayway.restassured.builder.RequestSpecBuilder
import com.jayway.restassured.filter.log.RequestLoggingFilter
import com.jayway.restassured.filter.log.ResponseLoggingFilter
import com.jayway.restassured.specification.RequestSpecification
import org.hamcrest.Matchers.*
import org.junit.*
import java.util.*

class StatusTest {
    private val authenticator = Authenticator()
    private val validOrderId = 9009797

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
        DoRequestAndAssertResult(validOrderId, 1)
    }

    @Test
    fun ValidOrderIdInString() {
        DoRequestAndAssertResult(validOrderId.toString(), 1)
    }

    // WHY return code is not 400?
    @Test
    fun ValidOrderIdInFloat() {
        DoRequestAndAssertResult(validOrderId.toFloat(), 0)
    }

    // WHY return code is not 400?
    @Test
    fun InvalidNegativeOrderId() {
        val invalidOrderId = -1
        DoRequestAndAssertResult(invalidOrderId, 0)
    }

    // WHY return code is not 400?
    @Test
    fun InvalidFloatOrderId() {
        val invalidOrderId = 1.1
        DoRequestAndAssertResult(invalidOrderId, 0)
    }

    // WHY return code is not 400?
    @Test
    fun InvalidBigOrderId() {
        val invalidOrderId = System.nanoTime()
        DoRequestAndAssertResult(invalidOrderId, 0)
    }

    // WHY return code is not 400?
    @Test
    fun InvalidNullOrderId() {
        val invalidOrderId = null
        DoRequestAndAssertResult(invalidOrderId, 0)
    }

    // WHY return code is not 400?
    @Test
    fun InvalidStringOrderId() {
        val invalidOrderId = "abc"
        DoRequestAndAssertResult(invalidOrderId, 0)
    }

    // WHY return code is not 400?
    @Test
    fun InvalidEmptyStringOrderId() {
        val invalidOrderId = ""
        DoRequestAndAssertResult(invalidOrderId, 0)
    }

    private fun DoRequestAndAssertResult(orderId : Any?, expectedSuccess : Int) {
        val params = hashMapOf("orderId" to orderId, "nonce" to System.currentTimeMillis())
        given()
                .spec(spec)
                .header("Sign", authenticator.getSignature(resourceBundle.getString("apiSecret"), params))
                .params(params)
        .`when`()
                .post()
        .then()
                .statusCode(200)
        .assertThat()
                .body("success", equalTo(expectedSuccess))
    }
}