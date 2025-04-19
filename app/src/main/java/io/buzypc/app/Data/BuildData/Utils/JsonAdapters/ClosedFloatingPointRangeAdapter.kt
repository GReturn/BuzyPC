package io.buzypc.app.Data.BuildData.Utils.JsonAdapters

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type

class ClosedFloatingPointRangeAdapter : JsonSerializer<ClosedFloatingPointRange<Double>>,
JsonDeserializer<ClosedFloatingPointRange<Double>> {
    override fun serialize(
        src: ClosedFloatingPointRange<Double>?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonObject().apply {
            addProperty("min", src?.start)
            addProperty("max", src?.endInclusive)
        }
    }

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext?
    ): ClosedFloatingPointRange<Double> {
        val jsonObject = json.asJsonObject
        val start = jsonObject.get("min").asDouble
        val endInclusive = jsonObject.get("max").asDouble
        return start..endInclusive
    }
}