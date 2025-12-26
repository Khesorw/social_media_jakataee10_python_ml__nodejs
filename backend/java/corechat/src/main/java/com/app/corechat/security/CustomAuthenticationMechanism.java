package com.app.corechat.security;
import java.util.logging.Logger;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.AuthenticationStatus;
import jakarta.security.enterprise.authentication.mechanism.http.AutoApplySession;
import jakarta.security.enterprise.authentication.mechanism.http.HttpAuthenticationMechanism;
import jakarta.security.enterprise.authentication.mechanism.http.HttpMessageContext;
import jakarta.security.enterprise.credential.Credential;
import jakarta.security.enterprise.identitystore.CredentialValidationResult;
import jakarta.security.enterprise.identitystore.IdentityStoreHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@ApplicationScoped
@AutoApplySession
public class CustomAuthenticationMechanism implements HttpAuthenticationMechanism {

    private static final Logger LOG = Logger.getLogger(CustomAuthenticationMechanism.class.getName());

    @Inject
    private IdentityStoreHandler identityStoreHandler;


    @Override
    public AuthenticationStatus validateRequest(HttpServletRequest request, HttpServletResponse response,
            HttpMessageContext context) {


        LOG.info(()->"Validte uri reqeusti "+request.getRequestURI());
        //for login 
        // if (context.isAuthenticationRequest()) {

            Credential credentials = context.getAuthParameters().getCredential();
            if (credentials != null) {
                CredentialValidationResult result = identityStoreHandler.validate(credentials);

              

                if (result.getStatus() == CredentialValidationResult.Status.VALID) {

                    LOG.info(()->"GROUP OR RESULT IS " + result.getCallerGroups());
                    LOG.info(()->"Validation status is " + result.getStatus());
                    LOG.info(()->"Principal is: "+result.getCallerPrincipal() == null ? "null" : result.getCallerPrincipal().getName());

                    return context.notifyContainerAboutLogin(result.getCallerPrincipal(), result.getCallerGroups());
                }

                return context.responseUnauthorized();
            }
        // }

        /* for authenticated user */      
        if (context.getCallerPrincipal() != null) {
            LOG.info("USER IS ALREADY AUTHENTICATED ");
            return AuthenticationStatus.SUCCESS;
        }

        LOG.info("UNAUTHENTICATED USER ");
        //anonymous users
        return AuthenticationStatus.NOT_DONE;
        

    }

}


