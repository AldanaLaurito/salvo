package com.accenture.salvo;

import com.accenture.salvo.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ScoreRepository   extends JpaRepository<Score, Long> {
}
