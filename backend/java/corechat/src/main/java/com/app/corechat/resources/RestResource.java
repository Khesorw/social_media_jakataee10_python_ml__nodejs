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
package com.app.corechat.resources;

import java.util.List;
import java.util.logging.Logger;

import com.app.corechat.api_pojos.FeedMsgSummaryDTO;
import com.app.corechat.business.user.UserService;
import com.app.corechat.entities.Conversation;
import com.app.corechat.entities.ConversationParticipant;
import com.app.corechat.entities.Message;
import com.app.corechat.entities.TestUser;
import com.app.corechat.entities.User;

import jakarta.ejb.EJB;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import jakarta.security.enterprise.identitystore.PasswordHash;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import jakarta.ws.rs.core.Response;

@Path("hello")
public class RestResource {

    @Inject
    private PasswordHash passwordHash;

    @EJB
    private UserService userService;

    private final static Logger LOG = Logger.getLogger(RestResource.class.getName());

    @jakarta.persistence.PersistenceContext(unitName = "MyPU")
    private EntityManager em;
    @GET
    public Response hel() {
        return Response.ok("Hello from Response").build();
    }

    @Path("test")
    @GET
    @Produces(APPLICATION_JSON)
    public Response test() {
        return Response
                .ok(em.createQuery("select t from TestUser t order by t.email asc", TestUser.class).getResultList())
                .build();

    }
    
    @Path("wok")
    @GET
    public Response wok() {

        return Response.ok("hello from wok").build();
    }



    @Path("genhash")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hash(@QueryParam("p") String password) {

        if (password == null || password.isBlank())
            return "provide ?p=yourpassword";

        return passwordHash.generate(password.toCharArray());
    }


    

    @Path("eml")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findUserByEmail(@QueryParam("email") String email) {

        if (email == null)
            return Response.status(Response.Status.BAD_REQUEST).build();

        String queryString = "SELECT u FROM User u where u.email = :email";

        TypedQuery<User> query = em.createQuery(queryString, User.class);

        query.setParameter("email", email);

        return Response.ok(query.getResultList()).build();
    }


    @Path("regist")
    @POST
    public Response signup(User user) {
        userService.persist(user);

        return Response.status(Response.Status.CREATED).build();
    }
    

    @Path("check")
    @GET
    public Response isPart(@QueryParam("partId") Long partId, @QueryParam("usId") Long usId) {
        String jpql = "SELECT COUNT(cp1) "
                + "FROM ConversationParticipant cp1 "
                + "WHERE cp1.user.id= :senderId AND "
                + "cp1.conversation.id = :conversationId";

        Long count = em.createQuery(jpql, Long.class)
                .setParameter("senderId", usId)
                .setParameter("conversationId", partId)
                .getSingleResult();

        String c = String.valueOf(count > 0);

        return Response.ok(c).build();

    }

    
    @Path("findid")
    @GET

    public Response getidbyemail(@QueryParam("eml") String eml) {

        if (eml == null || eml.isBlank()) {
            throw new IllegalArgumentException("balnk or null email ?eml=?");
        }

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);

        Root<User> user = cq.from(User.class);

        cq.select(user.get("id"));

        cq.where(cb.equal(user.get("email"), eml));

        Long userId = em.createQuery(cq).getSingleResult();

        if (userId != null) {
            return Response.ok(String.valueOf(userId)).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }





    @Path("doesCon")
    @GET
    public Response doesConverExists() {

        Long me = 8l;
        Long other = 4l;
        Long c=null;

            try {
            
            String jpql = "SELECT cp.conversation.id FROM ConversationParticipant cp WHERE cp.user.id IN (:me, :other) "
                    + " GROUP BY cp.conversation.id HAVING COUNT(cp.conversation.id) = 2";
            
             c = em.createQuery(jpql, Long.class)
                   .setParameter("me", me)
                   .setParameter("other", other)
                    .getSingleResult();

            return Response.ok(c).build();

        } catch (NoResultException e) {
            return Response.ok(c).build();
        }
        

        
    }











    
    @Path("convPart")
    @GET
    @Produces(MediaType.APPLICATION_JSON)

