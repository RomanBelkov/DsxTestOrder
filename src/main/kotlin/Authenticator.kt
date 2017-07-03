/**
 * Created by Roman on 28.06.2017.
 */

import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec


class Authenticator {
    private val HMAC_SHA512 = "HmacSHA512"

    fun getSignature(secretKey : String, parameters : Map<String, Any?>) : String {
        val queryString = mapToQueryString(parameters)
        val mac = Mac.getInstance(HMAC_SHA512)
        mac.init(SecretKeySpec(secretKey.toByteArray(), HMAC_SHA512))
        return String(Base64.getEncoder().encode(mac.doFinal(queryString.toByteArray())))
    }

    private fun <K, V> mapToQueryString(map : Map<K, V>) : String {
        val sb = StringBuilder()
        for ((key, value) in map.entries) {
            if (sb.isNotEmpty()) sb.append("&")
            sb.append(key).append("=").append(value)
        }
        return sb.toString()
    }
}