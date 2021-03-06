package by.note.home.service.util;

import java.security.SecureRandom;
import org.apache.commons.lang3.RandomStringUtils;

public class RandomUtil {

    private static final int DEF_COUNT = 20;

    private static final SecureRandom SECURE_RANDOM;

    static {
        SECURE_RANDOM = new SecureRandom();
        SECURE_RANDOM.nextBytes(new byte[64]);
    }

    private RandomUtil() {
    }

    private static String generateRandomAlphanumericString() {
        return RandomStringUtils.random(DEF_COUNT, 0, 0, true, true, null, SECURE_RANDOM);
    }

    public static String generateActivationKey() {
        return generateRandomAlphanumericString();
    }
}
