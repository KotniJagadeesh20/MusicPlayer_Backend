package com.example.soundofmeme.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;
import com.example.soundofmeme.entity.LikedSongs;

@Repository
public interface LikedSongsRepository extends JpaRepository<LikedSongs, Long> {

	void deleteById(long id);

	@Query("SELECT l FROM LikedSongs l WHERE l.Songid = :Songid")
	LikedSongs findLikedSongBySongid(@Param("Songid") Long Songid);

}
