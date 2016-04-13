/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package message;

import java.io.Serializable;
import java.util.ArrayList;
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

    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
     
    private int messageId;
    private String title;
    private String contents;
    private String author;
    private String senttime;
    
    public Messages () {}
    
    public Messages(JsonObject json) {
        // Requires complex conversion as jquery.val() does not set JSON Type
        messageId = Integer.parseInt(json.getString("MessageID", "0"));
        title = json.getString("Title");
        contents = json.getString("Contents");
        author = json.getString("Author");
        senttime = json.getString("Time");
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
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
                .add("ProductID", messageId)
                .add("Name", title)
                .add("Content", contents)
                .add("Author", author)
                .add("Time", senttime)
                .build();
    }
}
