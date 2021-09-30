package io.jay.springbootwebclientsample.producer;

import lombok.Data;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicLong;

public abstract class IntervalMessageProducer {
    public static Flux<String> produce(int count) {
        return produce().take(count);
    }

    public static Flux<String> produce() {
        return doProduceCountAndStrings().map(CountAndString::getMessage);
    }

    private static Flux<CountAndString> doProduceCountAndStrings() {
        AtomicLong counter = new AtomicLong();
        return Flux
                .interval(Duration.ofSeconds(1))
                .map(i -> new CountAndString(counter.incrementAndGet()));
    }
}

@Data
class CountAndString {
    private final String message;
    private final long count;

    public CountAndString(long count) {
        this.count = count;
        this.message = "#" + this.count;
    }
}
