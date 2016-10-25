package com.example;

import com.netflix.spectator.api.Registry;
import com.netflix.spectator.api.Tag;
import org.kairosdb.client.HttpClient;
import org.kairosdb.client.builder.Metric;
import org.kairosdb.client.builder.MetricBuilder;
import org.kairosdb.client.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * A reporter which writes measurements of each metric in KairosDB!
 * A reporter which publishes metric values to a KairosDB server.
 */
@Component
public class SpectatorMetricsKairosDBReporter
{
    private Registry registry;

    @Autowired
    public SpectatorMetricsKairosDBReporter(Registry registry)
    {
        this.registry = registry;
    }

    // kairosdb - http://192.168.99.100:8080/

    @Scheduled(fixedDelay = 2000)
    void exportPublicMetrics() throws Exception
    {
        HttpClient client = new HttpClient("http://192.168.99.100:8080");

        registry.stream().forEach(meter -> {
            meter.measure().forEach(System.out::println);
        });

        registry.counters().forEach(counter -> {
            MetricBuilder builder = MetricBuilder.getInstance();
            Map<String, String> tags = new HashMap();
            Iterator<Tag> sTags = counter.id().tags().iterator();
            Metric metric = builder.addMetric(counter.id().name());
            while (sTags.hasNext())
            {
                Tag tag = sTags.next();
                System.out.println(tag.key() + " : " + tag.value());
                metric.addTag(tag.key(), tag.value());
            }
            metric.addDataPoint(System.currentTimeMillis(), counter.count());
            try
            {
                Response response = client.pushMetrics(builder);
                System.out.println(response.getStatusCode());
            }
            catch (URISyntaxException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        });

        /*
        MetricBuilder builder = MetricBuilder.getInstance();
        builder.addMetric("metric1")
                .addTag("host", "server1")
                .addTag("customer", "Acme")
                .addDataPoint(System.currentTimeMillis(), 10)
                .addDataPoint(System.currentTimeMillis(), 30L);

        // should be defined in some config bean!
        Response response = client.pushMetrics(builder);
        System.out.println(response.getStatusCode());
        */
        client.shutdown();
    }


}
