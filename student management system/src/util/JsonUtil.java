package util;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A deliberately small, dependency-free JSON helper.
 * Only parses FLAT JSON objects, e.g. {"name":"Sahil","age":21,"cgpa":8.5}
 * That is all the API in this project ever needs to read from a request body,
 * so a full JSON library (Jackson/Gson) isn't pulled in just for this.
 */
public final class JsonUtil {

    private JsonUtil() {
    }

    /**
     * Parses a flat JSON object into a String-to-String map.
     * Numbers/booleans/null come back as their literal text (e.g. "21", "8.5"),
     * the caller parses them with Integer/Double.parseXxx as needed.
     */
    public static Map<String, String> parseObject(String json) {
        Map<String, String> result = new LinkedHashMap<>();
        if (json == null) {
            return result;
        }
        int i = 0;
        int n = json.length();

        while (i < n && json.charAt(i) != '{') {
            i++;
        }
        i++; // step past '{'

        while (i < n) {
            i = skipWhitespace(json, i);
            if (i >= n || json.charAt(i) == '}') {
                break;
            }
            if (json.charAt(i) == ',') {
                i++;
                continue;
            }
            if (json.charAt(i) != '"') {
                throw new IllegalArgumentException("Malformed JSON: expected a key at position " + i);
            }

            int[] afterKey = new int[1];
            String key = readString(json, i, afterKey);
            i = afterKey[0];

            i = skipWhitespace(json, i);
            if (i < n && json.charAt(i) == ':') {
                i++;
            }
            i = skipWhitespace(json, i);

            String value;
            if (i < n && json.charAt(i) == '"') {
                int[] afterVal = new int[1];
                value = readString(json, i, afterVal);
                i = afterVal[0];
            } else {
                int start = i;
                while (i < n && json.charAt(i) != ',' && json.charAt(i) != '}') {
                    i++;
                }
                value = json.substring(start, i).trim();
            }

            result.put(key, value);
        }
        return result;
    }

    private static int skipWhitespace(String s, int i) {
        while (i < s.length() && Character.isWhitespace(s.charAt(i))) {
            i++;
        }
        return i;
    }

    /** Reads a quoted JSON string starting at index i (which must point at the opening quote). */
    private static String readString(String s, int i, int[] endIndexOut) {
        StringBuilder sb = new StringBuilder();
        int n = s.length();
        i++; // skip opening quote
        while (i < n && s.charAt(i) != '"') {
            char c = s.charAt(i);
            if (c == '\\' && i + 1 < n) {
                char next = s.charAt(i + 1);
                switch (next) {
                    case 'n' -> sb.append('\n');
                    case 't' -> sb.append('\t');
                    case 'r' -> sb.append('\r');
                    case '"' -> sb.append('"');
                    case '\\' -> sb.append('\\');
                    case '/' -> sb.append('/');
                    default -> sb.append(next);
                }
                i += 2;
            } else {
                sb.append(c);
                i++;
            }
        }
        endIndexOut[0] = i + 1; // step past closing quote
        return sb.toString();
    }
}
