package dev.matheuscruz.product;

import io.quarkiverse.dapr.core.SyncDaprClient;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Random;

@Path("/products")
public class ProductResource {

    static final String DEV_SERVICE_PUBSUBE_NAME = "pubsub";

    @Inject
    SyncDaprClient syncDaprClient;

    @POST
    public Response create() {
        List<String> products = List.of("mouse", "book", "pencil");
        int random = new Random().nextInt(products.size());
        syncDaprClient.publishEvent(DEV_SERVICE_PUBSUBE_NAME, "products.new", products.get(random));
        return Response.accepted().build();
    }
}
