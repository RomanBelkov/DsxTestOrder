package Order

import com.jayway.restassured.RestAssured.*
import com.jayway.restassured.builder.RequestSpecBuilder
import com.jayway.restassured.filter.log.RequestLoggingFilter
import com.jayway.restassured.filter.log.ResponseLoggingFilter
import java.util.*

import Authenticator

fun CreateNewOrder(): Number? {
    val resourceBundle = ResourceBundle.getBundle("keys")
    val params = hashMapOf(
            "type" to "buy",
            "rate" to 3000,
            "volume" to 1,
            "pair" to "btcusd",
            "orderType" to "market",
            "nonce" to System.currentTimeMillis())
    val orderId =
    given()
            .spec(RequestSpecBuilder()
                    .setBaseUri("https://dsx.uk/tapi/v2/order")
                    .addHeader("Key", resourceBundle.getString("apiPublic"))
                    .addFilter(ResponseLoggingFilter())
                    .addFilter(RequestLoggingFilter())
                    .build())
            .header("Sign", Authenticator().getSignature(resourceBundle.getString("apiSecret"), params))
            .params(params)
    .`when`()
            .post("/new")
    .then()
            .statusCode(200)
    .extract()
            .path<Number>("return.orderId")
    return orderId
}