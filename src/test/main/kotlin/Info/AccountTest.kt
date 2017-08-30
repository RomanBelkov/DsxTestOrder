package Info

import com.jayway.restassured.RestAssured.*

import Authenticator
import Helper.resourceBundle
import Helper.spec
import org.hamcrest.Matchers
import org.junit.Test

class AccountTest {
    private val params = hashMapOf("nonce" to System.currentTimeMillis())
    @Test
    fun getAccountInfo() {
        given()
            .spec(spec)
            .header("Sign", Authenticator.getSignature(resourceBundle.getString("apiSecret"), params))
            .params(params)
        .`when`()
            .post("/info/account")
        .then()
            .statusCode(200)
        .assertThat()
            .body("success", Matchers.equalTo(1))
    }
}