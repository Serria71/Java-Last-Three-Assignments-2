/*
 * Copyright 2015 Len Payne <len.payne@lambtoncollege.ca>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package message;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.UserTransaction;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

/**
 *
 * @author Len Payne <len.payne@lambtoncollege.ca>
 */
@Path("/message")
@RequestScoped
public class MessageREST {

    @PersistenceContext(unitName = "samplemessagePU")
    EntityManager em;

    @Inject
    UserTransaction transaction;

    List<Messages> messageList;

    @GET
    @Produces("application/json")
    public Response getAll() {
        JsonArrayBuilder json = Json.createArrayBuilder();
        Query q = em.createNamedQuery("Message.findAll");
        messageList = q.getResultList();
        for (Messages p : messageList) {
            json.add(p.toJSON());
        }
        return Response.ok(json.build().toString()).build();
    }

    @GET
    @Path("{id}")
    @Produces("application/json")
    public Response getById(@PathParam("id") int id) {
        Query q = em.createQuery("SELECT m FROM Message m WHERE m.messageId = :messageId");
        q.setParameter("messageId", id);
        Messages p = (Messages) q.getSingleResult();
        return Response.ok(p.toJSON().toString()).build();
    }
    
    @GET
    @Path("{startDate}/{endDate}")
    @Produces("application/json")
    public Response getByDate(@PathParam("startDate") String startDate, @PathParam("endDate") String endDate){
        
        //TimeZone tz = TimeZone.getTimeZone("UTC");
        //DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mmZ");
        //df.setTimeZone(tz);
        //String nowAsIso = df.format(new Date());
        
        JsonArrayBuilder json = Json.createArrayBuilder();
        Query q = em.createQuery("SELECT p FROM Message p WHERE p.messageDate IS BETWEEN :startDate AND :endDate");
        q.setParameter("startDate", startDate);
        q.setParameter("endDate", endDate);
        messageList = q.getResultList();
        for (Messages p : messageList){
            json.add(p.toJSON());        
        }
        return Response.ok(json.build().toString()).build();
    }   

    @POST
    @Consumes("application/json")
    public Response add(JsonObject json) {
        Response result;
        try {
            transaction.begin();
            Messages p = new Messages(json);
            em.persist(p);
            transaction.commit();
            result = Response.ok().build();
        } catch (Exception ex) {
            result = Response.status(500).entity(ex.getMessage()).build();
        }
        return result;
    }

    @PUT
    @Consumes("application/json")
    public Response edit(JsonObject json) {
        Response result;
        try {
            transaction.begin();
            Messages p = (Messages) em.createNamedQuery("Message.findByMessageId")
                    .setParameter("messageId", json.getInt("messageId")).getSingleResult();
            p.setTitle(json.getString("title"));
            p.setContents(json.getString("content"));
            p.setAuthor(json.getString("author"));
            em.persist(p);
            transaction.commit();
            result = Response.ok().build();
        } catch (Exception ex) {
            result = Response.status(500).entity(ex.getMessage()).build();
        }
        return result;
    }

    @DELETE
    @Path("{id}")
    public Response delete(@PathParam("id") int id) {
        Response result;
        try {
            transaction.begin();
            Messages p = (Messages) em.createNamedQuery("Message.findByMessageId")
                    .setParameter("messageId", id).getSingleResult();
            em.remove(p);
            transaction.commit();
            result = Response.ok().build();
        } catch (Exception ex) {
            result = Response.status(500).entity(ex.getMessage()).build();
        }
        return result;
    }
}
