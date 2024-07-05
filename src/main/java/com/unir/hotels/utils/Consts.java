package com.unir.hotels.utils;

public final class Consts {

    private Consts() {
        throw new IllegalStateException("Utility class");
    }

    //Nombres de campos
    public static final String FIELD_ID = "id";
    public static final String FIELD_NAME = "name";
    public static final String FIELD_ADDRESS = "address";
    public static final String FIELD_DESCRIPTION = "description";
    public static final String FIELD_PRICE = "price";
    public static final String FIELD_STARS = "stars";
    public static final String FIELD_OPINION = "opinion";
    public static final String FIELD_MAX_ROOMS = "maxRooms";
    public static final String FIELD_FACILITIES = "facilities";
    public static final String FIELD_CONTACT_MAIL = "contactMail";
    public static final String FIELD_CONTACT_NUMBER = "contactNumber";
    public static final String FIELD_LATITUDE = "latitude";
    public static final String FIELD_LONGITUDE = "longitude";
    public static final String FIELD_IMAGES = "images";

    //Nombres de agregaciones
    public static final String AGG_KEY_RANGE_PRICE = "priceValues";
    public static final String AGG_KEY_RANGE_PRICE_0 = "-50";
    public static final String AGG_KEY_RANGE_PRICE_1 = "50-100";
    public static final String AGG_KEY_RANGE_PRICE_2 = "100-";

    public static final String AGG_KEY_RANGE_STARS = "starsValues";
    public static final String AGG_KEY_RANGE_STARS_0 = "-2";
    public static final String AGG_KEY_RANGE_STARS_1 = "2-4";
    public static final String AGG_KEY_RANGE_STARS_2 = "4-";
    public static final String AGG_KEY_RANGE_STARS_3 = "5";

    public static final String AGG_KEY_RANGE_OPINION = "opinionValues";
    public static final String AGG_KEY_RANGE_OPINION_0 = "-5";
    public static final String AGG_KEY_RANGE_OPINION_1 = "5-8";
    public static final String AGG_KEY_RANGE_OPINION_2 = "8-";

    public static final String AGG_KEY_TERM_FACILITIES = "facilities";
}
