package dogapi;

import java.util.*;

/**
 * This BreedFetcher caches fetch request results to improve performance and
 * lessen the load on the underlying data source.
 */
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

        // Count every call attempt
        callsMade++;

        try {
            List<String> subBreeds = innerFetcher.getSubBreeds(breed);
            cache.put(breed, subBreeds);  // cache successful results only
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
