package br.gov.jfrj.siga.sr.model;

public enum SrTipoMotivoPendencia {

    AGUARDANDO_PRIORIZACAO(1, "Aguardando prioriza��o", false), AGUARDANDO_RECURSO_EXTERNO(2, "Aguardando recurso externo", false), USUARIO_INDISPONIVEL(3, "Usu�rio indispon�vel", false), AGUARDANDO_RESPOSTA(
            4, "Aguardando resposta do usu�rio", false);

    private int nivelTipoMotivoPendencia;

    private String descrTipoMotivoPendencia;

    private boolean suspendeTempoAtendimento;

    private SrTipoMotivoPendencia(int nivelUrgencia, String descrUrgencia) {
        this(nivelUrgencia, descrUrgencia, false);
    }

    SrTipoMotivoPendencia(int nivel, String descricao, boolean suspende) {
        this.setNivelTipoMotivoPendencia(nivel);
        this.setDescrTipoMotivoPendencia(descricao);
        this.setSuspendeTempoAtendimento(suspende);
    }

    public int getNivelTipoMotivoPendencia() {
        return nivelTipoMotivoPendencia;
    }

    public void setNivelTipoMotivoPendencia(int nivelTipoMotivoPendencia) {
        this.nivelTipoMotivoPendencia = nivelTipoMotivoPendencia;
    }

    public String getDescrTipoMotivoPendencia() {
        return descrTipoMotivoPendencia;
    }

    public void setDescrTipoMotivoPendencia(String descrTipoMotivoPendencia) {
        this.descrTipoMotivoPendencia = descrTipoMotivoPendencia;
    }

    public boolean isSuspendeTempoAtendimento() {
        return suspendeTempoAtendimento;
    }

    public void setSuspendeTempoAtendimento(boolean suspendeTempoAtendimento) {
        this.suspendeTempoAtendimento = suspendeTempoAtendimento;
    }

}
