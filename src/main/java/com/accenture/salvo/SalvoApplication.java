package com.accenture.salvo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.Instant;
import java.util.*;

@SpringBootApplication
public class SalvoApplication {
	public Date AddHour(Date date)
	{
		Instant inst = date.toInstant();
		//inst.plusSeconds(3600);
		return Date.from(inst.plusSeconds(3600));

	}
	public Date AddHalfHour(Date date)
	{
		Instant inst = date.toInstant();
		//inst.plusSeconds(1800);
		return Date.from(inst.plusSeconds(1800));

	}
	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
	}
	@Bean
	public CommandLineRunner initData(PlayerRepository playerRepository,
									  GameRepository gameRepository,
									  GamePlayerRepository gamePlayerRepository,
									  ShipRepository shipRepository,
									  SalvoRepository salvoRepository,
									  ScoreRepository scoreRepository) {
		return (args) -> {
			Player player1 = new Player("j.bauer@ctu.gov","24");
			Player player2 = new Player("c.obrian@ctu.gov","42");
			Player player3 = new Player("kim_bauer@gmail.com","kb");
			Player player4 = new Player("t.almeida@ctu.gov","mole");

			playerRepository.save(player1);
			playerRepository.save(player2);
			playerRepository.save(player3);
			playerRepository.save(player4);

			Date date = new Date();
			Date date2 = AddHour(date);
			Date date3 = AddHour(date2);
			Date date4 = AddHour(date3);
			Date date5 = AddHour(date4);
			Date date6 = AddHour(date5);
			Date date7 = AddHour(date6);
			Date date8 = AddHour(date7);

			Game game1 = new Game(date);
			Game game2 = new Game(date2);
			Game game3 = new Game(date3);
			Game game4 = new Game(date4);
			Game game5 = new Game(date5);
			Game game6 = new Game(date6);
			Game game7 = new Game(date7);
			Game game8 = new Game(date8);

			gameRepository.save(game1);
			gameRepository.save(game2);
			gameRepository.save(game3);
			gameRepository.save(game4);
			gameRepository.save(game5);
			gameRepository.save(game6);
			gameRepository.save(game7);
			gameRepository.save(game8);

			GamePlayer gamePlayer1 = new GamePlayer(date,game1,player1);
			GamePlayer gamePlayer2 = new GamePlayer(date,game1,player2);
			GamePlayer gamePlayer3 = new GamePlayer(date2,game2,player1);
			GamePlayer gamePlayer4 = new GamePlayer(date2,game2,player2);
			GamePlayer gamePlayer5 = new GamePlayer(date3,game3,player2);
			GamePlayer gamePlayer6 = new GamePlayer(date3,game3,player4);
			GamePlayer gamePlayer7 = new GamePlayer(date4,game4,player2);
			GamePlayer gamePlayer8 = new GamePlayer(date4,game4,player1);
			GamePlayer gamePlayer9 = new GamePlayer(date5,game5,player4);
			GamePlayer gamePlayer10 = new GamePlayer(date5,game5,player1);
			GamePlayer gamePlayer11= new GamePlayer(date6,game6,player3);
			GamePlayer gamePlayer12 = new GamePlayer(date7,game7,player4);
			GamePlayer gamePlayer13 = new GamePlayer(date8,game8,player3);
			GamePlayer gamePlayer14= new GamePlayer(date8,game8,player4);


			gamePlayerRepository.save(gamePlayer1);
			gamePlayerRepository.save(gamePlayer2);
			gamePlayerRepository.save(gamePlayer3);
			gamePlayerRepository.save(gamePlayer4);
			gamePlayerRepository.save(gamePlayer5);
			gamePlayerRepository.save(gamePlayer6);
			gamePlayerRepository.save(gamePlayer7);
			gamePlayerRepository.save(gamePlayer8);
			gamePlayerRepository.save(gamePlayer9);
			gamePlayerRepository.save(gamePlayer10);
			gamePlayerRepository.save(gamePlayer11);
			gamePlayerRepository.save(gamePlayer12);
			gamePlayerRepository.save(gamePlayer13);
			gamePlayerRepository.save(gamePlayer14);


			List<String> listLocationsSh1 = new ArrayList<>();
			listLocationsSh1.add("H2");
			listLocationsSh1.add("H3");
			listLocationsSh1.add("H4");

			List<String> listLocationsSh2 = new ArrayList<>();
			listLocationsSh2.add("E1");
			listLocationsSh2.add("F1");
			listLocationsSh2.add("G1");

			List<String> listLocationsSh3 = new ArrayList<>();
			listLocationsSh3.add("B4");
			listLocationsSh3.add("B5");

			List<String> listLocationsSh4 = new ArrayList<>();
			listLocationsSh4.add("B5");
			listLocationsSh4.add("C5");
			listLocationsSh4.add("D5");
			List<String> listLocationsSh5 = new ArrayList<>();
			listLocationsSh5.add("F1");
			listLocationsSh5.add("F2");

			List<String> listLocationsSh6 = new ArrayList<>();
			listLocationsSh6.add("B5");
			listLocationsSh6.add("C5");
			listLocationsSh6.add("D5");
			List<String> listLocationsSh7 = new ArrayList<>();
			listLocationsSh7.add("C6");
			listLocationsSh7.add("C7");

			List<String> listLocationsSh8 = new ArrayList<>();
			listLocationsSh8.add("A2");
			listLocationsSh8.add("A3");
			listLocationsSh8.add("A4");
			List<String> listLocationsSh9 = new ArrayList<>();
			listLocationsSh9.add("G6");
			listLocationsSh9.add("H6");

			List<String> listLocationsSh10 = new ArrayList<>();
			listLocationsSh10.add("B5");
			listLocationsSh10.add("C5");
			listLocationsSh10.add("D5");
			List<String> listLocationsSh11 = new ArrayList<>();
			listLocationsSh11.add("C6");
			listLocationsSh11.add("C7");

			List<String> listLocationsSh12 = new ArrayList<>();
			listLocationsSh12.add("A2");
			listLocationsSh12.add("A3");
			listLocationsSh12.add("A4");
			List<String> listLocationsSh13 = new ArrayList<>();
			listLocationsSh13.add("G6");
			listLocationsSh13.add("H6");
			List<String> listLocationsSh14 = new ArrayList<>();
			listLocationsSh14.add("B5");
			listLocationsSh14.add("C5");
			listLocationsSh14.add("D5");
			List<String> listLocationsSh15 = new ArrayList<>();
			listLocationsSh15.add("C6");
			listLocationsSh15.add("C7");

			List<String> listLocationsSh16 = new ArrayList<>();
			listLocationsSh16.add("A2");
			listLocationsSh16.add("A3");
			listLocationsSh16.add("A4");
			List<String> listLocationsSh17 = new ArrayList<>();
			listLocationsSh17.add("G6");
			listLocationsSh17.add("H6");

			List<String> listLocationsSh18 = new ArrayList<>();
			listLocationsSh17.add("B5");
			listLocationsSh17.add("C5");
			listLocationsSh17.add("D5");
			List<String> listLocationsSh19 = new ArrayList<>();
			listLocationsSh19.add("C6");
			listLocationsSh19.add("C7");

			List<String> listLocationsSh20 = new ArrayList<>();
			listLocationsSh20.add("A2");
			listLocationsSh20.add("A3");
			listLocationsSh20.add("A4");
			List<String> listLocationsSh21 = new ArrayList<>();
			listLocationsSh21.add("G6");
			listLocationsSh21.add("H6");

			List<String> listLocationsSh22 = new ArrayList<>();
			listLocationsSh22.add("B5");
			listLocationsSh22.add("C5");
			listLocationsSh22.add("D5");
			List<String> listLocationsSh23 = new ArrayList<>();
			listLocationsSh23.add("C6");
			listLocationsSh23.add("C7");

			List<String> listLocationsSh24 = new ArrayList<>();
			listLocationsSh24.add("B5");
			listLocationsSh24.add("C5");
			listLocationsSh24.add("D5");
			List<String> listLocationsSh25 = new ArrayList<>();
			listLocationsSh25.add("C6");
			listLocationsSh25.add("C7");

			List<String> listLocationsSh26 = new ArrayList<>();
			listLocationsSh26.add("A2");
			listLocationsSh26.add("A3");
			listLocationsSh26.add("A4");
			List<String> listLocationsSh27 = new ArrayList<>();
			listLocationsSh27.add("G6");
			listLocationsSh27.add("H6");



			Ship ship1 = new Ship("destroyer",gamePlayer1);
			Ship ship2 = new Ship("patrol boat",gamePlayer1);
			Ship ship3 = new Ship("battleship",gamePlayer1);
			Ship ship4 = new Ship("carrier",gamePlayer2);
			Ship ship5 = new Ship("submarine",gamePlayer2);
			Ship ship6 = new Ship("battleship",gamePlayer3);
			Ship ship7 = new Ship("battleship",gamePlayer3);
			Ship ship8 = new Ship("battleship",gamePlayer4);
			Ship ship9 = new Ship("battleship",gamePlayer4);
			Ship ship10 = new Ship("battleship",gamePlayer5);
			Ship ship11 = new Ship("battleship",gamePlayer5);
			Ship ship12 = new Ship("battleship",gamePlayer6);
			Ship ship13 = new Ship("battleship",gamePlayer6);
			Ship ship14 = new Ship("battleship",gamePlayer7);
			Ship ship15 = new Ship("battleship",gamePlayer7);
			Ship ship16 = new Ship("battleship",gamePlayer8);
			Ship ship17 = new Ship("battleship",gamePlayer8);
			Ship ship18 = new Ship("battleship",gamePlayer9);
			Ship ship19 = new Ship("battleship",gamePlayer9);
			Ship ship20 = new Ship("battleship",gamePlayer10);
			Ship ship21 = new Ship("battleship",gamePlayer10);
			Ship ship22 = new Ship("battleship",gamePlayer11);
			Ship ship23 = new Ship("battleship",gamePlayer11);
			Ship ship24 = new Ship("battleship",gamePlayer13);
			Ship ship25 = new Ship("battleship",gamePlayer13);
			Ship ship26 = new Ship("battleship",gamePlayer14);
			Ship ship27 = new Ship("battleship",gamePlayer14);


			ship1.setListLocations(listLocationsSh1);
			ship2.setListLocations(listLocationsSh2);
			ship3.setListLocations(listLocationsSh3);
			ship4.setListLocations(listLocationsSh4);
			ship5.setListLocations(listLocationsSh5);
			ship6.setListLocations(listLocationsSh7);
			ship8.setListLocations(listLocationsSh8);
			ship9.setListLocations(listLocationsSh9);
			ship10.setListLocations(listLocationsSh10);
			ship11.setListLocations(listLocationsSh11);
			ship12.setListLocations(listLocationsSh12);
			ship13.setListLocations(listLocationsSh13);
			ship14.setListLocations(listLocationsSh14);
			ship15.setListLocations(listLocationsSh15);
			ship16.setListLocations(listLocationsSh16);
			ship17.setListLocations(listLocationsSh17);
			ship18.setListLocations(listLocationsSh18);
			ship19.setListLocations(listLocationsSh19);
			ship20.setListLocations(listLocationsSh20);
			ship21.setListLocations(listLocationsSh21);
			ship22.setListLocations(listLocationsSh23);
			ship24.setListLocations(listLocationsSh24);
			ship25.setListLocations(listLocationsSh25);
			ship26.setListLocations(listLocationsSh26);
			ship27.setListLocations(listLocationsSh27);

			shipRepository.save(ship1);
			shipRepository.save(ship2);
			shipRepository.save(ship3);
			shipRepository.save(ship4);
			shipRepository.save(ship5);
			shipRepository.save(ship6);
			shipRepository.save(ship7);
			shipRepository.save(ship8);
			shipRepository.save(ship9);
			shipRepository.save(ship10);
			shipRepository.save(ship11);
			shipRepository.save(ship12);
			shipRepository.save(ship13);
			shipRepository.save(ship14);
			shipRepository.save(ship15);
			shipRepository.save(ship16);
			shipRepository.save(ship17);
			shipRepository.save(ship18);
			shipRepository.save(ship19);
			shipRepository.save(ship20);
			shipRepository.save(ship21);
			shipRepository.save(ship22);
			shipRepository.save(ship23);
			shipRepository.save(ship24);
			shipRepository.save(ship25);
			shipRepository.save(ship26);
			shipRepository.save(ship27);

			Salvo salvo1 = new Salvo(1,gamePlayer1);
			Salvo salvo2 = new Salvo(2,gamePlayer1);
			Salvo salvo3 = new Salvo(1,gamePlayer2);
			Salvo salvo4 = new Salvo(2,gamePlayer2);

			List<String> listLocationSal1 = new ArrayList<>();
            listLocationSal1.add("B5");
            listLocationSal1.add("C5");
            listLocationSal1.add("F1");
			List<String> listLocationSal2 = new ArrayList<>();
            listLocationSal2.add("F2");
            listLocationSal2.add("D5");
			List<String> listLocationSal3 = new ArrayList<>();
            listLocationSal3.add("B4");
            listLocationSal3.add("B5");
            listLocationSal3.add("B6");
			List<String> listLocationSal4 = new ArrayList<>();
            listLocationSal4 .add("E1");
			listLocationSal4 .add("H3");
			listLocationSal4 .add("A2");



			salvo1.setListLocations(listLocationSal1);
			salvo2.setListLocations(listLocationSal2);
			salvo3.setListLocations(listLocationSal3);

			salvo4.setListLocations(listLocationSal4);

			salvoRepository.save(salvo1);
			salvoRepository.save(salvo2);
			salvoRepository.save(salvo3);
			salvoRepository.save(salvo4);


			Date date1F = AddHalfHour(date);
			Date date2F = AddHalfHour(date2);
			Date date3F = AddHalfHour(date3);
			Date date4F = AddHalfHour(date4);

			Score score1 = new Score(1,date1F,game1,player1);
			Score score2 = new Score(0.5,date2F,game2,player1);
			Score score3 = new Score(0.5,date2F,game2,player2);
			Score score4 = new Score(1,date3F,game3,player2);
			Score score5 = new Score(0.5,date4F,game4,player2);
			Score score6= new Score(0.5,date4F,game4,player1);

			Score score7 = new Score(0,date1F,game1,player2);
			Score score8 = new Score(0,date3F,game3,player4);

			scoreRepository.save(score1);
			scoreRepository.save(score2);
			scoreRepository.save(score3);
			scoreRepository.save(score4);
			scoreRepository.save(score5);
			scoreRepository.save(score6);
			scoreRepository.save(score7);
			scoreRepository.save(score8);



		};
	}

	@Configuration
	class WebSecurityConfiguration extends GlobalAuthenticationConfigurerAdapter {
		@Autowired
		PlayerRepository playerRepository;

		@Override
		public void init(AuthenticationManagerBuilder auth) throws Exception {
			auth.userDetailsService(inputName-> {
				Player player = playerRepository.findByUserName(inputName);
				if (player != null) {
					return new User(player.getUserName(), player.getPassword(),
							AuthorityUtils.createAuthorityList("USER"));
				} else {
					throw new UsernameNotFoundException("Unknown user: " + inputName);
				}
			});
		}

	}

	@EnableWebSecurity
	@Configuration
	class WebSecurityConfig extends WebSecurityConfigurerAdapter {

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			/*http.authorizeRequests().anyRequest().fullyAuthenticated().
					and().httpBasic();*/

			/*http.authorizeRequests()
					.antMatchers("/admin/**").hasAuthority("ADMIN")
					.antMatchers("/**").hasAuthority("USER")
					.and()
					.formLogin();*/

			/*http.authorizeRequests()
					.antMatchers("/api/players").hasAuthority("USER")
					.antMatchers("/api/game_view/**").hasAuthority("USER")
					.antMatchers("/web/**").permitAll()
					.antMatchers("/webNew/**").permitAll()
					.antMatchers("/api/**").permitAll();
					//.anyRequest().fullyAuthenticated();
					*/
			http.authorizeRequests()
					.antMatchers("/webNew/**").permitAll()
					.antMatchers("/api/game_view/**").hasAuthority("USER")
					.antMatchers("/api/**").permitAll()
					.antMatchers("/web/**").permitAll()
					.antMatchers("/**").permitAll()
					.anyRequest().hasAuthority("USER");
			http.headers().frameOptions().disable();

			http.formLogin()
					.usernameParameter("name")
					.passwordParameter("pwd")
					.loginPage("/api/login");

			http.logout().logoutUrl("/api/logout");

			// turn off checking for CSRF tokens
			http.csrf().disable();

			// if user is not authenticated, just send an authentication failure response
			http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

			// if login is successful, just clear the flags asking for authentication
			http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

			// if login fails, just send an authentication failure response
			http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

			// if logout is successful, just send a success response
			http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());
		}

		private void clearAuthenticationAttributes(HttpServletRequest request) {
			HttpSession session = request.getSession(false);
			if (session != null) {
				session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
			}
		}

	}

}
