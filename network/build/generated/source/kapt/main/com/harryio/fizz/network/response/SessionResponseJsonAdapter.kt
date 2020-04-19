// Code generated by moshi-kotlin-codegen. Do not edit.
package com.harryio.fizz.network.response

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import com.squareup.moshi.internal.Util
import java.lang.NullPointerException
import kotlin.String
import kotlin.Suppress
import kotlin.collections.emptySet
import kotlin.text.buildString

@Suppress("DEPRECATION", "unused", "ClassName", "REDUNDANT_PROJECTION")
class SessionResponseJsonAdapter(
  moshi: Moshi
) : JsonAdapter<SessionResponse>() {
  private val options: JsonReader.Options = JsonReader.Options.of("session_id")

  private val stringAdapter: JsonAdapter<String> = moshi.adapter(String::class.java, emptySet(),
      "sessionId")

  override fun toString(): String = buildString(37) {
      append("GeneratedJsonAdapter(").append("SessionResponse").append(')') }

  override fun fromJson(reader: JsonReader): SessionResponse {
    var sessionId: String? = null
    reader.beginObject()
    while (reader.hasNext()) {
      when (reader.selectName(options)) {
        0 -> sessionId = stringAdapter.fromJson(reader) ?: throw Util.unexpectedNull("sessionId",
            "session_id", reader)
        -1 -> {
          // Unknown name, skip it.
          reader.skipName()
          reader.skipValue()
        }
      }
    }
    reader.endObject()
    return SessionResponse(
        sessionId = sessionId ?: throw Util.missingProperty("sessionId", "session_id", reader)
    )
  }

  override fun toJson(writer: JsonWriter, value: SessionResponse?) {
    if (value == null) {
      throw NullPointerException("value was null! Wrap in .nullSafe() to write nullable values.")
    }
    writer.beginObject()
    writer.name("session_id")
    stringAdapter.toJson(writer, value.sessionId)
    writer.endObject()
  }
}
