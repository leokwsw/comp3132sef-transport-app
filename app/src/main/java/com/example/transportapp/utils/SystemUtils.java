package com.example.transportapp.utils;

import android.os.Build;

public class SystemUtils {
    public static boolean isEmulator() {
        return // Android SDK emulator
                ("Google".equals(Build.MANUFACTURER) && "google".equals(Build.BRAND) &&
                        ((Build.FINGERPRINT.startsWith("google/sdk_gphone_")
                                && Build.FINGERPRINT.endsWith(":user/release-keys")
                                && Build.PRODUCT.startsWith("sdk_gphone_")
                                && Build.MODEL.startsWith("sdk_gphone_"))
                                //alternative
                                || (Build.FINGERPRINT.startsWith("google/sdk_gphone64_")
                                && (Build.FINGERPRINT.endsWith(":userdebug/dev-keys")
                                || Build.FINGERPRINT.endsWith(":user/release-keys"))
                                && Build.PRODUCT.startsWith("sdk_gphone64_")
                                && Build.MODEL.startsWith("sdk_gphone64_")))
                        // Google Play Games emulator
                        || ("HPE device".equals(Build.MODEL)
                        && Build.FINGERPRINT.startsWith("google/kiwi_")
                        && Build.FINGERPRINT.endsWith(":user/release-keys")
                        && "kiwi".equals(Build.BOARD)
                        && Build.PRODUCT.startsWith("kiwi_")))
                        // Generic checks
                        || Build.FINGERPRINT.startsWith("generic")
                        || Build.FINGERPRINT.startsWith("unknown")
                        || Build.MODEL.contains("google_sdk")
                        || Build.MODEL.contains("Emulator")
                        || Build.MODEL.contains("Android SDK built for x86")
                        // Bluestacks
                        || ("QC_Reference_Phone".equals(Build.BOARD) && !"Xiaomi".equalsIgnoreCase(Build.MANUFACTURER))
                        || Build.MANUFACTURER.contains("Genymotion")
                        || Build.HOST.startsWith("Build")
                        // MSI App Player
                        || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                        || "google_sdk".equals(Build.PRODUCT)
                        // Another Android SDK emulator check
                        || "1".equals(SystemProperties.getProp("ro.kernel.qemu"));
    }

    public static class SystemProperties {
        public static String getProp(String key) {
            try {
                Class<?> systemProperties = Class.forName("android.os.SystemProperties");
                return (String) systemProperties.getMethod("get", String.class).invoke(null, key);
            } catch (Exception e) {
                return "";
            }
        }
    }
}
