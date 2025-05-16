package com.bobo.storage.oembed;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Optional;

/**
 * @param type            The resource type.
 *                        For this use case we only care about validating the existence the content,
 *                        and any metadata about it. We may also forward the provider information for another consumer to retrieve
 *                        the embedded content.
 * @param version         The oEmbed version number. This must be {@code 1.0}.
 * @param title           A text title, describing the resource.
 * @param authorName      The name of the author/owner of the resource.
 * @param authorUrl       A URL for the author/owner of the resource.
 * @param providerName    The name of the resource provider.
 * @param providerUrl     The url of the resource provider.
 * @param cacheAge        The suggested cache lifetime for this resource, in seconds.
 *                        Consumers may choose to use this value or not.
 * @param thumbnailUrl    A URL to a thumbnail image representing the resource.
 *                        The thumbnail must respect any {@code maxwidth} and {@code maxheight} parameters.
 *                        If this parameter is present, {@code thumbnail_width} and {@code thumbnail_height} must also be present.
 * @param thumbnailHeight The width of the optional thumbnail.
 *                        If this parameter is present, {@code thumbnail_url} and {@code thumbnail_height} must also be present.
 * @param thumbnailWidth  The height of the optional thumbnail.
 *                        If this parameter is present, {@code thumbnail_url} and {@code thumbnail_width} must also be present.
 * @see <a href="https://oembed.com/#section2.3">2.3.4. Response Parameters</a>
 */
public record OEmbedResponse(
        String type,
        String version,
        Optional<String> title,
        @JsonProperty("author_name") Optional<String> authorName,
        @JsonProperty("author_url") Optional<String> authorUrl,
        @JsonProperty("provider_name") Optional<String> providerName,
        @JsonProperty("provider_url") Optional<String> providerUrl,
        @JsonProperty("cache_age") Optional<String> cacheAge,
        @JsonProperty("thumbnail_url") Optional<String> thumbnailUrl,
        @JsonProperty("thumbnail_height") Optional<String> thumbnailHeight,
        @JsonProperty("thumbnail_width") Optional<String> thumbnailWidth
) {

}
