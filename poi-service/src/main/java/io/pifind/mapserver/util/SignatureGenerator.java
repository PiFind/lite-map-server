package io.pifind.mapserver.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;

public class SignatureGenerator {

    // 你的 API Key
    private static final String API_KEY = "PiChain#022@!&&0001";

    // 生成签名
    public static String generateSignature(long timestamp) throws NoSuchAlgorithmException {

        // 构建待签名的字符串
        String dataToSign = API_KEY + timestamp;

        // 使用 SHA-256 算法进行哈希
        byte[] hash = getSHA256(dataToSign);

        // 将哈希转换为十六进制表示
        return bytesToHex(hash);
    }

    // 使用 SHA-256 算法进行哈希
    private static byte[] getSHA256(String data) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        return digest.digest(data.getBytes(StandardCharsets.UTF_8));
    }

    // 将字节数组转换为十六进制表示
    private static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }
}
