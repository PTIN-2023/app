package com.example.appptin.medico.fragments.recetas.crear;

public class MedsRecipeDetail {

    private String codi;
    private Integer quant;

    public MedsRecipeDetail(String codi, Integer quant) {
        this.codi = codi;
        this.quant = quant;
    }

    public String getCodi() {
        return codi;
    }

    public Integer getQuant() {
        return quant;
    }

    public void setCodiQuant(String codi, Integer quant) {
        this.codi = codi;
        this.quant = quant;
    }
}
