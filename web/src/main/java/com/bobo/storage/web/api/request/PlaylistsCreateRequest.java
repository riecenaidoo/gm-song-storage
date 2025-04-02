package com.bobo.storage.web.api.request;

import java.util.Collection;

public record PlaylistsCreateRequest(String name, Collection<String> songs) {

}
