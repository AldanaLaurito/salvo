package com.accenture.salvo;


import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import java.time.Instant;
import java.util.*;

@SpringBootApplication
public class SalvoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
	}
	@Bean
	public CommandLineRunner initData(PlayerRepository playerRepository,
									  GameRepository gameRepository,
									  GamePlayerRepository gamePlayerRepository) {
		return (args) -> {
			Player player1 = new Player("hola@gmail.com");
			Player player2 = new Player("hi@gmail.com");
			Player player3 = new Player("sun@gmail.com");
			Player player4 = new Player("mar@gmail.com");

			playerRepository.save(player1);
			playerRepository.save(player2);
			playerRepository.save(player3);
			playerRepository.save(player4);

			Date date = Calendar.getInstance().getTime();
			Date date2 = AddHour(date);
			Date date3 = AddHour(date2);

			Game game1 = new Game(date);
			Game game2 = new Game(date2);
			Game game3 = new Game(date3);

			gameRepository.save(game1);
			gameRepository.save(game2);
			gameRepository.save(game3);

			gamePlayerRepository.save(new GamePlayer(date,game1,player1));
			gamePlayerRepository.save(new GamePlayer(date2,game1,player2));
			gamePlayerRepository.save(new GamePlayer(date3,game3,player1));
		};
	}

	public Date AddHour(Date date)
	{
		Instant inst = date.toInstant();
		inst.plusSeconds(3600);
		return Date.from(inst);

	}
}
