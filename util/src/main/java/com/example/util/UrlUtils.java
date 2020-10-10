
package com.example.util;

import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class UrlUtils {
    private static final String TAG = "UrlUtils";

    /**
     * 生成url
     *
     * @param host
     * @param param
     * @return
     */
    public static String getUrl(String host, Map<String, String> param) {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            if (param != null) {
                Iterator<String> iterator = param.keySet().iterator();
                if (iterator.hasNext()) {
                    if (host.contains("?") == false) {
                        stringBuilder.append("?");
                    }
                }
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    Object value = param.get(key);
                    stringBuilder.append(key);
                    stringBuilder.append("=");
                    stringBuilder.append(URLEncoder.encode(value.toString(), "utf-8"));
                    if (iterator.hasNext()) {
                        stringBuilder.append("&");
                    }
                }
            }
            return host + stringBuilder.toString();
        } catch (Exception e) {
            LogUtil.i(TAG, "questTask get Url e : " + e != null ? e.toString() : "e == null");
        }
        return null;
    }

    public static class UrlEntity {
        public String baseUrl;
        public Map<String, String> params;
    }

    public static UrlEntity parse(String url) {
        UrlEntity entity = new UrlEntity();
        try {
            URL u = new URL(url);
            entity.baseUrl = u.getHost();
            String query = u.getQuery();
            if (null != query && !"".equals(query.trim())) {
                entity.params = new HashMap<>();
                String[] params = query.split("&");
                for (String param : params) {
                    String[] keyValue = param.split("=");
                    entity.params.put(keyValue[0], keyValue[1]);
                }
            }
        } catch (Exception e) {
            LogUtil.e(TAG, "Exception: " + e.getMessage());
            e.printStackTrace();
        }
        return entity;
    }
}
