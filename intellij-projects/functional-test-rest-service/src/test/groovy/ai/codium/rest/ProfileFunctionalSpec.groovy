package ai.codium.rest

import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.client.RestTemplate
import spock.lang.Specification
import spock.lang.Unroll

class ProfileFunctionalSpec extends Specification {

    def baseUrl = "http://localhost:8585"
    def restTemplate = new RestTemplate()

    @Unroll
    def "test POST /profile/ with dynamic data"() {
        given:
        def url = "${baseUrl}/profile/"
        def headers = new HttpHeaders()
        headers.setContentType(MediaType.APPLICATION_JSON)
        def request = new HttpEntity<>(userProfile, headers)

        when:
        def response = restTemplate.postForEntity(url, request, Map)

        then:
        response.statusCode == HttpStatus.OK
        response.body.id == userProfile.id
        response.body.name == userProfile.name
        response.body.email == userProfile.email
        response.body.age == userProfile.age

        where:
        userProfile << [[id: 1, name: "John Doe", email: "john.doe@example.com", age: 30],
                        [id: 2, name: "Jane Smith", email: "jane.smith@example.com", age: 25]]
    }

    @Unroll
    def "test GET /profile/{id} with dynamic data"() {
        given:
        def url = "${baseUrl}/profile/${id}"

        when:
        def response = restTemplate.getForEntity(url, Map)

        then:
        response.statusCode == HttpStatus.OK
        response.body.id == id
        response.body.name == expectedName
        response.body.email == expectedEmail
        response.body.age == expectedAge

        where:
        id | expectedName | expectedEmail            | expectedAge
        1  | "John Doe"   | "john.doe@example.com"   | 30
        2  | "Jane Smith" | "jane.smith@example.com" | 25
    }

    def "test GET /profile/users"() {
        given:
        def url = "${baseUrl}/profile/users"

        when:
        def response = restTemplate.getForEntity(url, List)

        then:
        response.statusCode == HttpStatus.OK
        response.body.size() > 0
        response.body.every { it instanceof Map && it.containsKey('id') && it.containsKey('name') && it.containsKey('email') && it.containsKey('age') }
    }
}