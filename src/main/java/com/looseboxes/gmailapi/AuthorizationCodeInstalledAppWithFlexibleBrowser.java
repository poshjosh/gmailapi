package com.looseboxes.gmailapi;

import java.io.IOException;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.extensions.java6.auth.oauth2.VerificationCodeReceiver;
import com.google.api.client.util.Preconditions;

/**
 * OAuth 2.0 authorization code flow for an installed Java application that persists end-user
 * credentials.
 *
 * <p>
 * Implementation is thread-safe.
 * </p>
 *
 * @since 1.11
 * @author Yaniv Inbar
 */
public class AuthorizationCodeInstalledAppWithFlexibleBrowser {
  
  /** Authorization code flow. */
  private final AuthorizationCodeFlow flow;

  /** Verification code receiver. */
  private final VerificationCodeReceiver receiver;
  
  private final UrlBrowser urlBrowser;

  /**
   * @param flow authorization code flow
   * @param receiver verification code receiver
   */
  public AuthorizationCodeInstalledAppWithFlexibleBrowser(
      AuthorizationCodeFlow flow, VerificationCodeReceiver receiver, UrlBrowser urlBrowser) {
    this.flow = Preconditions.checkNotNull(flow);
    this.receiver = Preconditions.checkNotNull(receiver);
    this.urlBrowser = Preconditions.checkNotNull(urlBrowser);
  }

  /**
   * Authorizes the installed application to access user's protected data.
   *
   * @param userId user ID or {@code null} if not using a persisted credential store
   * @return credential
   */
  public Credential authorize(String userId) throws IOException {
    try {
      Credential credential = flow.loadCredential(userId);
      if (credential != null
          && (credential.getRefreshToken() != null || 
              credential.getExpiresInSeconds() == null || 
              credential.getExpiresInSeconds() > 60)) {
        return credential;
      }
      // open in browser
      String redirectUri = receiver.getRedirectUri();
      AuthorizationCodeRequestUrl authorizationUrl =
          flow.newAuthorizationUrl().setRedirectUri(redirectUri);
      onAuthorization(authorizationUrl);
      // receive authorization code and exchange it for an access token
      String code = receiver.waitForCode();
      TokenResponse response = flow.newTokenRequest(code).setRedirectUri(redirectUri).execute();
      // store credential and return it
      return flow.createAndStoreCredential(response, userId);
    } finally {
      receiver.stop();
    }
  }

  /**
   * Handles user authorization by redirecting to the OAuth 2.0 authorization server.
   *
   * <p>
   * Default implementation is to call {@code browse(authorizationUrl.build())}. Subclasses may
   * override to provide optional parameters such as the recommended state parameter. Sample
   * implementation:
   * </p>
   *
   * <pre>
  &#64;Override
  protected void onAuthorization(AuthorizationCodeRequestUrl authorizationUrl) throws IOException {
    authorizationUrl.setState("xyz");
    super.onAuthorization(authorizationUrl);
  }
   * </pre>
   *
   * @param authorizationUrl authorization URL
   * @throws IOException I/O exception
   */
  protected void onAuthorization(AuthorizationCodeRequestUrl authorizationUrl) throws IOException {
    urlBrowser.browseTo(authorizationUrl.build());
  }

  /** Returns the authorization code flow. */
  public final AuthorizationCodeFlow getFlow() {
    return flow;
  }

  /** Returns the verification code receiver. */
  public final VerificationCodeReceiver getReceiver() {
    return receiver;
  }
}
