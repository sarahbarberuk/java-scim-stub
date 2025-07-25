package com.example.scimstub;

import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/scim/v2")
public class ScimController {

    private void logAuthHeader(HttpServletRequest request) {
        String expectedUser = System.getenv("SCIM_USER");
        String expectedPassword = System.getenv("SCIM_PASSWORD");

        System.out.println("SCIM_USER: " + expectedUser);
        System.out.println("SCIM_PASSWORD: " + expectedPassword);

        if (expectedUser == null || expectedPassword == null) {
            throw new IllegalStateException("SCIM_USER or SCIM_PASSWORD not set in environment");
        }

        String auth = request.getHeader("Authorization");

        if (auth == null || !auth.startsWith("Basic ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing or invalid Authorization header");
        }

        String base64Credentials = auth.substring("Basic ".length()).trim();
        byte[] decodedBytes = Base64.getDecoder().decode(base64Credentials);
        String decoded = new String(decodedBytes, StandardCharsets.UTF_8);

        String[] parts = decoded.split(":", 2);
        if (parts.length != 2 || !parts[0].equals(expectedUser) || !parts[1].equals(expectedPassword)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
        }
    }


    @GetMapping("/ServiceProviderConfig")
    public ResponseEntity<Map<String, Object>> getServiceProviderConfig(HttpServletRequest request) {
        logAuthHeader(request);
        return ResponseEntity.ok(Map.of(
            "schemas", List.of("urn:ietf:params:scim:schemas:core:2.0:ServiceProviderConfig"),
            "patch", Map.of("supported", true),
            "bulk", Map.of("supported", false),
            "filter", Map.of("supported", true),
            "changePassword", Map.of("supported", false),
            "authenticationSchemes", List.of()
        ));
    }

    @GetMapping("/Users")
    public ResponseEntity<Map<String, Object>> getUsers(HttpServletRequest request) {
        logAuthHeader(request);
        return ResponseEntity.ok(Map.of(
            "schemas", List.of("urn:ietf:params:scim:api:messages:2.0:ListResponse"),
            "totalResults", 0,
            "Resources", List.of(),
            "startIndex", 1,
            "itemsPerPage", 0
        ));
    }

    @PostMapping("/Users")
    public ResponseEntity<Map<String, Object>> createUser(@RequestBody Map<String, Object> body, HttpServletRequest request) {
        logAuthHeader(request);
        System.out.println("Received user from Okta:");
        System.out.println(body);

        return ResponseEntity.status(201).body(Map.of(
            "schemas", List.of("urn:ietf:params:scim:schemas:core:2.0:User"),
            "id", "user-123",
            "userName", body.get("userName"),
            "active", true
        ));
    }

    @GetMapping("/Groups")
    public ResponseEntity<Map<String, Object>> getGroups(HttpServletRequest request) {
        logAuthHeader(request);
        return ResponseEntity.ok(Map.of(
            "schemas", List.of("urn:ietf:params:scim:api:messages:2.0:ListResponse"),
            "totalResults", 0,
            "Resources", List.of(),
            "startIndex", 1,
            "itemsPerPage", 0
        ));
    }
}
