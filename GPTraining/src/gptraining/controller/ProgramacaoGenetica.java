package gptraining.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import gptraining.model.ArvoreSintaticaExpressaoMatematica;
import gptraining.model.No;

/**
 * Classe onde ser� gerada a popula��o inicial, a medi��o e classifica��o por aptid�o, e onde ser�o realizadas as opera��es gen�ticas conforme o avan�o das gera��es.
 */
public class ProgramacaoGenetica {

	public List<ArvoreSintaticaExpressaoMatematica> evoluir(List<ArvoreSintaticaExpressaoMatematica> populacaoInicial, int numeroGeracoes)
	{
		
		return null;
	}
	
	public void avancarGeracao(List<ArvoreSintaticaExpressaoMatematica> populacao)
	{
		
	}
	
	/**
	 * Ordena por aptid�o as �rvores presentes na lista de entrada do m�todo.
	 */
	public List<ArvoreSintaticaExpressaoMatematica> avaliarAptidaoCandidatos(List<ArvoreSintaticaExpressaoMatematica> geracao, int[] vetX, int[] vetY)
	{
		ArvoreSintaticaExpressaoMatematica arvore = new ArvoreSintaticaExpressaoMatematica();
		for (int i = 0; i < geracao.size(); i++)
		{
			geracao.get(i).setAptidao(arvore.avaliarAptidaoArvore(geracao.get(i).getRaiz(), vetX, vetY));
		}
		Collections.sort(geracao);
		return geracao;
	}
	
	public boolean crossover()
	{
		
		return true;
	}
	
	public boolean mutacao()
	{
		
		return true;
	}
	
	public List<ArvoreSintaticaExpressaoMatematica> gerarPopulacaoInicial(int tamanhoPopulacao, int profundidadeArvores)
	{
		List<ArvoreSintaticaExpressaoMatematica> populacaoInicial = new ArrayList();
		
		if (tamanhoPopulacao % 2 != 0)
		{
			System.out.println("O tamanho da popula��o deve ser par, para que o n�mero de indiv�duos gerados com o m�todo Grow seja igual ao dos gerados om o m�todo Full. ");
			return null;
		}
		else
		{
			ArvoreSintaticaExpressaoMatematica arvoreExpressao = new ArvoreSintaticaExpressaoMatematica();
			int metadeTamanho = tamanhoPopulacao / 2;
			
			for (int i = 0; i < metadeTamanho; i++)
			{
				No raizMetade1 = arvoreExpressao.geraArvoreMetodoGrow(profundidadeArvores);
				while (arvoreExpressao.checaExistenciaDeNoX(raizMetade1) == false)
				{
					raizMetade1 = arvoreExpressao.geraArvoreMetodoGrow(profundidadeArvores);
				}
				populacaoInicial.add(new ArvoreSintaticaExpressaoMatematica(raizMetade1));
				
				No raizMetade2 = arvoreExpressao.geraArvoreMetodoFull(profundidadeArvores);
				while (arvoreExpressao.checaExistenciaDeNoX(raizMetade2) == false)
				{
					raizMetade2 = arvoreExpressao.geraArvoreMetodoFull(profundidadeArvores);
				}
				populacaoInicial.add(new ArvoreSintaticaExpressaoMatematica(raizMetade2));
			}
		}
		return populacaoInicial;
	}
}
