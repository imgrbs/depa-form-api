package online.testing.user.model.user.impl;

import java.util.Map;

import online.testing.user.model.user.User;
import online.testing.user.security.config.AuthProvider;

public class GoogleUserAdapter implements User {

    private static UserImpl user;

    protected static User create(String email) {
        user = UserImpl.create(email);
        user.setProvider(AuthProvider.google);
        return user;
    }

    @Override
    public String getEmail() {
        return user.getEmail();
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public void setEmail(String email) {
        user.setEmail(email);
    }

    @Override
    public String getDisplayName() {
        return user.getDisplayName();
    }

    @Override
    public String getAuthProvider() {
        return user.getAuthProvider();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public void setPassword(String password) {
        user.setPassword(password);
    }

    @Override
    public String getProvider() {
        return user.getProvider();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public void setUsername(String username) {
        user.setUsername(username);
    }

    @Override
    public void setProviderId(String providerId) {
        user.setProviderId(providerId);
    }

    @Override
    public void setName(String name) {
        user.setName(name);
    }

    @Override
    public void setImageUrl(String imageUrl) {
        user.setImageUrl(imageUrl);
    }

    @Override
    public Map<String, Object> getAttributes() {
        return user.getAttributes();
    }

    @Override
    public void setAttributes(Map<String, Object> attributes) {
        user.setAttributes(attributes);
    }

    @Override
    public void setRole(String role) {
        user.setRole(role);
    }

    @Override
    public String getRole() {
        return user.getRole();
    }
    
    
}
