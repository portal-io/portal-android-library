/*
 * Copyright (C) 2015 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.whaleyvr.core.network.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.whaley.core.utils.StrUtil;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.Buffer;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * A {@linkplain Converter.Factory converter} which uses Gson for JSON.
 * <p>
 * Because Gson is so flexible in the types it supports, this converter assumes that it can handle
 * all types. If you are mixing JSON serialization with something else (such as protocol buffers),
 * you must {@linkplain Retrofit.Builder#addConverterFactory(Converter.Factory) add this instance}
 * last to allow the other converters a chance to see their types.
 */
public final class GsonConverterFactory extends Converter.Factory {
  /**
   * Create an instance using a default {@link Gson} instance for conversion. Encoding to JSON and
   * decoding from JSON (when no charset is specified by a header) will use UTF-8.
   */
  public static GsonConverterFactory create() {
    GsonBuilder builder=new GsonBuilder();
    builder.registerTypeHierarchyAdapter(List.class, new JsonDeserializer<List<?>>() {
      @Override
      public List<?> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json.isJsonArray()) {
          JsonArray array = json.getAsJsonArray();
          Type itemType = ((ParameterizedType) typeOfT).getActualTypeArguments()[0];
          List list = new ArrayList();
          for (int i = 0; i < array.size(); i++) {
            JsonElement element = array.get(i);
            Object item = context.deserialize(element, itemType);
            list.add(item);
          }
          return list;
        } else {
          //和接口类型不符，返回空List
          return Collections.EMPTY_LIST;
        }
      }
    });
    Gson gson=builder.create();
    return create(gson);
  }

  /**
   * Create an instance using {@code gson} for conversion. Encoding to JSON and
   * decoding from JSON (when no charset is specified by a header) will use UTF-8.
   */
  public static GsonConverterFactory create(Gson gson) {
    return new GsonConverterFactory(gson);
  }

  private final Gson gson;

  private GsonConverterFactory(Gson gson) {
    if (gson == null) throw new NullPointerException("gson == null");
    this.gson = gson;
  }

  @Override
  public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations,
      Retrofit retrofit) {
    TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
    return new GsonResponseBodyConverter<>(gson, adapter);
  }

  @Override
  public Converter<?, RequestBody> requestBodyConverter(Type type,
      Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
    TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
    return new GsonRequestBodyConverter<>(gson, adapter);
  }

  /*
 * Copyright (C) 2015 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

  static final class GsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final TypeAdapter<T> adapter;

    GsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
      this.gson = gson;
      this.adapter = adapter;
    }

    @Override public T convert(ResponseBody value) throws IOException {
      String json=value.string();
      if(!StrUtil.isEmpty(json)){
        if(json.contains(",\"data\":[]")){
            json=json.replace(",\"data\":[]","");
        }

        if(json.contains(",\"data\":{}")){
          json=json.replace(",\"data\":{}","");
        }

        if(json.contains(",\"data\":null")){
          json=json.replace(",\"data\":null","");
        }

        if(json.contains(",\"data\":\"\"")){
          json=json.replace(",\"data\":\"\"","");
        }

        if(json.contains(",\"data\":\"null\"")){
          json=json.replace(",\"data\":\"null\"","");
        }

      }

      JsonReader jsonReader = gson.newJsonReader(new StringReader(json));
      jsonReader.setLenient(true);
//              gson.newJsonReader(value.charStream());
      try {
        return adapter.read(jsonReader);
      } finally {
        value.close();
      }
    }
  }


   static class GsonRequestBodyConverter<T> implements Converter<T, RequestBody> {
     static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");
     static final Charset UTF_8 = Charset.forName("UTF-8");

    private final Gson gson;
    private final TypeAdapter<T> adapter;

    GsonRequestBodyConverter(Gson gson, TypeAdapter<T> adapter) {
      this.gson = gson;
      this.adapter = adapter;
    }

    @Override public RequestBody convert(T value) throws IOException {
      Buffer buffer = new Buffer();
      Writer writer = new OutputStreamWriter(buffer.outputStream(), UTF_8);
      JsonWriter jsonWriter = gson.newJsonWriter(writer);
      adapter.write(jsonWriter, value);
      jsonWriter.close();
      return RequestBody.create(MEDIA_TYPE, buffer.readByteString());
    }
  }

}
