/********************************************************************************
 * Copyright (c) 10/19/2022 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0, or the Eclipse Distribution License
 * v1.0 which is available at
 * https://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: EPL-2.0 OR BSD-3-Clause
 ********************************************************************************/
package com.app.corechat;

import jakarta.annotation.security.DeclareRoles;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.security.enterprise.identitystore.DatabaseIdentityStoreDefinition;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;







@ApplicationScoped
@DeclareRoles("USER")
@DatabaseIdentityStoreDefinition(
    dataSourceLookup = "jdbc/myappdb",
    callerQuery = "SELECT user_pass FROM user_info WHERE email = ?",
    groupsQuery = "SELECT 'USER' FROM user_info WHERE email = ?",
    hashAlgorithm = jakarta.security.enterprise.identitystore.Pbkdf2PasswordHash.class
)
@ApplicationPath("core")
public class ApplicationConfig extends Application {
}
