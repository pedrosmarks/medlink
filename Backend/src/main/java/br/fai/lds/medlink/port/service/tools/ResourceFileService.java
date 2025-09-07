package br.fai.lds.medlink.port.service.tools;

import java.io.IOException;

public interface ResourceFileService {
    String read(String basePath) throws IOException;
}
