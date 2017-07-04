package Order

import com.jayway.restassured.RestAssured.given
import com.jayway.restassured.builder.RequestSpecBuilder
import com.jayway.restassured.filter.log.RequestLoggingFilter
import com.jayway.restassured.filter.log.ResponseLoggingFilter
import com.jayway.restassured.specification.RequestSpecification
import org.junit.BeforeClass
import Authenticator
import Helper.resourceBundle
import org.hamcrest.Matchers
import org.junit.Test

/**
 * Created by Roman on 03.07.2017.
 */

class PermissionsTest {
    private val apiSecretNoTradingKey = resourceBundle.getString("apiSecretNoTrading")

    companion object {
        lateinit var spec: RequestSpecification
        @BeforeClass
        @JvmStatic fun initSpec() {
            spec = RequestSpecBuilder()
                    .setBaseUri("https://dsx.uk/tapi/v2/order")
                    .addHeader("Key", resourceBundle.getString("apiPublicNoTrading"))
                    .addFilter(ResponseLoggingFilter())
                    .addFilter(RequestLoggingFilter())
                    .build()
        }
    }

    // WHY return code is not 403?
    @Test
    fun CreatingNewOrderWithoutPermissionFails() {
        val params = hashMapOf(
                "type" to "buy",
                "rate" to 420,
                "volume" to 1,
                "pair" to "btcusd",
                "orderType" to "market",
                "nonce" to System.currentTimeMillis())
        given()
                .spec(spec)
                .header("Sign", Authenticator.getSignature(apiSecretNoTradingKey, params))
                .params(params)
        .`when`()
                .post("/new")
        .then()
                .statusCode(200)
        .assertThat()
                .body("success", Matchers.equalTo(0))
    }

    // WHY return code is not 403?
    @Test
    fun CancellingOrderWithoutPermissionFails() {
        val params = hashMapOf(
                "orderId" to CreateNewOrder(basicParams),
                "nonce" to System.currentTimeMillis())
        given()
                .spec(spec)
                .header("Sign", Authenticator.getSignature(apiSecretNoTradingKey, params))
                .params(params)
        .`when`()
                .post("/cancel")
        .then()
                .statusCode(200)
        .assertThat()
                .body("success", Matchers.equalTo(0))
    }

    @Test
    fun GettingOrderStatusWithoutPermissionOK() {
        val validOrderId = 9009797
        val params = hashMapOf("orderId" to validOrderId, "nonce" to System.currentTimeMillis())
        given()
                .spec(spec)
                .header("Sign", Authenticator.getSignature(apiSecretNoTradingKey, params))
                .params(params)
        .`when`()
                .post("/status")
        .then()
                .statusCode(200)
        .assertThat()
                .body("success", Matchers.equalTo(1))
    }

}