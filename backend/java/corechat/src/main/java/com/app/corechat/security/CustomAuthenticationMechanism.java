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


        LOG.info("Validte uri reqeusti "+request.getRequestURI());
        //for login 
        if (context.isAuthenticationRequest()) {

            Credential credentials = context.getAuthParameters().getCredential();
            if (credentials != null) {
                CredentialValidationResult result = identityStoreHandler.validate(credentials);

                if (result.getStatus() == CredentialValidationResult.Status.VALID) {

                    return context.notifyContainerAboutLogin(result.getCallerPrincipal(), result.getCallerGroups());
                }

                return context.responseUnauthorized();
            }
        }

        //for authenticated user
                
        // if (context.getCallerPrincipal() != null) {
        //     return AuthenticationStatus.SUCCESS;
        // }

        //anonymous users
        return AuthenticationStatus.NOT_DONE;
        

    }





    // @Override
    // public AuthenticationStatus validateRequest(HttpServletRequest request,
    //                                            HttpServletResponse response,
    //                                            HttpMessageContext context) {

    //     LOG.info("🔐 validateRequest - URI: " + request.getRequestURI());

    //     // Check if already authenticated (from session)
    //     if (context.getCallerPrincipal() != null) {
    //         LOG.info("✅ Already authenticated: " + context.getCallerPrincipal().getName());
    //         return AuthenticationStatus.SUCCESS;
    //     }

    //     // Handle explicit authentication (login endpoint)
    //     if (context.isAuthenticationRequest()) {
    //         Credential credential = context.getAuthParameters().getCredential();

    //         if (credential != null) {
    //             LOG.info("🔑 Validating credentials...");
    //             CredentialValidationResult result = identityStoreHandler.validate(credential);

    //             if (result.getStatus() == CredentialValidationResult.Status.VALID) {
    //                 LOG.info("✅ Valid credentials for: " + result.getCallerPrincipal().getName());

    //                 return context.notifyContainerAboutLogin(
    //                     result.getCallerPrincipal(),
    //                     result.getCallerGroups()
    //                 );
    //             }

    //             LOG.warning("❌ Invalid credentials");
    //             return context.responseUnauthorized();
    //         }
    //     }

    //     // Protected resources without authentication
    //     if (context.isProtected()) {
    //         LOG.warning("❌ Protected resource accessed without authentication");
    //         return context.responseUnauthorized();
    //     }

    //     LOG.info("➡️ Not protected, returning NOT_DONE");
    //     return AuthenticationStatus.NOT_DONE;
    // }
}
















// package com.app.corechat.security;

// import jakarta.enterprise.context.ApplicationScoped;
// import jakarta.inject.Inject;
// import jakarta.security.enterprise.AuthenticationStatus;
// import jakarta.security.enterprise.authentication.mechanism.http.HttpAuthenticationMechanism;
// import jakarta.security.enterprise.authentication.mechanism.http.HttpMessageContext;
// import jakarta.security.enterprise.credential.Credential;
// import jakarta.security.enterprise.identitystore.CredentialValidationResult;
// import jakarta.security.enterprise.identitystore.IdentityStoreHandler;
// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.servlet.http.HttpServletResponse;

// @ApplicationScoped
// public class CustomAuthenticationMechanism implements HttpAuthenticationMechanism {

//     @Inject
//     private IdentityStoreHandler identityStoreHandler;

//     @Override
//     public AuthenticationStatus validateRequest(HttpServletRequest request,
//                                                HttpServletResponse response,
//                                                HttpMessageContext context) {

//         // Check if already authenticated (from session)
//         if (context.isAuthenticationRequest()) {
//             Credential credential = context.getAuthParameters().getCredential();

//             if (credential != null) {
//                 CredentialValidationResult result = identityStoreHandler.validate(credential);

//                 if (result.getStatus() == CredentialValidationResult.Status.VALID) {
//                     // CRITICAL: Tell container to remember this authentication in session
//                     return context.notifyContainerAboutLogin(
//                         result.getCallerPrincipal(),
//                         result.getCallerGroups()
//                     );
//                 }

//                 return AuthenticationStatus.SEND_FAILURE;
//             }
//         }

//         // If there's a principal in session, authentication already happened
//         if (context.getCallerPrincipal() != null) {
//             return AuthenticationStatus.SUCCESS;
//         }

//         // No credentials and no existing authentication
//         return AuthenticationStatus.NOT_DONE;
//     }
// }


