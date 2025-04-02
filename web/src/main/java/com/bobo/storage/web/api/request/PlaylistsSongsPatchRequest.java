package com.bobo.storage.web.api.request;

import java.util.Collection;

import static com.bobo.storage.web.api.request.PatchOperation.ADD;

public class PlaylistsSongsPatchRequest {

  public PatchOperation op = ADD;

  public Collection<String> urls;

}
