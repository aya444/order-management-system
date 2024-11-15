package com.aya.api_gateway.filter;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AuthoritiesManager {

    // Add list of all URLs in the system with their required authorities
    private final HashMap<String, String> urlRoles;
    private static final String ADMIN = "ADMIN";
    private static final String ADMIN_SELLER = "ADMIN-SELLER";
    private static final String CUSTOMER = "CUSTOMER";

    public AuthoritiesManager() {
        urlRoles = new HashMap<>();
        populateUrls();
    }

    private void populateUrls() {
        urlRoles.put("/admin/debug", ADMIN);
        urlRoles.put("/admin/create-user", ADMIN);
        urlRoles.put("/admin/get-by-email", ADMIN);
        urlRoles.put("/admin/get-by-id/{id}", ADMIN);
        urlRoles.put("/admin/get-email-by-id/{id}", ADMIN);
        urlRoles.put("/admin/get-all", ADMIN);
        urlRoles.put("/admin/delete-user", ADMIN);

        urlRoles.put("/category/create", ADMIN_SELLER);
        urlRoles.put("/category/edit/{id}", ADMIN_SELLER);
        urlRoles.put("/category/{id}", ADMIN_SELLER);
        urlRoles.put("/category/name/{categoryName}", ADMIN_SELLER);
        urlRoles.put("/category/all", ADMIN_SELLER);
        urlRoles.put("/category/delete/{id}", ADMIN_SELLER);
        urlRoles.put("/inventory/{catName}", ADMIN_SELLER);
        urlRoles.put("/inventory/deduce/{id}", ADMIN_SELLER);

        urlRoles.put("/product/create", ADMIN_SELLER);
        urlRoles.put("/product/edit/{id}", ADMIN_SELLER);
        urlRoles.put("/product/{id}", ADMIN_SELLER);
        urlRoles.put("/product/name/{id}", ADMIN_SELLER);
        urlRoles.put("/product/all", ADMIN_SELLER);
        urlRoles.put("/product/delete/{id}", ADMIN_SELLER);
        urlRoles.put("/product/get-products", ADMIN_SELLER);
        urlRoles.put("/product/price/{id}", ADMIN_SELLER);
        urlRoles.put("/product/quantity/{id}", ADMIN_SELLER);

        urlRoles.put("/order/create", CUSTOMER);
        urlRoles.put("/order/complete/{id}", CUSTOMER);
        urlRoles.put("/order/cancel/{id}", CUSTOMER);
        urlRoles.put("/order/customer/{id}", CUSTOMER);
    }


    public boolean isUserAuthorized(String url, String role) {
        boolean isAuthorized = false;
        for (Map.Entry<String, String> entry : urlRoles.entrySet()) {
            if (isTwoPathsEqual(entry.getKey(), url)) {
                String[] s = entry.getValue().split("-");
                List<String> roles = Arrays.asList(s);
                if (roles.contains(role)) {
                    isAuthorized = true;
                    break;
                }
            }
        }
        return isAuthorized;
    }

    public boolean isTwoPathsEqual(String path1, String path2) {
        String[] path1Parts = path1.split("/");
        String[] path2Parts = path2.split("/");
        if (path1Parts.length != path2Parts.length)
            return false;
        for (int i = 0; i < path1Parts.length; i++) {
            if (path1Parts[i].startsWith("{") && path1Parts[i].endsWith("}"))
                continue;
            if (!path1Parts[i].equals(path2Parts[i]))
                return false;
        }
        return true;
    }

}
