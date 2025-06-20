create table song(
-- Primary
id integer NOT NULL PRIMARY KEY,
url varchar(2048) NOT NULL UNIQUE,  -- no standard for URL max length. Best approx. ~2,000 chars.
-- Metadata
last_lookup timestamp(6),   -- Timestamp without timezone
-- Song Information
title varchar(256),
artist varchar(256),
thumbnail_url varchar(2048)
);

create table playlist(
-- Primary
id integer NOT NULL PRIMARY KEY,
name varchar(255) NOT NULL  -- potential target for indexing later on.
);

create table playlist_song(
-- Primary
id integer NOT NULL PRIMARY KEY,
playlist_id integer NOT NULL,
FOREIGN KEY (playlist_id) REFERENCES playlist(id),
song_id integer NOT NULL,
FOREIGN KEY (song_id) REFERENCES song(id)
--UNIQUE(playlist_id, song_id) -- for now, the same Song can occur multiple times in a Playlist.
-- Playlist Song Information
--position smallint -- but we have not yet gotten to this stage as yet.
);