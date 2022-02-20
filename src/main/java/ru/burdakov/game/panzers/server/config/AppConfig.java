package ru.burdakov.game.panzers.server.config;

import com.badlogic.gdx.backends.headless.HeadlessApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.burdakov.game.panzers.server.GameLoop;

@Configuration
public class AppConfig {

    @Bean
    public HeadlessApplication getApplication(GameLoop gameLoop){
        return new HeadlessApplication(gameLoop);
    }

}
