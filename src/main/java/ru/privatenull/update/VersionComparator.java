package ru.privatenull.update;

import java.util.Locale;

final class VersionComparator {
    private VersionComparator() {
    }

    static int compare(String leftValue, String rightValue) {
        String[] left = normalize(leftValue).split("\\.");
        String[] right = normalize(rightValue).split("\\.");
        for (int index = 0; index < Math.max(left.length, right.length); index++) {
            int leftPart = index < left.length ? number(left[index]) : 0;
            int rightPart = index < right.length ? number(right[index]) : 0;
            if (leftPart != rightPart) return Integer.compare(leftPart, rightPart);
        }
        return 0;
    }

    private static String normalize(String version) {
        return (version == null ? "" : version.trim().toLowerCase(Locale.ROOT))
                .replaceFirst("^v", "").split("[-+]", 2)[0];
    }

    private static int number(String value) {
        try {
            String digits = value.replaceAll("\\D", "");
            return digits.isEmpty() ? 0 : Integer.parseInt(digits);
        } catch (NumberFormatException ignored) {
            return 0;
        }
    }
}
