package io.pifind.mapserver.util;

import org.apache.commons.codec.digest.DigestUtils;

public class InterestPointHashUtils {

    private final static String DEFAULT_KEY = "k3Ab3VmezvxoyfVpn1kpEF0osEUwivMv";

    /**
     * 生成 hash
     * @param name 名字
     * @param address 地址
     * @return 哈希值
     */
    public static String hash(String name,String address) {
        String hash = DigestUtils.sha256Hex(name + address + DEFAULT_KEY);
        StringBuilder hashBuilder = new StringBuilder(hash);
        if (hash.length() < 64) {
            for (int i = 0; i < 64 - hash.length(); i++) {
                hashBuilder.append("0");
            }
        }
        return hashBuilder.toString();
    }

    /**
     * 检查 hash
     * @param name 名字
     * @param address 地址
     * @param hash 哈希值
     * @return 是否匹配
     */
    public static boolean check(String name,String address, String hash) {
        return InterestPointHashUtils.hash(name,address).equals(hash);
    }

}
