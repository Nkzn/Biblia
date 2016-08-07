package info.nkzn.biblia;

import info.nkzn.biblia.rakuten.RakutenApiClient;

public class Biblia {

    public static void setRakutenApplicationId(String applicationId) {
        RakutenApiClient.Static.setApplicationId(applicationId);
    }

    public static ApiClient client() {
        return client(ClientType.RAKUTEN);
    }

    public static ApiClient client(ClientType clientType) {
        switch (clientType) {
            case RAKUTEN:
                return new RakutenApiClient();
            case GOOGLE:
                throw new UnsupportedOperationException("Google Books API is not supported.");
            default:
                throw new IllegalArgumentException("Unknown API: " + clientType.name());
        }
    }
}
