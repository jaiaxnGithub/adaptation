package com.jaiaxn.adaptation.utils.uuid;

import java.util.UUID;

/**
 * @author: wang.jiaxin
 * @date: 2019年07月17日
 * @description: UUID工具类
 **/
public class UUIDUtils {

    /**
     * 生成32位的UUID
     *
     * @return 32位的UUID
     */
    public static String generate32UUID() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 32);
    }
}
