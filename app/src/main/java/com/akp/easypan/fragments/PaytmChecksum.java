package com.akp.easypan.fragments;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

public class PaytmChecksum {
    public static String generateSignature(String params, String merchantKey) throws Exception {
        String salt = merchantKey;
        StringBuilder finalString = new StringBuilder();
        String[] keyValuePairs = params.split("&");
        for (String pair : keyValuePairs) {
            String[] entry = pair.split("=");
            finalString.append(entry[0]).append("=").append(entry[1]).append("&");
        }
        finalString = finalString.deleteCharAt(finalString.length() - 1);
        finalString.append(salt);
        return calculateChecksum(finalString.toString(), "SHA-256");
    }

    private static String calculateChecksum(String requestBody, String algorithm) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance(algorithm);
        digest.reset();
        byte[] byteData = digest.digest(requestBody.getBytes());
        Formatter formatter = new Formatter();
        for (byte b : byteData) {
            formatter.format("%02x", b);
        }
        return formatter.toString();
    }
}

