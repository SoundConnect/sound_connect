package com.soundconnect.soundconnect.controller;

import com.soundconnect.soundconnect.model.Album;
import com.soundconnect.soundconnect.model.Artist;
import com.soundconnect.soundconnect.model.Playlist;
import com.soundconnect.soundconnect.model.Track;
import com.soundconnect.soundconnect.repositories.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Set;

@Controller
public class PlaylistController {

    private final PlaylistRepository playlistsDao;
    private final TrackRepository tracksDao;

    private final AlbumRepository albumsDao;
    private final ArtistRepository artistsDao;
    private final UserRepository usersDao;

    public PlaylistController(PlaylistRepository playlistsDao, TrackRepository tracksDao, AlbumRepository albumsDao, ArtistRepository artistsDao, UserRepository usersDao) {
        this.playlistsDao = playlistsDao;
        this.tracksDao = tracksDao;
        this.albumsDao = albumsDao;
        this.artistsDao = artistsDao;
        this.usersDao = usersDao;
    }

    // show form for creating a playlist
    @GetMapping("/create")
    public String showCreatePlaylistForm() {
        return "createPlaylist";
    }

    // get form data and create playlist
    @PostMapping("/create")
    public String createPlaylist(@RequestBody Playlist playlist){
        Playlist savePlaylist = new Playlist(playlist.getName(), playlist.getDescription());
        savePlaylist.setTracks(playlist.getTracks());

        // save all tracks, albums, and artists to database
        for (Track track : playlist.getTracks()) {
            Track saveTrack;
            Set<Artist> artists = track.getArtists();

            // tracks
            if (tracksDao.findByName(track.getName()) == null) {
                saveTrack = new Track(track.getName(), track.getSpotifyId(), track.getDuration());
                saveTrack.setAlbum(track.getAlbum());
                saveTrack.setArtists(artists);
                tracksDao.save(saveTrack);

                for (Artist artist : artists) {
                    if (artistsDao.findByName(artist.getName()) == null) {
                        Artist saveArtist = new Artist(artist.getName());
                        artistsDao.save(saveArtist);
                    }
                }
            }

            // albums
            Album saveAlbum;
            if (albumsDao.findByName(track.getAlbum().getName()) == null) {
                saveAlbum = new Album(track.getAlbum().getName(), track.getAlbum().getAlbumArt(), track.getAlbum().getArtist());
                albumsDao.save(saveAlbum);
            }
        }

        playlistsDao.save(savePlaylist);
        return "redirect:/profile";
    }

    // show form for editing a playlist
    @GetMapping("/feed/{id}/edit")
    public String showEditPlaylistForm(@PathVariable long id, Model model){
        Playlist playlist = playlistsDao.findById(id);
        model.addAttribute("playlist", playlist);
        return "editPlaylist";
    }

    // edit playlist
    @PostMapping("/feed/{id}/edit")
    public String editPlaylist(@PathVariable long id, @RequestBody Playlist playlist){
        return "redirect:/profile";
    }



        // edit playlist
        @PostMapping("/edit/{id}")
        public String editPlaylist () {
            return "redirect:/profile";
        }

//     show feed for all shared playlists
        @GetMapping("/feed")
        public String showFeed (Model model){
//        List<Playlist> playlists = playlistsDao.findAll();
//        model.addAttribute("playlists", playlists);
            return "feed";
        }

//     delete playlist from account
        @PostMapping("/feed")
        public String deletePlaylist () {

            return "redirect:/feed";
        }
    }

