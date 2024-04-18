package Alpha3.org;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.net.ssl.SSLContext;

public class Main {

    private static final String RFC_URL_FORMAT = "https://www.rfc-editor.org/rfc/rfc%d.txt";
    private static final int RFC_COUNT = 100;
    private static final Pattern alphabetPattern = Pattern.compile("[a-z]");
    private static final Map<Character, Integer> aggregatedCounts = new ConcurrentHashMap<>();

    public static void main(String[] args) throws InterruptedException {
        List<Thread> threads = new ArrayList<>();

        for (int i = 1; i <= RFC_COUNT; i++) {
            int rfcIndex = i;
            Thread thread = Thread.startVirtualThread(() -> processRFC(rfcIndex));
            threads.add(thread);
        }

        for (Thread thread : threads) {
            thread.join();
        }


        System.out.println("Aggregated Alphabet Counts:");
        for (char c = 'a'; c <= 'z'; c++) {
            System.out.println(c + ": " + aggregatedCounts.getOrDefault(c, 0));
        }
    }

    private static void processRFC(int rfcIndex) {
        try {
            String rfcText = fetchRFC(rfcIndex);
            countAlphabets(rfcText);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String fetchRFC(int rfcIndex) throws Exception {
        String url = String.format(RFC_URL_FORMAT, rfcIndex);


        TrustManager[] trustAllCerts = new TrustManager[] {
                new X509TrustManager() {
                    public void checkClientTrusted(X509Certificate[] chain, String authType) {}
                    public void checkServerTrusted(X509Certificate[] chain, String authType) {}
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }
                }
        };


        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustAllCerts, new SecureRandom());


        HttpClient client = HttpClient.newBuilder()
                .sslContext(sslContext)
                .followRedirects(HttpClient.Redirect.ALWAYS)
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Failed to fetch RFC " + rfcIndex + ". HTTP Status Code: " + response.statusCode());
        }

        return response.body();
    }

    private static void countAlphabets(String text) {
        Matcher matcher = alphabetPattern.matcher(text.toLowerCase());

        while (matcher.find()) {
            char alphabet = matcher.group().charAt(0);
            aggregatedCounts.compute(alphabet, (key, value) -> (value == null) ? 1 : value + 1);
        }
    }
}
