package com.lab04.api.config;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class RequestSpecFactory {

    private RequestSpecFactory() {}

    /**
     * Base request spec: sets base URI, content-type, timeout and request/response logging.
     */
    public static RequestSpecification buildBaseSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(ApiConfig.BASE_URL)
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .log(LogDetail.ALL)
                .build();
    }

    /**
     * Response spec that expects HTTP 200 and JSON content-type.
     */
    public static ResponseSpecification build200Spec() {
        return new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectContentType(ContentType.JSON)
                .log(LogDetail.ALL)
                .build();
    }

    /**
     * Response spec that expects HTTP 201 (resource created).
     */
    public static ResponseSpecification build201Spec() {
        return new ResponseSpecBuilder()
                .expectStatusCode(201)
                .expectContentType(ContentType.JSON)
                .log(LogDetail.ALL)
                .build();
    }
}
