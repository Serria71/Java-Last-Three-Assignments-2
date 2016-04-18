/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package message;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.json.Json;
import javax.json.JsonObject;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author c0640780
 */
public class Messages implements Serializable {

     
    int id;
    private String title;
    private String contents;
    private String author;
    String senttime;
    
    public Messages(int id, String title, String author, String contents, String senttime) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.contents = contents;
        this.senttime = senttime;
    }
    
    public Messages(JsonObject json) {
        // Requires complex conversion as jquery.val() does not set JSON Type
        id = Integer.parseInt(json.getString("MessageID", "0"));
        title = json.getString("Title");
        contents = json.getString("Contents");
        author = json.getString("Author");
        senttime = json.getString("Time");
    }

    public int getId() {
        return id;
    }

    public void setId(int messageId) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getSenttime() {
        return senttime;
    }

    public void setSenttime(String senttime) {
        this.senttime = senttime;
    }
    
    public JsonObject toJSON() {
        return Json.createObjectBuilder()
                .add("MessageID", id)
                .add("Name", title)
                .add("Content", contents)
                .add("Author", author)
                .add("Time", senttime)
                .build();
    }
}
