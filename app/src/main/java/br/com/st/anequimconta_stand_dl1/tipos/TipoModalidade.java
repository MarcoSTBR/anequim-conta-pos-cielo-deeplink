package br.com.st.anequimconta_stand_dl1.tipos;

public enum TipoModalidade {
    pgTroco("pgTroco"),
    pgDinheiro("pgDinheiro"),
    pgCartao("pgCartao"),
    pgDebito("pgDebito"),
    pgCredito("pgCredito"),
    pgCartaoLio("pgCartaoLio"),
    pgDesconto("pgDesconto"),
    pgGorjeta("pgGorjeta"),
    pgPIX("pgPIX"),
    pgQRCode("pgQRCode");

    public String valor;

    TipoModalidade(String v) {
        valor = v;
    }
}
