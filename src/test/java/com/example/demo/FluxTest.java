package com.example.demo;

import com.mysema.commons.lang.Assert;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FluxTest {
    @Test
    public void test_flux_just_consumer(){
        List<String> names = new ArrayList<>();
        Flux<String> flux = Flux.just("Eddy", "Irin").log();

        flux.subscribe(names::add);


    }
}
