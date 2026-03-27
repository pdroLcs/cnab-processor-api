package br.com.pedro.cnab_processor_api.service;

import br.com.pedro.cnab_processor_api.model.TipoTransacao;
import br.com.pedro.cnab_processor_api.model.Transacao;
import br.com.pedro.cnab_processor_api.repository.TransacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static java.util.Objects.nonNull;

@Service
public class CnabService {

    @Autowired
    private TransacaoRepository transacaoRepository;

    public void processarArquivo(MultipartFile file) {
        try (var br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String linha = br.readLine();
            while (nonNull(linha)) {
                Transacao transacao = parseLinha(linha);
                transacaoRepository.save(transacao);
            }
        } catch (IOException err) {
            err.printStackTrace();
        }
    }

    private Transacao parseLinha(String linha) {
        var transacao = new Transacao();
        int tipoCodigo = Integer.parseInt(linha.substring(0, 1));
        var tipoTransacao = TipoTransacao.fromCodigo(tipoCodigo);

        transacao.setTipo(tipoTransacao.getDescricao());
        transacao.setNatureza(tipoTransacao.getNatureza());
        transacao.setSinal(tipoTransacao.getSinal());
        transacao.setData(LocalDate.parse(linha.substring(1, 9), DateTimeFormatter.BASIC_ISO_DATE));
        transacao.setValor(new BigDecimal(linha.substring(9, 19)).divide(new BigDecimal(100)));
        transacao.setCpf(linha.substring(19, 30));
        transacao.setCartao(linha.substring(30, 42));
        transacao.setHora(LocalTime.parse(linha.substring(42, 48), DateTimeFormatter.ofPattern("HHmmss")));
        transacao.setDonoLoja(linha.substring(48, 62).trim());
        transacao.setNomeLoja(linha.substring(62, 81));

        return transacao;
    }

    public List<Transacao> listarTransacoes() {
        return transacaoRepository.findAll();
    }

}
