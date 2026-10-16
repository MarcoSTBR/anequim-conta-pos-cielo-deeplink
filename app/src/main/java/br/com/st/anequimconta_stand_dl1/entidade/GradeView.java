package br.com.st.anequimconta_stand_dl1.entidade;

import java.util.List;

public class GradeView {
    private GradeVendas grade;
    private List<Produto> produtoList;

    public GradeView(GradeVendas grade, List<Produto> produtoList) {
        this.grade = grade;
        this.produtoList = produtoList;
    }

    public GradeVendas getGrade() {
        return grade;
    }

    public void setGrade(GradeVendas grade) {
        this.grade = grade;
    }

    public List<Produto> getProdutoList() {
        return produtoList;
    }

    public void setProdutoList(List<Produto> produtoList) {
        this.produtoList = produtoList;
    }
}

