package dev.matheuscruz.product;

import io.dapr.Topic;
import io.dapr.client.domain.CloudEvent;
import io.dapr.client.domain.State;
import io.dapr.utils.TypeRef;
import io.quarkiverse.dapr.core.SyncDaprClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

import java.util.*;

@Path("/handlers")
@ApplicationScoped
public class ProductCreatedHandler {


    @Inject
    SyncDaprClient syncDaprClient;

    List<CloudEvent<String>> events = new ArrayList<>();

    @POST
    @Topic(name = "products.new", pubsubName = "pubsub")
    @Path("/products")
    public void handle(CloudEvent<String> event) {
        System.out.println("Received: " + event.getData());
        events.add(event);
        try {
            State<Product> state = syncDaprClient.getState("kvstore", event.getData(), TypeRef.get(Product.class));
            System.out.println("We already have a product with name: " + state.getValue());
        } catch (Exception e) {
            System.out.println("We do not have a product :(");
            // Let's create
            syncDaprClient.saveState("kvstore", event.getData(), new Product(
                    event.getData()
            ));
        }
    }

    /**
     * Just to have a way to get events!
     * @return
     */
    @GET
    @Path("/events")
    public List<CloudEvent<String>> getEvents() {
        return Collections.unmodifiableList(events);
    }

    public record Product(String name) {}
}
