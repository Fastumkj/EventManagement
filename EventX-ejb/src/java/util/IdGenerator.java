package util;

import java.util.UUID;

public class IdGenerator {

    public static String generateUniqueId() {
        // Generate a unique ID using UUID
        return UUID.randomUUID().toString();
    }
}
