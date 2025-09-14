package com.demo.easyuploader_pick_pack_assistant.model;

import java.util.List;
import java.util.regex.Pattern;

public record OrderIdentifier(String raw, String normalized, Type type, Long euIdValue) {

    public enum Type { EUID, TRACKING }

    private static final List<Pattern> ORDER_IDENTIFIER_PATTERNS = List.of(
            Pattern.compile("^\\d{24}$"),           // InPost Paczkomaty
            Pattern.compile("\\d{13}\\D"),          // DPD
            Pattern.compile("^\\d{11}(?=\\d)"),     // GLS
            Pattern.compile("^\\d{11}$"),           // DHL, InPost Kurier
            Pattern.compile("^[A-Z]{2}\\d{10}$"),   // Pocztex
            Pattern.compile("^EUID\\d+")            // EUID
    );

    public static OrderIdentifier of(String input) {
        String normalized = input;
        for (Pattern p : ORDER_IDENTIFIER_PATTERNS) {
            var m = p.matcher(input);
            if (m.find()) {
                normalized = m.group();
                break;
            }
        }

        if (normalized.matches("^EUID\\d+$")) {
            return new OrderIdentifier(input, normalized, Type.EUID, Long.parseLong(normalized.substring(4)));
        }
        return new OrderIdentifier(input, normalized, Type.TRACKING, null);
    }

    public boolean isEuid() {
        return type == Type.EUID;
    }
}