    public Response getConv() {
        int userId = 3;

        // String jpql = "SELECT pc.conversation.id, pc.user.id " + 
        //                 " FROM ConversationParticipant pc " + 
        //                 " WHERE pc.conversation.id IN ( " + 
        //                 "    SELECT pc2.conversation.id " + 
        //                 "    FROM ConversationParticipant pc2 " + 
        //                 "    WHERE pc2.user.id = 3 " + 
        //                 ") " + 
        //         "AND pc.user.id <> 3 "; 

        // String jpql = "SELECT m FROM Message m WHERE m.conversation.id = :convId ORDER BY m.createdAt DESC";

        // TypedQuery<Message> query = em.createQuery(jpql, Message.class);
        // query.setParameter("convId", 2L);
        // query.setMaxResults(1); // <--- This is how you do "LIMIT 1" in JPA

        // Message lastMessage = (Message) query.getSingleResult();

        // LastMessageDTO lastMessageDTO = new LastMessageDTO(lastMessage.getConversation().getId(), lastMessage.getMessageText(),lastMessage.getSender().getId());
        // String jpql = "SELECT " + 
        //                 "  c.id, " + 
        //                 "  otherUser.id, " + 
        //                 "  otherUser.name, " + 
        //                 "  m.message_txt, " + 
        //                 "  m.sender_id, " + 
        //                 "  m.created_at " + 
        //                 "FROM conversation c " + 
        //                 "JOIN participant_conversation pc1 ON pc1.conversation_id = c.id " + 
        //                 "JOIN participant_conversation pc2 ON pc2.conversation_id = c.id " + 
        //                 "JOIN user_info otherUser ON otherUser.id = pc2.user_id " + 
        //                 "JOIN messages m ON m.conversation_id = c.id " + 
        //                 "WHERE pc1.user_id = :me " + 
        //                 "  AND pc2.user_id <> :me " + 
        //                 "  AND m.created_at = ( " + 
        //                 "      SELECT MAX(m2.created_at) " + 
        //                 "      FROM messages m2 " + 
        //                 "      WHERE m2.conversation_id = c.id " + 
        //         "  ) ";

        /*       CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
        
        Root<Conversation> conversation = cq.from(Conversation.class);
        Root<ConversationParticipant> pc1 = cq.from(ConversationParticipant.class);
        Root<ConversationParticipant> pc2 = cq.from(ConversationParticipant.class);
        Root<Message> message = cq.from(Message.class);
        
        Subquery<OffsetDateTime> maxCreatedAt = cq.subquery(OffsetDateTime.class);
        Root<Message> m2 = maxCreatedAt.from(Message.class);
        
        Expression<OffsetDateTime> latestDate = cb.greatest(m2.<OffsetDateTime>get("createdAt"));
        maxCreatedAt.select(latestDate);
        maxCreatedAt.where(cb.equal(m2.get("conversation"), conversation));
        cq.multiselect(
            conversation.get("id"),
            pc2.get("user").get("username"),
            message.get("messageText"),
            message.get("sender").get("id"),
            message.get("createdAt")
            
        );
        
        cq.where(
            cb.equal(pc1.get("conversation"), conversation),
            cb.equal(pc2.get("conversation"), conversation),
            cb.equal(message.get("conversation"), conversation),
            cb.equal(pc1.get("user").get("id"), userId),
            cb.notEqual(pc2.get("user").get("id"), userId),
            cb.equal(message.get("createdAt"), maxCreatedAt)
        
        );
        
        List<Object[]> feedList = em.createQuery(cq).getResultList(); */

        //now using joins instead mutipleselect
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<FeedMsgSummaryDTO> cq = cb.createQuery(FeedMsgSummaryDTO.class);

        // ROOT
        Root<Conversation> conversation = cq.from(Conversation.class);

        // JOINS
        Join<Conversation, ConversationParticipant> mePart = conversation.join("participant");

        Join<Conversation, ConversationParticipant> otherPart = conversation.join("participant");

        Join<ConversationParticipant, User> otherUser = otherPart.join("user");

        Join<Conversation, Message> message = conversation.join("messages");

        // SUBQUERY: last message id per conversation
        Subquery<Long> lastMessageId = cq.subquery(Long.class);
        Root<Message> m2 = lastMessageId.from(Message.class);

        lastMessageId.select(cb.max(m2.get("id")))
                .where(cb.equal(m2.get("conversation"), conversation));

        // SELECT (constructor projection)
        cq.select(cb.construct(
                FeedMsgSummaryDTO.class,
                conversation.get("id"),
                otherUser.get("username"),
                message.get("messageText"),
                message.get("sender").get("id"),
                message.get("createdAt")));

        // WHERE
        cq.where(
                cb.equal(mePart.get("user").get("id"), userId),
                cb.notEqual(otherUser.get("id"), userId),
                cb.equal(message.get("id"), lastMessageId));

        cq.distinct(true);
        List<FeedMsgSummaryDTO> ms = em.createQuery(cq).getResultList();
        return Response.ok(ms).build();

    }
    




  

}
