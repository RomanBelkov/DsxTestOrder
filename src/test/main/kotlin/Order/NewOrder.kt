package Order

import com.jayway.restassured.RestAssured.*

import Authenticator
import Helper.resourceBundle
import Helper.spec

val basicParams = hashMapOf(
        "type" to "buy",
        "rate" to 1000,
        "volume" to 1,
        "pair" to "btcusd",
        "orderType" to "limit",
        "nonce" to System.currentTimeMillis())

fun CreateNewOrder(params : Map<String, Any?>): Number? {
    val orderId =
    given()
            .spec(spec)
            .header("Sign", Authenticator.getSignature(resourceBundle.getString("apiSecret"), params))
            .params(params)
    .`when`()
            .post("/new")
    .then()
            .statusCode(200)
    .extract()
            .path<Number>("return.orderId")
    return orderId
}