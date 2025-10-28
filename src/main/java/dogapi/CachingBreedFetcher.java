package dogapi;

import java.util.*;

public class CachingBreedFetcher implements BreedFetcher {
    private final BreedFetcher innerFetcher;
    private final Map<String, List<String>> cache = new HashMap<>();
    private int callsMade = 0;

    public CachingBreedFetcher(BreedFetcher fetcher) {
        this.innerFetcher = fetcher;
    }

    @Override
    public List<String> getSubBreeds(String breed) throws BreedNotFoundException {
        // Check cache first
        if (cache.containsKey(breed)) {
            return cache.get(breed);
        }

        // Otherwise fetch and cache
        try {
            List<String> subBreeds = innerFetcher.getSubBreeds(breed);
            cache.put(breed, subBreeds);
            callsMade++;
            return subBreeds;
        } catch (BreedNotFoundException e) {
            // Do NOT cache failed results
            throw e;
        }
    }

    public int getCallsMade() {
        return callsMade;
    }
}

