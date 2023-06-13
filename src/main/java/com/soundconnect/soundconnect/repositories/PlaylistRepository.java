package com.soundconnect.soundconnect.repositories;

import com.soundconnect.soundconnect.model.Playlist;
import com.soundconnect.soundconnect.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    Playlist findById(long id);

    Playlist findByName(String name);

    Playlist findByDescription(String description);

    Playlist findByUser(User user);
}
