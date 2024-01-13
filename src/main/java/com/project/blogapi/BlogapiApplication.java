package com.project.blogapi;

import com.project.blogapi.articles.ArticleEntity;
import com.project.blogapi.articles.dto.ArticleResponseDTO;
import com.project.blogapi.security.jwt.JWTTokenService;
import com.project.blogapi.security.TokenService;
import com.project.blogapi.security.tokens.UserTokenRepository;
import com.project.blogapi.security.tokens.UserTokenService;
import com.project.blogapi.users.UsersRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_SINGLETON;

@SpringBootApplication
public class BlogapiApplication {

	private final String TOKEN_SERVICE_TYPE = "SST"; //"JWT" OR:"SST"

	public static void main(String[] args) {

		SpringApplication.run(BlogapiApplication.class, args);
	}
	@Bean //make custom autowiring -> Dependency Injection
	@Scope(BeanDefinition.SCOPE_SINGLETON)
	public ModelMapper modelMapper(){
		ModelMapper modelMapper = new ModelMapper();

		// Custom mapping configuration for ArticleEntity to ArticleResponseDTO
		modelMapper.addMappings(new PropertyMap<ArticleEntity, ArticleResponseDTO>() {
			@Override
            protected void configure() {
				// Skip mapping the entire UserEntity and map only the username to the author field
				map().setAuthor(source.getAuthor().getUsername());
			}
		});

		return modelMapper;
	}
	@Bean
	@Scope(BeanDefinition.SCOPE_SINGLETON)
	public PasswordEncoder passwordEncoder(){
		var bcryptEncoder = new BCryptPasswordEncoder();
		var argon2Encoder = Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();

		return new PasswordEncoder() { //backward compatability for password encoding
			@Override
			public String encode(CharSequence rawPassword) {
				return argon2Encoder.encode(rawPassword);
			}

			@Override
			public boolean matches(CharSequence rawPassword, String encodedPassword) {
				var passMatch = argon2Encoder.matches(rawPassword, encodedPassword);
				if(!passMatch){
					passMatch = bcryptEncoder.matches(rawPassword,encodedPassword);
				}
				return passMatch;
			}
		};
	}
	@Bean
	@Scope(SCOPE_SINGLETON)
	public TokenService tokenService(
			@Autowired UsersRepository usersRepository,
			@Autowired UserTokenRepository userTokenRepository
	){
		switch (TOKEN_SERVICE_TYPE){
			case "SST":
				return new UserTokenService(usersRepository, userTokenRepository);
			case "JWT":
				return new JWTTokenService();
			default:
				throw new IllegalStateException("Unexpected value "+TOKEN_SERVICE_TYPE);
		}
	}

}
