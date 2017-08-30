package Order

import Authenticator
import Helper.resourceBundle
import com.jayway.restassured.RestAssured
import org.hamcrest.Matchers
import org.junit.Test

/**
 * Created by romanbelkov on 04/07/2017.
 */

class CancelTest {
    private val correctNewOrderParams = hashMapOf(
            "type" to "buy",
            "rate" to 100,
            "volume" to 1,
            "pair" to "bccusd",
            "orderType" to "limit",
            "nonce" to System.currentTimeMillis())

    @Test
    fun CreatedValidOrderCancelOK() {
        DoRequestAndAssertResult(CreateNewOrder(correctNewOrderParams), 1)
    }

    @Test
    fun CreatedValidOrderInStringCancelOK() {
        DoRequestAndAssertResult(CreateNewOrder(correctNewOrderParams).toString(), 1)
    }

    @Test
    fun CreatedValidOrderInFloatCancelFail() {
        DoRequestAndAssertResult(CreateNewOrder(correctNewOrderParams)?.toFloat(), 0)
    }

    @Test
    fun InvalidNegativeOrderIdCancelFail() {
        val invalidOrderId = -1
        DoRequestAndAssertResult(invalidOrderId, 0)
    }

    @Test
    fun InvalidFloatOrderIdCancelFail() {
        val invalidOrderId = 1.1
        DoRequestAndAssertResult(invalidOrderId, 0)
    }

    @Test
    fun InvalidBigOrderIdCancelFail() {
        val invalidOrderId = System.nanoTime()
        DoRequestAndAssertResult(invalidOrderId, 0)
    }

    @Test
    fun InvalidNullOrderIdCancelFail() {
        val invalidOrderId = null
        DoRequestAndAssertResult(invalidOrderId, 0)
    }

    @Test
    fun InvalidStringOrderIdCancelFail() {
        val invalidOrderId = "abc"
        DoRequestAndAssertResult(invalidOrderId, 0)
    }

    @Test
    fun InvalidEmptyStringOrderIdCancelFail() {
        val invalidOrderId = ""
        DoRequestAndAssertResult(invalidOrderId, 0)
    }

    private fun DoRequestAndAssertResult(orderId : Any?, expectedSuccess : Int) {
        val params = hashMapOf("orderId" to orderId, "nonce" to System.currentTimeMillis())
        RestAssured.given()
                .spec(Helper.spec)
                .header("Sign", Authenticator.getSignature(resourceBundle.getString("apiSecret"), params))
                .params(params)
        .`when`()
                .post("/order/cancel")
        .then()
                .statusCode(200)
        .assertThat()
                .body("success", Matchers.equalTo(expectedSuccess))
    }
}