package ua.kiev.prog.jsonUploaders;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import ua.kiev.prog.Contact;

import java.util.List;

public class JsonContactManager extends JsonPost<Contact> {
    public JsonContactManager(String path) {
        super(path);
        listType = new TypeToken<List<Contact>>() { }.getType();
        gsonForSave = new GsonBuilder()
                .registerTypeAdapter(Contact.class, new ContactSerializer())
                .create();
        gsonForUpload = new GsonBuilder()
                .registerTypeAdapter(Contact.class, new ContactDeserializer())
                .create();
        placeToSave="saveContacts.json";
        placeToUpload="uploadContacts.json";
    }

}
