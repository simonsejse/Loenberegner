package dk.simonsejse.loenberegning.database;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import dk.simonsejse.loenberegning.models.Extra;

public class ExtraConverter {
    @TypeConverter
    public List<Extra> toExtraList(String extraListGson){
        if (extraListGson == null){
            return null;
        }
        Gson gson = new Gson();
        final Type type = new TypeToken<List<Extra>>() {}.getType();
        return gson.fromJson(extraListGson, type);
    }

    @TypeConverter
    public String fromExtraListToGson(List<Extra> extras){
        if (extras == null){
            return null;
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Extra>>(){}.getType();
        return gson.toJson(extras, type);
    }
}
