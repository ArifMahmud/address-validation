import groovy.json.JsonBuilder
import groovyx.net.http.RESTClient
import spock.lang.Specification
import spock.lang.Unroll

class AddressValidationSpec extends Specification {

    RESTClient restClient = new RESTClient(Constant.testURL)

    @Unroll
    def 'User should be able to search any specific address'() {

        setup:
        def addressLine1 = "506 4TH AVE APT 1";
        def addressLine2 = "ASBURY PARK, NJ 07712-6086";

        when:
        def response = restClient.get(
                query: [
                        'AddressLine1': addressLine1,
                        'AddressLine2': addressLine2,
                ]
        )

        println "response: " + new JsonBuilder(response.responseData).toPrettyString()

        then:
        response.responseData.ErrorCode == 00

    }

    @Unroll
    def 'User should be able to search any specific address without mandatory data address line 1'() {

        //Though second address line is optional, it can not be null, at least any of city/state/zip should be present
        setup:
        def addressLine1 = "";
        def addressLine2 = "ASBURY PARK, NJ 07712-6086";

        when:
        def response = restClient.get(
                query: [
                        'AddressLine1': addressLine1,
                        'AddressLine2': addressLine2,
                ]
        )

        println "response: " + new JsonBuilder(response.responseData).toPrettyString()

        then:
        response.responseData.ErrorCode == 00

    }

    @Unroll
    def 'User should not be able to search any specific address without mandatory data address line 2'() {

        //Though second address line is optional, it can not be null, at least any of city/state/zip should be present
        setup:
        def addressLine1 = "506 4TH AVE APT 1";
        def addressLine2 = "";

        when:
        def response = restClient.get(
                query: [
                        'AddressLine1': addressLine1,
                        'AddressLine2': addressLine2,
                ]
        )

        println "response: " + new JsonBuilder(response.responseData).toPrettyString()

        then:
        response.responseData.ErrorCode == 2

    }

    @Unroll
    def 'User should be able to search any specific address with the data of city only in Address Line 2'() {

        //For Address Line 2, city name is added. So the address should be identified
        setup:
        def addressLine1 = "506 4TH AVE APT 1";
        def addressLine2 = "ASBURY PARK";

        when:
        def response = restClient.get(
                query: [
                        'AddressLine1': addressLine1,
                        'AddressLine2': addressLine2,
                ]
        )

        println "response: " + new JsonBuilder(response.responseData).toPrettyString()

        then:
        response.responseData.ErrorCode == 00

    }

    @Unroll
    def 'User should be able to search any specific address with the data of state only in Address Line 2'() {

        //For Address Line 2, state name is added. So the address should be identified
        setup:
        def addressLine1 = "506 4TH AVE APT 1";
        def addressLine2 = "NJ";

        when:
        def response = restClient.get(
                query: [
                        'AddressLine1': addressLine1,
                        'AddressLine2': addressLine2,
                ]
        )

        println "response: " + new JsonBuilder(response.responseData).toPrettyString()

        then:
        response.responseData.ErrorCode == 00

    }

    @Unroll
    def 'User should be able to search any specific address with the data of zip only in Address Line 2'() {

        //For Address Line 2, zip is added. So the address should be identified
        setup:
        def addressLine1 = "506 4TH AVE APT 1";
        def addressLine2 = "07712";

        when:
        def response = restClient.get(
                query: [
                        'AddressLine1': addressLine1,
                        'AddressLine2': addressLine2,
                ]
        )

        println "response: " + new JsonBuilder(response.responseData).toPrettyString()

        then:
        response.responseData.ErrorCode == 00

    }

    @Unroll
    def 'User should not be able to search any specific address with invalid format of input address'() {

        //Invalid format of zip
        setup:
        def addressLine1 = "506 4TH AVE APT 1";
        def addressLine2 = "PARK, NJ, 019@12";

        when:
        def response = restClient.get(
                query: [
                        'AddressLine1': addressLine1,
                        'AddressLine2': addressLine2,
                ]
        )

        println "response: " + new JsonBuilder(response.responseData).toPrettyString()

        then:
        response.responseData.ErrorCode == 2

    }

    @Unroll
    def 'User should not be able to search any specific address with invalid city name in any specific state'() {

        setup:
        def addressLine1 = "506 4TH AVE APT 1";
        def addressLine2 = "NO CITY, NJ";

        when:
        def response = restClient.get(
                query: [
                        'AddressLine1': addressLine1,
                        'AddressLine2': addressLine2,
                ]
        )

        println "response: " + new JsonBuilder(response.responseData).toPrettyString()

        then:
        response.responseData.ErrorCode == 4

    }

    @Unroll
    def 'User should not be able to search any specific address with invalid street name in any specific city/state'() {

        setup:
        def addressLine1 = "123 Street";
        def addressLine2 = "ASBURY PARK, NJ";

        when:
        def response = restClient.get(
                query: [
                        'AddressLine1': addressLine1,
                        'AddressLine2': addressLine2,
                ]
        )

        println "response: " + new JsonBuilder(response.responseData).toPrettyString()

        then:
        response.responseData.ErrorCode == 3

    }
}
