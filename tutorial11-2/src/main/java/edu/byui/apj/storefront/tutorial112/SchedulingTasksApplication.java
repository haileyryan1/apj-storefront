package edu.byui.apj.storefront.tutorial112;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootApplication
@EnableScheduling
public class SchedulingTasksApplication implements CommandLineRunner{
    public static void main(String[] args) {
        SpringApplication.run(SchedulingTasksApplication.class, args);
    }

    @Override
    public void run(String... args) {
        System.out.println("Application started...");
    }

    @Scheduled(fixedRate = 5000)
    public void reportCurrentTime() {
        System.out.println("The time is now " + LocalTime.now());
    }

    // New scheduled task using cron-based scheduling
    @Scheduled(cron = "0 30 10 * * ?") // Runs at 10:30 AM every day
    public void processNamesInParallel() {
        List<String> names = Arrays.asList(
                "Alice", "Bob", "Charlie", "David", "Emma",
                "Frank", "Grace", "Henry", "Ivy", "Jack",
                "Karen", "Liam", "Mia", "Noah", "Olivia",
                "Paul", "Quinn", "Ryan", "Sophia", "Thomas"
        );

        int mid = names.size() / 2;
        List<String> batch1 = names.subList(0, mid);
        List<String> batch2 = names.subList(mid, names.size());

        ExecutorService executor = Executors.newFixedThreadPool(2);

        Runnable task1 = () -> batch1.forEach(name ->
                System.out.println(Thread.currentThread().getName() + " - " + name + " at " + LocalTime.now()));
        Runnable task2 = () -> batch2.forEach(name ->
                System.out.println(Thread.currentThread().getName() + " - " + name + " at " + LocalTime.now()));

        executor.execute(task1);
        executor.execute(task2);

        executor.shutdown();
        while (!executor.isTerminated()) {
            // Wait for both tasks to complete
        }
        System.out.println("All done here!");
    }
}
