//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.bitnei.cloud.common.util;

import java.io.IOException;
import java.util.Iterator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class CsvUtil {
    private CsvUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    @NotNull
    public static String encode(@Nullable String source) {
        if (source == null) {
            return "";
        } else {
            boolean flag = false;
            StringBuilder builder = new StringBuilder(source);

            for(int i = builder.length() - 1; i >= 0; --i) {
                char c = builder.charAt(i);
                if (c == '"') {
                    builder.insert(i, '"');
                } else if (c != ',' && c != '\n') {
                    continue;
                }

                flag = true;
            }

            if (flag) {
                builder.insert(0, '"');
                builder.append('"');
            }

            return builder.toString()+"\t";
        }
    }

    public static void appendLine(@NotNull Appendable writer, @Nullable Iterable<String> stream) throws IOException {
        if (stream != null) {
            Iterator<String> iterator = stream.iterator();
            if (iterator.hasNext()) {
                writer.append(encode((String)iterator.next()));

                while(iterator.hasNext()) {
                    writer.append(',');
                    writer.append(encode((String)iterator.next()));
                }

                writer.append("\r\n");
            }
        }
    }
}
