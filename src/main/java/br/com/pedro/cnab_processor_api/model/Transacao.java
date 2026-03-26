package br.com.pedro.cnab_processor_api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
public class Transacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String tipo;
    private String natureza;
    private String sinal;
    private LocalDate data;
    private BigDecimal valor;
    private String cpf;
    private String cartao;
    private LocalTime hora;
    private String donoLoja;
    private String nomeLoja;

}
