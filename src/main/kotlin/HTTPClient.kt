/**
 * Created by Roman on 29.06.2017.
 */

import java.io.IOException
import org.apache.http.Consts
import org.apache.http.HttpEntity
import org.apache.http.HttpResponse
import org.apache.http.NameValuePair
import org.apache.http.client.HttpClient
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.client.methods.HttpRequestBase
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.util.EntityUtils

class HTTPClient {

    @Throws(IOException::class)
    fun postHttp(url: String, params: List<NameValuePair>, headers: List<NameValuePair>): String? {
        val post = HttpPost(url)
        post.entity = UrlEncodedFormEntity(params, Consts.UTF_8)
        post.entity.toString()

        for (header in headers) {
            post.addHeader(header.name, header.value)
        }

        val httpClient = HttpClientBuilder.create().build()
        val response = httpClient.execute(post)

        val entity = response.entity
        if (entity != null) {
            return EntityUtils.toString(entity)

        }
        return null
    }

    @Throws(IOException::class)
    fun getHttp(url: String, headers: List<NameValuePair>): String? {
        val request = HttpGet(url)

        for (header in headers) {
            request.addHeader(header.name, header.value)
        }

        val httpClient = HttpClientBuilder.create().build()
        val response = httpClient.execute(request)

        val entity = response.entity
        if (entity != null) {
            return EntityUtils.toString(entity)
        }
        return null
    }
}
