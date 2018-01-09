package gptraining.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class Populacao 
{
	public @Getter @Setter List<ArvoreExpressao> individuos;
	public @Getter @Setter int  numeroGeracao;
	
	public Populacao()
	{
		individuos = new ArrayList<ArvoreExpressao>();
		numeroGeracao = 0;
	}
	
	public Populacao(int tamanhoPopulacao)
	{
		individuos = new ArrayList<ArvoreExpressao>(tamanhoPopulacao);
		numeroGeracao = 0;
	}
	
	public Populacao(List<ArvoreExpressao> listaIndividuos)
	{
		individuos = listaIndividuos;
	}
	
	public void adicionaIndividuo(ArvoreExpressao arvore)
	{
		individuos.add(arvore);
	}

	public void embaralhaPopulacao() {
		Collections.shuffle(individuos);
	}
	
}
