package com.sqide.sqdbg;

import com.sqide.sqdbg.protocol.DlvRequest;
import com.sqide.sqdbg.protocol.DlvResponse;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.containers.ContainerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.concurrency.Promise;
import org.jetbrains.rpc.CommandProcessor;
import org.jetbrains.rpc.CommandProcessorKt;
import org.jetbrains.rpc.RequestCallback;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

public abstract class DlvCommandProcessor extends CommandProcessor<JsonReaderEx, DlvResponse, DlvResponse> {
  @Nullable
  @Override
  public DlvResponse readIfHasSequence(@NotNull JsonReaderEx message) {
    return new DlvResponse.CommandResponseImpl(message, null);
  }

  @Override
  public int getSequence(@NotNull DlvResponse response) {
    return response.id();
  }

  @Override
  public void acceptNonSequence(JsonReaderEx message) {
  }

  public void processIncomingJson(@NotNull JsonReaderEx reader) {
    getMessageManager().processIncoming(reader);
  }

  @Override
  public void call(@NotNull DlvResponse response, @NotNull RequestCallback<DlvResponse> callback) {
    if (response.result() != null) {
      callback.onSuccess(response, this);
    }
    else {
      callback.onError(Promise.createError(createMessage(response)));
    }
  }

  @NotNull
  private static String createMessage(@NotNull DlvResponse r) {
    DlvResponse.ErrorInfo e = r.error();
    if (e == null) return "Internal messaging error";
    List<String> data = e.data();
    String message = e.message();
    if (ContainerUtil.isEmpty(data)) return StringUtil.defaultIfEmpty(message, "<null>");
    List<String> list = ContainerUtil.newSmartList(message);
    list.addAll(data);
    return list.toString();
  }

  @NotNull
  @Override
  public <RESULT> RESULT readResult(@NotNull String method, @NotNull DlvResponse successResponse) {
    JsonReaderEx result = successResponse.result();
    assert result != null : "success result should be not null";
    JsonReader reader = result.asGson();
    Object o = new GsonBuilder().create().fromJson(reader, getResultType(method.replaceFirst("RPCServer\\.", "")));
    //noinspection unchecked
    return (RESULT)o;
  }

  @NotNull
  private static Type getResultType(@NotNull String method) {
    for (Class<?> c : DlvRequest.class.getDeclaredClasses()) {
      if (method.equals(c.getSimpleName())) {
        Type s = c.getGenericSuperclass();
        assert s instanceof ParameterizedType : c.getCanonicalName() + " should have a generic parameter for correct callback processing";
        Type[] arguments = ((ParameterizedType)s).getActualTypeArguments();
        assert arguments.length == 1 : c.getCanonicalName() + " should have only one generic argument for correct callback processing";
        return arguments[0];
      }
    }
    CommandProcessorKt.getLOG().error("Unknown response " + method + ", please register an appropriate request into com.sqide.sqdbg.protocol.DlvRequest");
    return Object.class;
  }
}