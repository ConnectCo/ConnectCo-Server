package com.connectCo.config;

import com.connectCo.global.exception.CustomApiException;
import com.connectCo.global.exception.ErrorCode;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Configuration
@Order(1)
public class FireBaseConfig {

    @Value("${fcm.file_path}")
    private String serviceAccountFilePath;
    
    //firebase 초기화
    @PostConstruct
    public void init(){
        try (InputStream serviceAccount = getClass().getClassLoader().getResourceAsStream(serviceAccountFilePath)){

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                log.info("FirebaseApp initialization complete");
            }

        }catch (IOException e){
            log.error("Error initializing Firebase: {}", e.getMessage(), e);
            throw new CustomApiException(ErrorCode.FIREBASE_INIT_FAILED);
        }
    }
}