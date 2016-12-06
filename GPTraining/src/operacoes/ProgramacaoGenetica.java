package operacoes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import gptraining.model.ArvoreExpressao;
import gptraining.model.No;

/**
 * Classe onde será gerada a população inicial, a medição e classificação por aptidão, e onde serão realizadas as operações genéticas conforme o avanço das gerações.
 */
public class ProgramacaoGenetica {

	public List<ArvoreExpressao> evoluir(List<ArvoreExpressao> populacaoInicial, int numeroGeracoes)
	{
		
		return null;
	}
	
	public void avancarGeracao(List<ArvoreExpressao> populacao)
	{
		
	}
	
	/**
	 * Ordena por aptidão as árvores presentes na lista de entrada do método.
	 */
	public List<ArvoreExpressao> avaliarAptidaoCandidatos(List<ArvoreExpressao> geracao, int[] vetX, int[] vetY)
	{
		List<ArvoreExpressao> listaOrdenada = geracao;
		ArvoreExpressao arvore = new ArvoreExpressao();
		for (int i = 0; i < geracao.size(); i++)
		{
			listaOrdenada.get(i).setAptidao(arvore.avaliarAptidaoArvore(listaOrdenada.get(i).getRaiz(), vetX, vetY));
		}
		Collections.sort(listaOrdenada);
		return listaOrdenada;
		
	}
	
	public boolean crossover()
	{
		
		return true;
	}
	
	public boolean mutacao()
	{
		
		return true;
	}
	
}
