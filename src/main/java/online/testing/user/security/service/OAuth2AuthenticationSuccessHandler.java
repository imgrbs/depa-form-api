package online.testing.user.security.service;

import java.io.IOException;
import java.net.URI;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.SneakyThrows;
import online.testing.user.dto.UserPrincipal;
import online.testing.user.model.user.impl.UserFactory;
import online.testing.user.model.user.impl.UserImpl;
import online.testing.user.repository.UserRepository;
import online.testing.user.security.config.AppProperties;
import online.testing.user.security.exception.BadRequestException;
import online.testing.user.security.repository.HttpCookieOAuth2AuthorizationRequestRepository;
import online.testing.user.service.TokenProvider;

@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private static final String REDIRECT_URI_PARAM_COOKIE_NAME = "redirect_uri";
    private TokenProvider tokenProvider;

    private AppProperties appProperties;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private UserRepository userRepository;

    private HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

    @Autowired
    OAuth2AuthenticationSuccessHandler(TokenProvider tokenProvider, AppProperties appProperties,
            HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository) {
        this.tokenProvider = tokenProvider;
        this.appProperties = appProperties;
        this.httpCookieOAuth2AuthorizationRequestRepository = httpCookieOAuth2AuthorizationRequestRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        String targetUrl = determineTargetUrl(request, response, authentication);

        if (response.isCommitted()) {
            logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
            return;
        }

        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    @SneakyThrows
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) {
        Optional<String> redirectUri = CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME).map(Cookie::getValue);

        if (redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get())) {
            throw new BadRequestException(
                    "Sorry! We've got an Unauthorized Redirect URI and can't proceed with the authentication");
        }

        String targetUrl = redirectUri.orElse(getDefaultTargetUrl());

        if (authentication.isAuthenticated() && isOAuth2Provider(authentication)) {
            UserPrincipal userPrincipal = UserPrincipal.create(authentication.getPrincipal());
            System.out.println("Out of hell loop : Oauth2Authe Success Handler");
            System.out.println(userPrincipal.getRole());
            UserDetails userDetails = userDetailsService.loadUserByUsername(userPrincipal.getEmail());

            if (userDetails == null) {
                UserImpl user = UserFactory.create(userPrincipal, getRegistrationId(authentication));
                userRepository.save(user);
                System.out.println("!!!!! 86 OAuth User Null !!!!!");
                System.out.println(user.getRole());
                //String token = tokenProvider.createToken(user.getId().toString());
                String token = tokenProvider.createToken(userPrincipal);
                return UriComponentsBuilder.fromUriString(targetUrl).queryParam("token", token).build().toUriString();
            }
            System.out.println("Ultimate Convert UserDetail to UserPrincipal Pass!!!!!!!!");
            userPrincipal = (UserPrincipal) userDetails;
            System.out.println(userPrincipal.getRole());
            System.out.println("!!!! 91 User Detail Not Null !!!!");
            System.out.println(userPrincipal.getRole());
            //String token = tokenProvider.createToken(((UserPrincipal) userDetails).getId().toString());
            String token = tokenProvider.createToken(userPrincipal);
            return UriComponentsBuilder.fromUriString(targetUrl).queryParam("token", token).build().toUriString();
        }

        return "#";
    }

    private String getRegistrationId(Authentication authentication) {
        return ((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId();
    }

    private boolean isOAuth2Provider(Authentication authentication) {
        OAuth2AuthenticationToken authenticationToken = (OAuth2AuthenticationToken) authentication;
        return !authenticationToken.getAuthorizedClientRegistrationId().equals("local");
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

    private boolean isAuthorizedRedirectUri(String uri) {
        URI clientRedirectUri = URI.create(uri);

        return appProperties.getOauth2().getAuthorizedRedirectUris().stream().anyMatch(authorizedRedirectUri -> {
            // Only validate host and port. Let the clients use different paths if they want to
            URI authorizedURI = URI.create(authorizedRedirectUri);
            if (authorizedURI.getHost().equalsIgnoreCase(clientRedirectUri.getHost())
                    && authorizedURI.getPort() == clientRedirectUri.getPort()) {
                return true;
            }
            return false;
        });
    }
}
