package dev.matheuscruz.product;

import io.quarkiverse.dapr.core.SyncDaprClient;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("/products")
public class ProductResource {

    static final String DEV_SERVICE_PUBSUBE_NAME = "pubsub";

    @Inject
    SyncDaprClient syncDaprClient;

    public ProductResource() {
    }

    @POST
    public Response create() {
        syncDaprClient.publishEvent(DEV_SERVICE_PUBSUBE_NAME, "products.new", "New product created");
        return Response.accepted().build();
    }

}
