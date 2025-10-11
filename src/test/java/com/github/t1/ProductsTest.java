package com.github.t1;

import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.BDDAssertions.then;

@QuarkusTest
class ProductsTest {
    @Path("/products")
    public interface ProductsApi {
        @GET List<Product> list();

        @GET @Path("/{id}") Product get(@PathParam("id") long id);

        @POST Product create(Product product);
    }

    public record Product(Long id, String name) {}

    ProductsApi products;

    @TestHTTPResource
    URI baseUri;

    @BeforeEach void setUp() {
        products = RestClientBuilder.newBuilder().baseUri(baseUri).build(ProductsApi.class);
    }

    @Nested class GivenExistingProduct {
        Product product;

        @BeforeEach void setUp() {
            this.product = products.create(new Product(null, "product " + UUID.randomUUID().toString().substring(0, 6)));
        }

        @Test void shouldGetProducts() {
            var list = products.list();

            then(list).contains(product);
        }

        @Test void shouldGetProduct() {
            var found = products.get(product.id);

            then(found).usingRecursiveComparison().isEqualTo(product);
        }
    }
}
