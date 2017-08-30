/**
 * Created by romanbelkov on 04/07/2017.
 */

import com.jayway.restassured.builder.RequestSpecBuilder
import com.jayway.restassured.filter.log.RequestLoggingFilter
import com.jayway.restassured.filter.log.ResponseLoggingFilter
import java.util.*

object Helper {
    val resourceBundle = ResourceBundle.getBundle("keys")!!

    val spec = RequestSpecBuilder()
            .setBaseUri("https://dsx.uk/tapi/v2")
            .addHeader("Key", resourceBundle.getString("apiPublic"))
            .addFilter(ResponseLoggingFilter())
            .addFilter(RequestLoggingFilter())
            .build()!!
}