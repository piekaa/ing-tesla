package pl.piekoszek.ing.http

import groovyx.net.http.HttpBuilder
import org.springframework.http.MediaType

class HttpClient {
    HttpBuilder http;

    HttpClient(int port, String path) {
        http = HttpBuilder.configure {
            request.uri = "http://localhost:$port/$path"
            request.contentType = MediaType.APPLICATION_JSON_VALUE
        }
    }

    def post(String bodyJSON) {
        def status
        def responseBody
        http.post {
            request.body = bodyJSON
            response.success { resp, json ->
                status = resp.statusCode
                responseBody = json
            }
        }
        return [responseBody, status]
    }
}
