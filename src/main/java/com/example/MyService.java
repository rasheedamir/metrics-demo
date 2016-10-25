package com.example;

import com.netflix.spectator.api.Id;
import com.netflix.spectator.api.Meter;
import com.netflix.spectator.api.Registry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.stereotype.Service;

/**
 * Created by rasheed on 17/10/16.
 */
@Service
public class MyService
{
    private final CounterService counterService;
    private final Registry registry;
    private int count;

    @Autowired
    public MyService(CounterService counterService, Registry registry)
    {
        this.counterService = counterService;
        this.registry = registry;
    }

    public int exampleMethod()
    {
        count++;

        /* using spring boot actuator */
        this.counterService.increment("services.system.myservice.invoked");

        /* using spectator api */
        final Id requestCountId = registry.createId("server.requestCount")
                .withTag("method", "GET");
        registry.counter(requestCountId).increment();

        final Id requestCountIdPost = registry.createId("server.requestCount")
                .withTag("method", "POST");
        registry.counter(requestCountIdPost).increment();

        /*
        registry.get(requestCountId).measure().forEach(System.out::println);
        registry.get(requestCountIdPost).measure().forEach(System.out::println);

        System.out.println(registry.stream().count());

        registry.stream().forEach(meter -> {
            System.out.println(meter.id());
        });
        */

        return count;
    }
}
