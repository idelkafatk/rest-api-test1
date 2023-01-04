package utils;

import com.github.javafaker.Faker;

public class RandomUtils {
    static Faker faker = new Faker();
    public static String testcaseName = faker.name().nameWithMiddle();
}
