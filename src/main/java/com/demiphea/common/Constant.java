package com.demiphea.common;

import cn.hutool.core.util.RandomUtil;

import java.util.List;

/**
 * Constant
 *
 * @author demiphea
 * @since 17.0.9
 */
public class Constant {
    public static final String DEFAULT_AVATAR_URL = "https://cdn.demiphea.com/intelli-note/default/avatar.jpeg";

    public static final List<String> AVATAR_DIR = List.of("intelli-note", "avatar");

    public static final List<String> NOTE_COVER_DIR = List.of("intelli-note", "note", "cover");

    public static final List<String> COLLECTION_COVER_DIR = List.of("intelli-note", "collection", "cover");

    public static final List<String> TMP_DIR = List.of("intelli-note", "tmp");


    private static final String DEFAULT_NOTE_COVER_URL = "https://cdn.demiphea.com/intelli-note/default/note-cover-<NUMBER>.png";

    public static String defaultNoteCover() {
        return DEFAULT_NOTE_COVER_URL.replace("<NUMBER>", String.valueOf(RandomUtil.randomInt(0, 6)));
    }
}
