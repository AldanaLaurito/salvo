package com.accenture.salvo;

import com.accenture.salvo.GamePlayer;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface GamePlayerRepository  extends JpaRepository<GamePlayer, Long> {

}
