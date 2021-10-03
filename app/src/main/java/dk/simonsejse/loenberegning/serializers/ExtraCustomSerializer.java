package dk.simonsejse.loenberegning.serializers;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.util.List;

import dk.simonsejse.loenberegning.models.Extra;
import dk.simonsejse.loenberegning.utilities.DateUtil;

public class ExtraCustomSerializer extends StdSerializer<Extra> {

    public ExtraCustomSerializer(){
        this(null);
    }

    protected ExtraCustomSerializer(Class<Extra> t) {
        super(t);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void serialize(Extra extra, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
        jgen.writeStartObject();
        jgen.writeStringField("start", extra.start.format(DateUtil.DATE_TIME_FORMATTER));
        jgen.writeStringField("end", extra.end.format(DateUtil.DATE_TIME_FORMATTER));
        jgen.writeNumberField("integer", extra.integer);
        jgen.writeEndObject();
    }
}
