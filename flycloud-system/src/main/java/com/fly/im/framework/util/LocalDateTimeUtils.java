package com.fly.im.framework.util;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * LocalDateTime 工具。
 *
 * @author lxs
 * @date 2026-07-02
 */
public class LocalDateTimeUtils {

    public static LocalDateTime[] buildBetweenTime(int days) {
        LocalDateTime now = LocalDateTime.now();
        return new LocalDateTime[]{now.minusDays(days), now};
    }

    public static List<LocalDateTime> getLatestDays(int days) {
        List<LocalDateTime> result = new ArrayList<>();
        LocalDateTime today = LocalDateTime.now().toLocalDate().atStartOfDay();
        for (int i = days - 1; i >= 0; i--) {
            result.add(today.minusDays(i));
        }
        return result;
    }

}
