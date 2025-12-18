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
    public jakarta.security.enterprise.AuthenticationStatus validateRequest(HttpServletRequest request,
                 HttpServletResponse response,
            HttpMessageContext context) {
                    
        Credential credential = context.getAuthParameters().getCredential();

        if (credential != null) {
            CredentialValidationResult result = identityStoreHandler.validate(credential);
            if (result.getStatus() == CredentialValidationResult.Status.VALID) {

                context.notifyContainerAboutLogin(result);
                return AuthenticationStatus.SUCCESS;
                
            } //if()

             return AuthenticationStatus.SEND_FAILURE;
        }//if()

        return AuthenticationStatus.NOT_DONE;


        
        

    }
    
}
