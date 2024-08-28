package com.example.soundofmeme.repository;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.soundofmeme.entity.Song;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {

	@Query("SELECT s FROM Song s WHERE s.userId = :userId")
	List<Song> findByUserId(@Param("userId") Long userId);


   // @Query("SELECT s FROM Song s WHERE s.id IN (SELECT DISTINCT s2.id FROM Song s2)")
    //List<Song> findAllDistinctById(Pageable pageable);

}
