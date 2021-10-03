package dk.simonsejse.loenberegning.database;

import android.annotation.SuppressLint;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.room.TypeConverter;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import dk.simonsejse.loenberegning.models.Extra;
import dk.simonsejse.loenberegning.serializers.ExtraCustomDeserializer;
import dk.simonsejse.loenberegning.serializers.ExtraCustomSerializer;
import dk.simonsejse.loenberegning.utilities.DateUtil;

public class ExtraConverter {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @TypeConverter
    public List<Extra> toExtraList(String extraListGson){
        if (extraListGson == null){
            return null;
        }
        final ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule simpleModule = new SimpleModule("", new Version(1, 0, 0, null, null, null));
        simpleModule.addDeserializer(Extra.class, new ExtraCustomDeserializer());
        objectMapper.registerModule(simpleModule);

        try {
            return objectMapper.readValue(extraListGson, new TypeReference<List<Extra>>(){});
        } catch (IOException e) {
            return new ArrayList<Extra>();
        }
    }

    @TypeConverter
    public String fromExtraListToGson(List<Extra> extras){
        if (extras == null){
            return null;
        }
        final ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule simpleModule = new SimpleModule("ExtraCustomSerializer", new Version(1, 0, 0, null, null, null));
        simpleModule.addSerializer(Extra.class, new ExtraCustomSerializer());
        objectMapper.registerModule(simpleModule);
        try {
            return objectMapper.writeValueAsString(extras);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

}
