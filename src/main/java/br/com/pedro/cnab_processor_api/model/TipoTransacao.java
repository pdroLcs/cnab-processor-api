package br.com.pedro.cnab_processor_api.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TipoTransacao {

    DEBITO(1, "Débito", "Entrada", "+"),
    BOLETO(2, "Boleto", "Saída", "-"),
    FINANCIAMENTO(3, "Financiamento", "Saída", "-"),
    CREDITO(4, "Crédito", "Entrada", "+"),
    RECEBIMENTO_EMPRESITMO(5, "Recebimento Empréstimo", "Entrada", "+"),
    VENDAS(6, "Vendas", "Entrada", "+"),
    RECEBIMENTO_TED(7, "Recebimento TED", "Entrada", "+"),
    RECEBIMENTO_DOC(8, "Recebimento DOC", "Entrada", "+"),
    ALUGUEL(9, "Aluguel", "Saída", "-");

    private final int codigo;
    private final String descricao;
    private final String natureza;
    private final String sinal;

    public static TipoTransacao fromCodigo(int codigo) {
        for (TipoTransacao tipo : TipoTransacao.values()) {
            if (tipo.codigo == codigo) return tipo;
        }
        throw new IllegalArgumentException("Tipo de transação inválida: " + codigo);
    }

}
