package ma.jaouad.bank.chatbotservice;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ChatBotServiceApplication {

    public static void main(String[] args) {
        // Load .env file from the project root
        try {
            Dotenv dotenv = Dotenv.configure()
                    .directory("../")  // Look in parent directory (project root)
                    .ignoreIfMissing()
                    .load();
            
            // Set environment variables for Spring to use
            dotenv.entries().forEach(entry -> 
                System.setProperty(entry.getKey(), entry.getValue())
            );
        } catch (Exception e) {
            System.out.println("Warning: Could not load .env file. Using system environment variables.");
        }
        
        SpringApplication.run(ChatBotServiceApplication.class, args);
    }

}
