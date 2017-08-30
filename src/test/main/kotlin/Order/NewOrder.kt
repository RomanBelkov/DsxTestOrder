package Order

import com.jayway.restassured.RestAssured.*

import Authenticator
import Helper.resourceBundle
import Helper.spec

val basicParams = hashMapOf(
        "type" to "buy",
        "rate" to 100,
        "volume" to 1,
        "pair" to "bccusd",
        "orderType" to "limit",
        "nonce" to System.currentTimeMillis())

fun CreateNewOrder(params : Map<String, Any?>): Number? {
    return given()
        .spec(spec)
        .header("Sign", Authenticator.getSignature(resourceBundle.getString("apiSecret"), params))
        .params(params)
    .`when`()
        .post("/order/new")
    .then()
        .statusCode(200)
    .extract()
        .path<Number>("return.orderId")
}