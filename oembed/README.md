## Problem

In the **domain**, a **Song** is defined as, '*a reference to a Song on the internet,*' and it is uniquely identified by its `url` property. One of the goals for the system is to validate these URLs. An `HTTP GET` is insufficient (*especially in the case of YouTube URLs*) as many providers will return `200 OK` and redirect to a default landing page with an error-like message, rather than respond consistently with `404 Not Found` we can handle gracefully.

This problem is hard to solve generically as we know how the **host** would handles invalid, outdated, or broken URLs. The best case scenario (**simplest, most maintainable**) would be having a single specification to program against.

### Goals

1. Who is the provider for the Song (URL)?
2. Get basic information (meta data) about a Song.
3. Validate that the URL (for a given provider) is still valid.

**Related, but out-of-scope**
-  Query other providers for an alternate URL to the same Song (based on the basic meta data)? The user must confirm whether it is the same song. 
	- *Being able to add Deezer songs and pick up a corresponding YouTube link is an end-goal for this system. Spotify support will be a nice to have.*

## oEmbed

*"oEmbed is a format for allowing an embedded representation of a URL on third party sites. The simple API allows a website to display embedded content (such as photos or videos) when a user posts a link to that resource, without having to parse the resource directly."* - [oEmbed](https://oembed.com/)

We can use **oEmbed** to query for a provider, if any, of the URL. This would become the single specification to program against.
- "*Many services consume oEmbed information to display link information, including WordPress and Slack,*" - [see 7.2 Consumers](https://oembed.com/#section7.2).

It would still be difficult to do this generically (*dynamically, for all possible hosts; [see 7.1 Providers](https://oembed.com/#section7.1)*), but our problem becomes easier to solve if we get more specific with the **hosts** (***providers***) we support.
### Providers

YouTube, Deezer and Spotify are three providers that I will be using for this project. The majority of the **ambient noises** I use for TTRPG games are provided by various YouTubers, whereas the **background music** I use is typically found listening through Artists' albums on Deezer. Spotify has mixes of both.
#### Documentation

- [oEmbed](https://oembed.com/)
- [Deezer](https://developers.deezer.com/api/oembed)
- [Spotify](https://developer.spotify.com/documentation/embeds/reference/oembed)

##### YouTube

I cannot find the official documentation for this endpoint, but it does exist and is supported.

**Endpoint:**

```
https://www.youtube.com/oembed
```

**Example Usage:**

```
https://www.youtube.com/oembed?url=https://www.youtube.com/watch?v=rdwz7QiG0lk
```

