package com.hcmute.bookstore.model;

import java.util.List;

public class OrderStatus_24162056 {
    public static final String NEW = "NEW";
    public static final String CONFIRMED = "CONFIRMED";
    public static final String PREPARING = "PREPARING";
    public static final String SHIPPING = "SHIPPING";
    public static final String DELIVERING = "DELIVERING";
    public static final String DELIVERED = "DELIVERED";
    public static final String CANCELLED = "CANCELLED";
    public static final String RETURNED = "RETURNED";

    public static final List<Option> OPTIONS = List.of(
            new Option(NEW, "Đơn hàng mới"),
            new Option(CONFIRMED, "Đã xác nhận"),
            new Option(PREPARING, "Chuẩn bị hàng"),
            new Option(SHIPPING, "Vận chuyển"),
            new Option(DELIVERING, "Giao hàng"),
            new Option(DELIVERED, "Đã giao"),
            new Option(CANCELLED, "Đơn hàng hủy"),
            new Option(RETURNED, "Đơn hàng hoàn")
    );

    private OrderStatus_24162056() {
    }

    public static boolean isValid(String value) {
        if (value == null || value.isBlank()) {
            return true;
        }
        return OPTIONS.stream().anyMatch(option -> option.getValue().equals(value));
    }

    public static String clean(String value) {
        String trimmed = value == null ? "" : value.trim().toUpperCase();
        return isValid(trimmed) ? trimmed : "";
    }

    public static class Option {
        private final String value;
        private final String label;

        public Option(String value, String label) {
            this.value = value;
            this.label = label;
        }

        public String getValue() {
            return value;
        }

        public String getLabel() {
            return label;
        }
    }
}
