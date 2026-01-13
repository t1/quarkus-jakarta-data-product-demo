package com.github.t1.products;

import com.github.t1.problemdetail.Status;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;

import java.util.List;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static jakarta.ws.rs.core.Response.Status.NOT_FOUND;

@Path("/products")
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
public class Products {
    @Inject ProductsRepository repo;

    @GET public List<Product> list() {return repo.findAll().toList();}

    @GET @Path("/{id}") public Product get(@PathParam("id") long id) {
        return repo.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Transactional
    @POST public Product create(Product product) {
        return repo.insert(product);
    }

    @Transactional
    @DELETE @Path("/{id}") public void delete(@PathParam("id") long id) {
        repo.deleteById(id);
    }

    @Status(NOT_FOUND)
    private static class ProductNotFoundException extends RuntimeException {
        public ProductNotFoundException(long id) {super("no product found with id=" + id);}
    }
}
