package springkpsgroup02.kps.Jwt;

import java.util.UUID;

public class UserContext {
    private static ThreadLocal<String> userIdHolder = new ThreadLocal<>();

    public static void setUserId(String userId) {
        userIdHolder.set(userId);
    }

    public static String getUserId() {
        return userIdHolder.get();
    }

    public static void clear() {
        userIdHolder.remove();
    }

    public static UUID getUserIdAsUUID() {
        String userId = userIdHolder.get();
        if (userId != null) {
            return UUID.fromString(userId);
        }
        return null;
    }
}
