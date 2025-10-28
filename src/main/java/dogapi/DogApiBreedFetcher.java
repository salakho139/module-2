package dogapi;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DogApiBreedFetcher implements BreedFetcher {
    private final OkHttpClient client = new OkHttpClient();

    @Override
    public List<String> getSubBreeds(String breed) throws BreedNotFoundException {
        String url = "https://dog.ceo/api/breed/" + breed + "/list";
        Request request = new Request.Builder().url(url).build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful() || response.body() == null) {
                throw new BreedNotFoundException("Failed to fetch breed info for: " + breed);
            }

            String jsonBody = response.body().string();
            JSONObject json = new JSONObject(jsonBody);

            if (json.getString("status").equals("error")) {
                throw new BreedNotFoundException("Breed not found: " + breed);
            }

            JSONArray subBreeds = json.getJSONArray("message");
            List<String> result = new ArrayList<>();
            for (int i = 0; i < subBreeds.length(); i++) {
                result.add(subBreeds.getString(i));
            }
            return result;

        } catch (IOException e) {
            throw new BreedNotFoundException("Network error while fetching breed: " + breed);
        } catch (Exception e) {
            throw new BreedNotFoundException("Unexpected error for breed: " + breed);
        }
    }
}
