package dk.simonsejse.loenberegning.serializers;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import dk.simonsejse.loenberegning.models.Extra;
import dk.simonsejse.loenberegning.utilities.ParseUtil;

@RequiresApi(api = Build.VERSION_CODES.O)
public class ExtraCustomDeserializer extends StdDeserializer<Extra> {

    public ExtraCustomDeserializer(){
        this(null);
    }

    protected ExtraCustomDeserializer(Class<?> vc) {
        super(vc);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public Extra deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        final Extra extra = new Extra();
        final ObjectCodec objectCodec = jp.getCodec();
        JsonNode jsonNode = objectCodec.readTree(jp);
        final JsonNode jsonNode1 = jsonNode.get("start");
        final JsonNode jsonNode2 = jsonNode.get("end");
        final JsonNode jsonNode3 = jsonNode.get("integer");

        try{
            final LocalDateTime start = ParseUtil.parseToLocalDateTime(jsonNode1.asText());
            final LocalDateTime end = ParseUtil.parseToLocalDateTime(jsonNode2.asText());
            final int i = Integer.parseInt(jsonNode3.asText());

            extra.setStart(start);
            extra.setEnd(end);
            extra.setInteger(i);
        }catch(DateTimeParseException | NumberFormatException e){
            System.out.println(e.getMessage());
        }

        return extra;
    }
}
