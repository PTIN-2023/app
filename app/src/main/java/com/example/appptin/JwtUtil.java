package com.example.appptin;

import org.json.JSONException;
import org.json.JSONObject;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.nio.charset.StandardCharsets;
import io.jsonwebtoken.security.Keys;

import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class JwtUtil {
    private static String secretKeyString = "jwt1234-piramide-quadrada-gos-salvatge-iceberg-piña-Meren";


    public static String createJWT(JSONObject jsonObject) throws JSONException {
        byte[] apiKeySecretBytes = secretKeyString.getBytes(StandardCharsets.UTF_8);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, SignatureAlgorithm.HS256.getJcaName());
        Map<String, Object> jsonMap = new HashMap<>();
        Iterator<String> keys = jsonObject.keys();
        while(keys.hasNext()) {
            String key = keys.next();
            jsonMap.put(key, jsonObject.get(key));
        }
        String jws = Jwts.builder()
                .setClaims(jsonMap)
                .setIssuedAt(new Date())
                .signWith(signingKey)
                .compact();
        return jws;
    }
}
