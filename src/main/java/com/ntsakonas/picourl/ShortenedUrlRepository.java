package com.ntsakonas.picourl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ShortenedUrlRepository {

    public Set<Long> getHashValues(List<Long> shortHashValues) {
        Set<Long> usedValues = new HashSet<>() {{
            // add(7835846572003L);
            // add(467513369371L);
        }};

        return usedValues;
    }
}
