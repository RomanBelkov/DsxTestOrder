import Helper.resourceBundle
import Helper.spec
import com.jayway.restassured.RestAssured.given
import org.hamcrest.Matchers.equalTo
import org.junit.Test

class StatusTest {
    private val validOrderId = 9009797

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
                .header("Sign", Authenticator.getSignature(resourceBundle.getString("apiSecret"), params))
                .params(params)
        .`when`()
                .post("/status")
        .then()
                .statusCode(200)
        .assertThat()
                .body("success", equalTo(expectedSuccess))
    }
}