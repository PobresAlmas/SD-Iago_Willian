package br.ufc.sd.servidor_notificacoes.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Notificacao {
    private Long id;
    private Long canalId;
    private Long adminId;
    private String titulo;
    private String mensagem;
    private String dataHora;
}