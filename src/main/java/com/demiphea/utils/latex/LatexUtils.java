package com.demiphea.utils.latex;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * LatexUtils
 *
 * @author demiphea
 * @since 17.0.9
 */
@Component
@Slf4j
public class LatexUtils {
    private static Process instance;

    @PostConstruct
    public static void run() throws IOException {
        ClassPathResource js = new ClassPathResource("latex/index.js");
        instance = new ProcessBuilder("node.exe", js.getFile().getAbsolutePath()).start();
        log.info("Latex Render Server Running...");
    }

    @PreDestroy
    public static void destroy() {
        log.info("Latex Render Server Stopping...");
        instance.destroy();
    }
}
