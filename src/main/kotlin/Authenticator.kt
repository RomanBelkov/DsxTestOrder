/**
 * Created by Roman on 28.06.2017.
 */

import org.apache.http.NameValuePair
import org.apache.http.message.BasicNameValuePair
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import kotlin.collections.ArrayList
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;
import com.sun.corba.se.spi.presentation.rmi.StubAdapter.request
import java.io.IOException


class Authenticator {
    private val HMAC_SHA512 = "HmacSHA512"
    private val apiKey = ""
    private val apiSecret = ""
    private val tradingUrl = ""
    private val client = HTTPClient()

    fun returnResults(method : String, additionalParams : List<NameValuePair>): String? {
        try {
            val postParams = ArrayList<NameValuePair>()
            postParams.add(BasicNameValuePair("method", method))
            postParams.add(BasicNameValuePair("nonce", System.nanoTime().toString()))

            if (additionalParams.isNotEmpty()) postParams.addAll(additionalParams)

            val sb = StringBuilder()
            for (postParam in postParams) {
                if (sb.isNotEmpty()) sb.append("&")
                sb.append(postParam.name).append("=").append(postParam.value)
            }
            val body = sb.toString()

            val mac = Mac.getInstance(HMAC_SHA512)
            mac.init(SecretKeySpec(apiSecret.toByteArray(), HMAC_SHA512))
            val signature = String(Base64.getEncoder().encode(mac.doFinal(body.toByteArray())))

            val httpHeaders = ArrayList<NameValuePair>()
            httpHeaders.add(BasicNameValuePair("Key", apiKey))
            httpHeaders.add(BasicNameValuePair("Sign", signature))

            return client.postHttp(tradingUrl, postParams, httpHeaders)
        } catch (e : IOException) {
            e.printStackTrace()
        }
        return null
    }
}