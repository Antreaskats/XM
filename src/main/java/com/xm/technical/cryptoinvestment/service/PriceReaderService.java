package com.xm.technical.cryptoinvestment.service;

import com.xm.technical.cryptoinvestment.model.Crypto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PriceReaderService {

    @Value("${spring.file.path}")
    private String folderPath;
    private static final String CACHE_KEY = "cryptoData";
    private static final String TIMESTAMP_CACHE_KEY = "cryptoFileTimestamps";
    private final RedisTemplate<String, Object> redisTemplate;
    private final HashOperations<String, String, List<Crypto>> hashOps;
    private final HashOperations<String, String, String> timestampOps;

    public PriceReaderService(@Qualifier("myRedisTemplate") RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.hashOps = redisTemplate.opsForHash();
        this.timestampOps = redisTemplate.opsForHash();
    }

    public Map<String, List<Crypto>> getCryptoData() {
        checkForUpdates();
        removeDeletedFiles();
        return Optional.ofNullable(hashOps.entries(CACHE_KEY)).orElse(Collections.emptyMap());
    }

    private void checkForUpdates() {
        try (var files = Files.list(Paths.get(folderPath))) {
            files.filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".csv"))
                    .forEach(this::updateFileIfChanged);
        } catch (IOException e) {
            throw new RuntimeException("Failed to check CSV files", e);
        }
    }

    private void updateFileIfChanged(Path filePath) {
        try {
            String fileName = filePath.getFileName().toString();
            long lastModified = Files.getLastModifiedTime(filePath).toMillis();

            String lastModifiedInRedis = timestampOps.get(TIMESTAMP_CACHE_KEY, fileName);

            if (lastModifiedInRedis == null || lastModified > Long.parseLong(lastModifiedInRedis)) {
                List<Crypto> cryptos = readCsvFile(filePath);
                if (!cryptos.isEmpty()) {
                    hashOps.put(CACHE_KEY, fileName, cryptos);
                    timestampOps.put(TIMESTAMP_CACHE_KEY, fileName, String.valueOf(lastModified));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read CSV file: " + filePath, e);
        }
    }

    private void removeDeletedFiles() {
        Set<String> storedFiles = timestampOps.keys(TIMESTAMP_CACHE_KEY);

        if(storedFiles.isEmpty()){
            return;
        }
        Set<String> currentFiles;
        try (var files = Files.list(Paths.get(folderPath))) {
            currentFiles = files.map(path -> path.getFileName().toString())
                    .collect(Collectors.toSet());
        } catch (IOException e) {
            throw new RuntimeException("Failed to list files in directory", e);
        }

        for (String storedFile : storedFiles) {
            if (!currentFiles.contains(storedFile)) {
                hashOps.delete(CACHE_KEY, storedFile);
                timestampOps.delete(TIMESTAMP_CACHE_KEY, storedFile);
            }
        }
    }

    private List<Crypto> readCsvFile(Path filePath) {
        try (var lines = Files.lines(filePath)) {
            return lines.skip(1)
                    .map(line -> line.split(","))
                    .map(arr -> new Crypto(arr[0], arr[1], new BigDecimal(arr[2])))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Failed to read CSV file: " + filePath, e);
        }
    }
}

