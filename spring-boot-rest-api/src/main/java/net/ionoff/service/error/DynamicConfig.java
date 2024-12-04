package net.ionoff.service.error;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class DynamicConfig {

    List<String> authors = Arrays.asList("Santideva", "Marie Kondo", "Martin Fowler", "mkyong");

    public boolean isValid(String author) {

        return authors.contains(author);

    }
}
