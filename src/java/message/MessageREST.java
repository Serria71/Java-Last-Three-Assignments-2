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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
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

    List<Messages> messageList;
       
    @GET
    @Produces("application/json")
    public Response getAll() {
        JsonArrayBuilder json = Json.createArrayBuilder();
        try (Connection conn = DBUtils.getConnection()){
            messageList = new ArrayList<>();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM messages");
            while (rs.next()){
                Messages m = new Messages(
                    rs.getInt("messageId"),
                    rs.getString("title"),
                    rs.getString("author"),
                    rs.getString("content"),
                    rs.getString("senttime")
                );
                messageList.add(m);
            }
            for (Messages p : messageList){
                json.add(p.toJSON());        
            }
            return Response.ok(json.build().toString()).build();
        } catch (SQLException ex){
            
        }
        return Response.ok(json.build().toString()).build();
    }

    @GET
    @Path("{id}")
    @Produces("application/json")
    public Response getById(@PathParam("id") int id) {
        JsonArrayBuilder json = Json.createArrayBuilder();
        try (Connection conn = DBUtils.getConnection()){
            
            messageList = new ArrayList<>();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM messages WHERE messageId = " + id);
            while (rs.next()){
                Messages m = new Messages(
                    rs.getInt("messageId"),
                    rs.getString("title"),
                    rs.getString("author"),
                    rs.getString("content"),
                    rs.getString("senttime")
                );
                messageList.add(m);
            }
            for (Messages p : messageList){
                json.add(p.toJSON());        
            }
            return Response.ok(json.build().toString()).build();
        } catch (SQLException ex){
            
        }
        return Response.ok(json.build().toString()).build();
    }
    
    @GET
    @Path("{startDate}/{endDate}")
    @Produces("application/json")
    public Response getByDate(@PathParam("startDate") String startDate, @PathParam("endDate") String endDate){
        JsonArrayBuilder json = Json.createArrayBuilder();
        //TimeZone tz = TimeZone.getTimeZone("UTC");
        //DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mmZ");
        //df.setTimeZone(tz);
        //String nowAsIso = df.format(new Date());
        try (Connection conn = DBUtils.getConnection()){
            messageList = new ArrayList<>();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM messages WHERE senttime  IS BETWEEN " + startDate + " AND " + endDate);
            while (rs.next()){
                Messages m = new Messages(
                    rs.getInt("messageId"),
                    rs.getString("title"),
                    rs.getString("author"),
                    rs.getString("content"),
                    rs.getString("senttime")
                );
                messageList.add(m);
            }
            for (Messages p : messageList){
                json.add(p.toJSON());        
            }
            return Response.ok(json.build().toString()).build();
        } catch (SQLException ex){
            
        }
        return Response.ok(json.build().toString()).build();
    }   

    @POST
    @Consumes("application/json")
    public Response add(JsonObject json) {
        
        Response result;
        int id = json.getInt("messageId");
        String title = json.getString("title");
        String content = json.getString("content");
        String author = json.getString("author");
        String senttime = json.getString("senttime");
        
        try (Connection conn = DBUtils.getConnection()){
            Statement stmt = conn.createStatement();
            stmt.executeQuery("INSERT INTO messages VALUES (" + id + "\"" + title + "\", \"" + content + "\","
                    + " \"" + author + "\", " + "\"" + senttime + "\");");
            result = Response.ok().build();
        } catch (SQLException ex){
            result = Response.status(500).entity(ex.getMessage()).build();
        }
        return result;
    }

    @PUT
    @Consumes("application/json")
    public Response edit(JsonObject json) {
        Response result;
        
        int id = json.getInt("messageId");
        String title = json.getString("title");
        String content = json.getString("content");
        String author = json.getString("author");
        String senttime = json.getString("senttime");
        //Not 100% sure how to get this part working without putting blanks into the data when nothing is entered.
        try (Connection conn = DBUtils.getConnection()){
            Statement stmt = conn.createStatement();
            stmt.executeQuery("UPDATE messages WHERE id = " + id + " SET ");
            result = Response.ok().build();
        } catch (SQLException ex){
            result = Response.status(500).entity(ex.getMessage()).build();
        }
        return result;
    }

    @DELETE
    @Path("{id}")
    public Response delete(@PathParam("id") int id) {
        Response result;
        try (Connection conn = DBUtils.getConnection()){
            Statement stmt = conn.createStatement();
            stmt.executeQuery("DELETE * FROM messages WHERE id = " + id);
            result = Response.ok().build();
        } catch (Exception ex) {
            result = Response.status(500).entity(ex.getMessage()).build();
        }
        return result;
    }
}
