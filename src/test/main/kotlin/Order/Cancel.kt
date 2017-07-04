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
    @Test
    fun createdOrderCancelOK() {
        val params = hashMapOf(
                "type" to "buy",
                "rate" to 1000,
                "volume" to 1,
                "pair" to "btcusd",
                "orderType" to "limit",
                "nonce" to System.currentTimeMillis())
        DoRequestAndAssertResult(CreateNewOrder(params), 1)
    }

    private fun DoRequestAndAssertResult(orderId : Any?, expectedSuccess : Int) {
        val params = hashMapOf("orderId" to orderId, "nonce" to System.currentTimeMillis())
        RestAssured.given()
                .spec(Helper.spec)
                .header("Sign", Authenticator.getSignature(resourceBundle.getString("apiSecret"), params))
                .params(params)
        .`when`()
                .post("/cancel")
        .then()
                .statusCode(200)
        .assertThat()
                .body("success", Matchers.equalTo(expectedSuccess))
    }
}