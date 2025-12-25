package com.app.corechat.security;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.AuthenticationStatus;
import jakarta.security.enterprise.authentication.mechanism.http.HttpAuthenticationMechanism;
import jakarta.security.enterprise.authentication.mechanism.http.HttpMessageContext;
import jakarta.security.enterprise.credential.Credential;
import jakarta.security.enterprise.identitystore.CredentialValidationResult;
import jakarta.security.enterprise.identitystore.IdentityStoreHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@ApplicationScoped
public class CustomAuthenticationMechanism implements HttpAuthenticationMechanism {

    @Inject
    private IdentityStoreHandler identityStoreHandler;

    @Override
    public AuthenticationStatus validateRequest(HttpServletRequest request,
                                               HttpServletResponse response,
                                               HttpMessageContext context) {

        // Check if already authenticated (from session)
        if (context.isAuthenticationRequest()) {
            Credential credential = context.getAuthParameters().getCredential();

            if (credential != null) {
                CredentialValidationResult result = identityStoreHandler.validate(credential);

                if (result.getStatus() == CredentialValidationResult.Status.VALID) {
                    // CRITICAL: Tell container to remember this authentication in session
                    return context.notifyContainerAboutLogin(
                        result.getCallerPrincipal(),
                        result.getCallerGroups()
                    );
                }

                return AuthenticationStatus.SEND_FAILURE;
            }
        }

        // If there's a principal in session, authentication already happened
        if (context.getCallerPrincipal() != null) {
            return AuthenticationStatus.SUCCESS;
        }

        // No credentials and no existing authentication
        return AuthenticationStatus.NOT_DONE;
    }
}


