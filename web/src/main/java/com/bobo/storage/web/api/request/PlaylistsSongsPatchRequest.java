package com.bobo.storage.web.api.request;

import java.util.Collection;

public record PlaylistsSongsPatchRequest(PatchOperation op, Collection<String> urls) {

}
