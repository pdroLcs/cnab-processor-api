package br.com.pedro.cnab_processor_api.controller;

import br.com.pedro.cnab_processor_api.model.Transacao;
import br.com.pedro.cnab_processor_api.service.CnabService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/cnab")
public class CnabController {

    @Autowired
    private CnabService cnabService;

    @GetMapping("/transacao")
    public List<Transacao> listarTransacoes() {
        return cnabService.listarTransacoes();
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        cnabService.processarArquivo(file);
        return ResponseEntity.ok("Arquivo processado com sucesso!");
    }

}
