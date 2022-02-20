package ru.burdakov.game.panzers.server.config;

import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.burdakov.game.panzers.server.GameLoop;
import ru.burdakov.game.panzers.server.actors.Panzer;

@Configuration
public class AppConfig {

    @Bean
    public HeadlessApplication getApplication(GameLoop gameLoop){
        return new HeadlessApplication(gameLoop);
    }

    @Bean
    public Json getJson(){
        Json json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        json.addClassTag("panzer", Panzer.class);
        return json;
    }

}
