package ua.kiev.prog.jsonUploaders;

import com.google.gson.Gson;
import java.io.*;
import java.lang.reflect.Type;
import java.util.List;

public abstract class JsonPost<T> {
    protected String path;
    protected String placeToSave;
    protected String placeToUpload;
    protected Gson gsonForSave;
    protected Gson gsonForUpload;
    protected Type listType;

    public JsonPost(String path) {
        this.path = path;
    }
    public List<T> uploadJsonForFile(){
        try(Reader reader = new FileReader(path+placeToUpload)){
            List<T> newList = gsonForUpload.fromJson(reader, listType);
            return newList;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void saveJsonForFile(List<T> list){
        String jsonString = gsonForSave.toJson(list);
        try (FileWriter fileWriter = new FileWriter(path + placeToSave)) {
            fileWriter.write(jsonString);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
