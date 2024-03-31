package com.easyskillup.productservice;

import com.easyskillup.productservice.dto.ProductRequest;
import io.restassured.RestAssured;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MongoDBContainer;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductServiceApplicationTests {

    @ServiceConnection
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0.7");

    @LocalServerPort
    private Integer port;

    @BeforeEach
    void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    static {
        mongoDBContainer.start();
    }

    @Test
    void shouldCreateProduct() throws Exception {
        ProductRequest productRequest = getProductRequest();
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(productRequest)
                .when()
                .post("/api/products")
                .then()
                .log().all()
                .statusCode(201)
                .body("id", notNullValue())
                .body("name", equalTo(productRequest.name()))
                .body("description", equalTo(productRequest.description()))
                .body("price", is(productRequest.price().intValueExact()));
    }

    @Test
    void shouldGetAllProduct() {
        RestAssured
                .given()
                .when()
                .get("/api/products")
                .then()
                .log()
                .all()
                .statusCode(200);
    }

    private ProductRequest getProductRequest() {
        return new ProductRequest("iPhone 13", "iPhone 13", BigDecimal.valueOf(1200));
    }

    public static ResponseSpecification responseSpecOK200ForGetMethod(){
        return new ResponseSpecBuilder()
                .expectStatusCode(200)
                .setDefaultParser(Parser.JSON)
                .build();
    }

}
