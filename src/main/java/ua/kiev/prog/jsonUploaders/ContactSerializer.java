package ua.kiev.prog.jsonUploaders;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import ua.kiev.prog.Contact;

import java.lang.reflect.Type;

public class ContactSerializer implements JsonSerializer<Contact> {
    @Override
    public JsonElement serialize(Contact contact, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", contact.getId());
        jsonObject.addProperty("name", contact.getName());
        jsonObject.addProperty("surname", contact.getSurname());
        jsonObject.addProperty("phone", contact.getPhone());
        jsonObject.addProperty("email", contact.getEmail());
        if (contact.getGroup() != null) {
            jsonObject.addProperty("groupName", contact.getGroup().getName());
        }
        return jsonObject;
    }
}
