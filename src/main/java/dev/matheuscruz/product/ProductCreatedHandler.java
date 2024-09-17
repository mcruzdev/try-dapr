package dev.matheuscruz.product;

import io.dapr.Topic;
import io.dapr.client.domain.CloudEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Path("/handlers")
@ApplicationScoped
public class ProductCreatedHandler {

    List<CloudEvent<String>> events = new ArrayList<>();

    @POST
    @Topic(name = "products.new", pubsubName = "pubsub")
    @Path("/products")
    public void handle(CloudEvent<String> event) {
        System.out.println("Received: " + event.getData());
        events.add(event);
    }

    public List<CloudEvent<String>> getEvents() {
        return Collections.unmodifiableList(events);
    }
}
