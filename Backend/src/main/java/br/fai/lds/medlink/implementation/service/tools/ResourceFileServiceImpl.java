package br.fai.lds.medlink.implementation.service.tools;

import br.fai.lds.medlink.port.service.tools.ResourceFileService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Service
public class ResourceFileServiceImpl implements ResourceFileService {

    @Override
    public String read(String basePath) throws IOException {
        
        ClassLoader classLoader = getClass().getClassLoader();
        
        // Tenta obter o arquivo como resource
        try (InputStream inputStream = classLoader.getResourceAsStream(basePath)) {
            if (inputStream != null) {
                return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            }
        }
        
        throw new IOException("Resource not found: " + basePath);
    }
}
