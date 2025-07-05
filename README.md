A basic CRUD application to **store** and **organise** (*URLs to*) Songs.

The planned features include:
- Creating Playlists of Songs.
- Organising Playlists into Catalogues.
- Tagging Catalogues, Playlists and Songs.
- Separating Catalogues by Profiles.

The planned extensions to the basic CRUD operations include:
- Fetching metadata (title, artist(s), run length) of Songs.
- Flagging Songs whose links no longer exist, or are invalid.
- Finding alternate links (YouTube, Deezer, Spotify) to Songs.

The purpose is to expose a RESTful API for the Gamemaster Soundboard, and potentially **automate** some of the housekeeping tasks I did to manage my song collection.

It is decoupled from the Gamemaster Soundboard because this service is useful for more than just what I need as a Gamemaster. It would be handy for organising personal playlists as well - especially when it comes to older, or more niche music which aren't all available on particular platform, but I want to build a playlist around.
